package com.ahancer.rr.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.CartStatus;
import com.ahancer.rr.custom.type.DocumentType;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.daos.BrandTransactionDocumentDao;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.BrandTransactionDocument;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.InfluencerTransactionDocument;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.EncodeUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class TransactionService {
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private ProposalDao proposalDao;
	
	@Autowired
	private BrandTransactionDocumentDao brandTransactionDocumentDao;
	
	@Autowired
	private ProposalMessageService proposalMessageService;
	
	@Autowired
	private RobotService robotService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private EmailService emailService;
	
	public Transaction createTransactionByBrand(UserResponse user,Locale locale) throws Exception {
		Long brandId = user.getBrand().getBrandId();
		Cart cart = cartDao.findByBrandIdAndStatus(brandId, CartStatus.Incart);
		if(null == cart){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.cart.not.exist");
		}
		if(0 == cart.getProposals().size()){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.cart.empty.proposal");
		}
		//create transaction
		Transaction transaction = new Transaction();
		Double sum = cart.getProposals().stream().mapToDouble(o -> o.getPrice()).sum();
		transaction.setAmount(sum);
		transaction.setStatus(TransactionStatus.Pending);
		transaction.setUserId(brandId);
		transaction.setType(TransactionType.Payin);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		transaction.setExpiredAt(cal.getTime());
		transaction = transactionDao.save(transaction);
		transaction.setTransactionNumber(EncodeUtil.encode(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		//create document
		BrandTransactionDocument document = new BrandTransactionDocument();
		document.setCartId(cart.getCartId());
		document.setTransactionId(transaction.getTransactionId());
		document.setAmount(sum);
		document.setType(DocumentType.Base);
		brandTransactionDocumentDao.save(document);
		//update cart status
		cart.setStatus(CartStatus.Checkout);
		cartDao.save(cart);
		
		String to = user.getEmail();
		String subject = messageSource.getMessage("email.brand.cart.checkout.subject",null,locale);
		String body = messageSource.getMessage("email.brand.cart.checkout.message",null,locale);
		emailService.send(to, subject, body);
		
		return transaction;
	}
	
	public Page<Transaction> findAllByUserTransaction(TransactionType type,Long userId,Pageable pageable) {
		return transactionDao.findByTypeAndUserId(type,userId, pageable);
	}
	
	public Page<Transaction> findAllTransactions(TransactionType type,Pageable pageable) {
		return transactionDao.findByType(type,pageable);
	}
	
	public Transaction findOneTransactionFromCartByAdmin(Long cartId){
		return transactionDao.findByBrandTransactionDocumentCartId(cartId);
	}
	
	public Transaction findOneTransactionFromWalletByAdmin(Long walletId){
		return transactionDao.findByBrandTransactionDocumentCartId(walletId);
	}
	
	public Transaction findOneTransactionFromWalletByInfluencer(Long walletId,Long influencerId){
		return transactionDao.findByInfluencerTransactionDocumentWalletIdAndUserId(walletId, influencerId);
	}
	
	public Transaction findOneTransactionFromCartByBrand(Long cartId,Long brandId){
		return transactionDao.findByBrandTransactionDocumentCartIdAndUserId(cartId, brandId);
	}
	
	public Transaction findOneTransaction(Long transactionId,Long uerId){
		return transactionDao.findByTransactionIdAndUserId(transactionId,uerId);
	}
	
	public Transaction findOneTransactionByAdmin(Long transactionId){
		return transactionDao.findOne(transactionId);
	}

	
	public Transaction confirmTransaction(Long transactioId, Locale locale) throws Exception {
		Transaction transaction = transactionDao.findOne(transactioId);
		if(null == transaction){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.not.exist");
		}
		if(!TransactionStatus.Pending.equals(transaction.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.invalid.status");
		}
		Date now = new Date();
		if(now.after(transaction.getExpiredAt())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.expired");
		}
		//update transaction status
		transaction.setCompletedAt(now);
		transaction.setStatus(TransactionStatus.Complete);
		transaction = transactionDao.save(transaction);
		
		//update proposal status
		Calendar cal = Calendar.getInstance();
		ProposalMessage message = new ProposalMessage();
		message.setMessage(messageSource.getMessage("robot.proposal.working.status.message", null, locale));
		User robotUser = robotService.getRobotUser();
		String to = StringUtils.EMPTY;
		String subject = StringUtils.EMPTY;
		String body = StringUtils.EMPTY;
		
		String superSubject = messageSource.getMessage("email.influencer.admin.confirm.checkout.subject",null,locale);
		String superBody = messageSource.getMessage("email.influencer.admin.confirm.checkout.message",null,locale);
		
		for(Proposal proposal : transaction.getBrandTransactionDocument().getCart().getProposals()){
			proposal.setStatus(ProposalStatus.Working);
			Integer days = proposal.getCompletionTime().getDay();
			cal.add(Calendar.DATE, days);
			proposal.setDueDate(cal.getTime());
			message.setProposal(proposal);
			proposalMessageService.createProposalMessage(proposal.getProposalId()
					, message
					, robotUser.getUserId()
					, robotUser.getRole());
			proposalService.processInboxPollingByOne(proposal.getInfluencerId());
			proposalService.processInboxPollingByOne(proposal.getCampaign().getBrandId());
			proposalMessageService.processMessagePolling(proposal.getProposalId());
			proposalDao.save(proposal);
			
			//send email to influencer
			to = proposal.getInfluencer().getUser().getEmail();
			subject = superSubject;
			body = superBody
					.replace("{{Brand Name}}", proposal.getCampaign().getBrand().getBrandName())
					.replace("{{Campaign Name}}", proposal.getCampaign().getTitle());
			emailService.send(to, subject, body);
		}
		
		//send email to brand
		to = transaction.getUser().getEmail();
		subject = messageSource.getMessage("email.brand.admin.confirm.checkout.subject",null,locale);
		body = messageSource.getMessage("email.brand.admin.confirm.checkout.message",null,locale).replace("{{Transaction ID}}", transaction.getTransactionNumber());
		emailService.send(to, subject, body);
		
		return transaction;
	}
	
	
	public Transaction payTransaction(Long transactioId,  Resource resource, Locale locale) throws Exception {
		Transaction transaction = transactionDao.findOne(transactioId);
		if(null == transaction){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.not.exist");
		}
		if(!TransactionStatus.Pending.equals(transaction.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.invalid.status");
		}
		Date now = new Date();
		//update transaction status
		transaction.setCompletedAt(now);
		transaction.setStatus(TransactionStatus.Complete);
		transaction.setSlip(resource);
		transaction = transactionDao.save(transaction);
		
		InfluencerTransactionDocument doc = transaction.getInfluencerTransactionDocument().iterator().next();
		
		//send mail to influencer
		String to = transaction.getUser().getEmail();
		String subject = messageSource.getMessage("email.influencer.admin.confirm.payout.subject",null,locale);
		String body = messageSource.getMessage("email.influencer.admin.confirm.payout.message",null,locale)
				.replace("{{Payout Amount}}", transaction.getAmount().toString())
				.replace("{{Bank Name}}", doc.getBank().getBankName())
				.replace("{{Bank Account Number}}", doc.getAccountNumber())
				.replace("{{Bank Account Name}}", doc.getAccountName());
		emailService.send(to, subject, body);
		
		return transaction;
	}
	
}
