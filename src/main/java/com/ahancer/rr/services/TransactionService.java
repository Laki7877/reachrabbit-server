package com.ahancer.rr.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.BrandTransactionDocumentDao;
import com.ahancer.rr.daos.CartDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.BrandTransactionDocument;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.InfluencerTransactionDocument;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.User;
import com.ahancer.rr.models.Wallet;
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
	@Autowired
	private WalletDao walletDao;
	@Value("${ui.host}")
	private String uiHost;
	@Value("${app.proposal.tax}")
	private Double tax;
	
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
		Double tax = 0.0;
		if(null != user.getBrand().getIsCompany() && user.getBrand().getIsCompany()) {
			tax = sum * this.tax / 100;
		}
		Double total = sum - tax; 
		transaction.setAmount(total);
		transaction.setStatus(TransactionStatus.Pending);
		transaction.setUserId(brandId);
		transaction.setType(TransactionType.Payin);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 5);
		transaction.setExpiredAt(cal.getTime());
		transaction = transactionDao.save(transaction);
		transaction.setTransactionNumber(EncodeUtil.encodeLong(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		//create document
		BrandTransactionDocument document = new BrandTransactionDocument();
		document.setCartId(cart.getCartId());
		document.setTransactionId(transaction.getTransactionId());
		document.setAmount(sum);
		document.setType(DocumentType.Base);
		document.setCompanyAddress(user.getBrand().getCompanyAddress());
		document.setCompanyName(user.getBrand().getCompanyName());
		document.setCompanyTaxId(user.getBrand().getCompanyTaxId());
		document = brandTransactionDocumentDao.save(document);
		if(null != user.getBrand().getIsCompany() && user.getBrand().getIsCompany()) {
			BrandTransactionDocument taxDocument = new BrandTransactionDocument();
			taxDocument.setCartId(cart.getCartId());
			taxDocument.setTransactionId(transaction.getTransactionId());
			taxDocument.setAmount(-tax);
			taxDocument.setType(DocumentType.Tax);
			taxDocument.setCompanyAddress(user.getBrand().getCompanyAddress());
			taxDocument.setCompanyName(user.getBrand().getCompanyName());
			taxDocument.setCompanyTaxId(user.getBrand().getCompanyTaxId());
			taxDocument = brandTransactionDocumentDao.save(taxDocument);
		}
		//update cart status
		cart.setStatus(CartStatus.Checkout);
		cartDao.save(cart);
		
		String to = user.getEmail();
		String subject = messageSource.getMessage("email.brand.cart.checkout.subject",null,locale)
				.replace("{{Transaction Id}}", transaction.getTransactionNumber());
		String body = messageSource.getMessage("email.brand.cart.checkout.message",null,locale)
				.replace("{{Transaction Id}}", transaction.getTransactionNumber())
				.replace("{{Total Price}}", String.valueOf(transaction.getAmount()))
				.replace("{{Host}}", uiHost)
				.replace("{{Cart Id}}", String.valueOf(cart.getCartId()));
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
		return transactionDao.findByInfluencerTransactionDocumentWalletId(walletId);
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
		ProposalMessage robotMessage = new ProposalMessage();
		//robotMessage.setCreatedAt(new Date());
		String message = messageSource.getMessage("robot.proposal.working.status.message", null, locale)
				.replace("{{Brand Name}}", transaction.getUser().getBrand().getBrandName());
		User robotUser = robotService.getRobotUser();
		String to = StringUtils.EMPTY;
		String subject = StringUtils.EMPTY;
		String body = StringUtils.EMPTY;
		String superSubject = messageSource.getMessage("email.influencer.admin.confirm.checkout.subject",null,locale)
				.replace("{{Brand Name}}", transaction.getUser().getBrand().getBrandName());
		String superBody = messageSource.getMessage("email.influencer.admin.confirm.checkout.message",null,locale)
				.replace("{{Brand Name}}", transaction.getUser().getBrand().getBrandName())
				.replace("{{Host}}", uiHost);
		for(BrandTransactionDocument brandDoc : transaction.getBrandTransactionDocument()) {
			if(!DocumentType.Base.equals(brandDoc.getType())) {
				continue;
			}
			for(Proposal proposal : brandDoc.getCart().getProposals()){
				proposal.setStatus(ProposalStatus.Working);
				Integer days = proposal.getCompletionTime().getDay();
				cal.add(Calendar.DATE, days);
				proposal.setDueDate(cal.getTime());
				//setup robot message
				robotMessage.setMessage(message
						.replace("{{Influencer Name}}", proposal.getInfluencer().getUser().getName())
						.replace("{{Campaign Name}}", proposal.getCampaign().getTitle())
						.replace("{{Proposal Id}}", String.valueOf(proposal.getProposalId())));
				robotMessage.setProposal(proposal);
				//long polling
				proposalMessageService.createProposalMessage(proposal.getProposalId()
						, robotMessage
						, robotUser.getUserId()
						, robotUser.getRole());
				proposalService.processInboxPolling(proposal.getInfluencerId());
				proposalService.processInboxPolling(proposal.getCampaign().getBrandId());
				proposalMessageService.processMessagePolling(proposal.getProposalId());
				proposalDao.save(proposal);
				//send email to influencer
				to = proposal.getInfluencer().getUser().getEmail();
				subject = superSubject;
				body = superBody
						.replace("{{Brand Name}}", proposal.getCampaign().getBrand().getBrandName())
						.replace("{{Campaign Name}}", proposal.getCampaign().getTitle())
						.replace("{{Influencer Name}}", proposal.getInfluencer().getUser().getName())
						.replace("{{Proposal Id}}", String.valueOf(proposal.getProposalId()));
				emailService.send(to, subject, body);
			}
		}
		//send email to brand
		to = transaction.getUser().getEmail();
		subject = messageSource.getMessage("email.brand.admin.confirm.checkout.subject",null,locale)
				.replace("{{Transaction Id}}", transaction.getTransactionNumber());
		body = messageSource.getMessage("email.brand.admin.confirm.checkout.message",null,locale)
				.replace("{{Transaction Id}}", transaction.getTransactionNumber())
				.replace("{{Brand Name}}", transaction.getUser().getBrand().getBrandName())
				.replace("{{Total Price}}", String.valueOf(transaction.getAmount()))
				.replace("{{Host}}", uiHost);
		emailService.send(to, subject, body);
		
		return transaction;
	}
	
	public Transaction payTransaction(Long transactioId,  Resource resource, Locale locale) throws Exception {
		if(resource == null){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.transaction.slip.require");
		}
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
		//create document
		InfluencerTransactionDocument document = transaction.getInfluencerTransactionDocument().iterator().next();
		Wallet wallet = document.getWallet();
		wallet.setStatus(WalletStatus.Paid);
		wallet = walletDao.save(wallet);
		//send mail to influencer
		String to = transaction.getUser().getEmail();
		String subject = messageSource.getMessage("email.influencer.admin.confirm.payout.subject",null,locale);
		String body = messageSource.getMessage("email.influencer.admin.confirm.payout.message",null,locale)
				.replace("{{Payout Amount}}", transaction.getAmount().toString())
				.replace("{{Bank Name}}", document.getBank().getBankName())
				.replace("{{Bank Account Number}}", document.getAccountNumber())
				.replace("{{Bank Account Name}}", document.getAccountName())
				.replace("{{Host}}", uiHost);
		emailService.send(to, subject, body);
		return transaction;
	}
	
}
