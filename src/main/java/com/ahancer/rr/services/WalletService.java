package com.ahancer.rr.services;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.DocumentType;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.InfluencerTransactionDocumentDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerTransactionDocument;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.response.WalletAmount;
import com.ahancer.rr.utils.EncodeUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class WalletService {
	@Autowired
	private WalletDao walletDao;
	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private InfluencerTransactionDocumentDao influencerTransactionDocumentDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EmailService emailService;
	@Value("${email.admin}")
	private String adminEmail;
	@Value("${app.proposal.tax}")
	private Double tax;

	public Wallet findPendingByIndluencer(Long influencerId) {
		return walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.WaitForPayout);
	}
	
	public Transaction payoutWallet(PayoutRequest request, Long influencerId,Locale locale) throws Exception {
		Wallet wallet = walletDao.findByInfluencerIdAndStatus(influencerId, WalletStatus.WaitForPayout);
		if(null == wallet){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.not.exist");
		}
		if(null == wallet.getInfluencer().getIsVerify() || !wallet.getInfluencer().getIsVerify()) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.infleucner.not.verify");
		}
		if(0 == wallet.getProposals().size()){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.wallet.empty.proposal");
		}
		wallet.setStatus(WalletStatus.Pending);
		wallet = walletDao.save(wallet);
		//setup transaction
		Transaction transaction = new Transaction();
		transaction.setCompletedAt(new Date());
		transaction.setStatus(TransactionStatus.Pending);
		transaction.setType(TransactionType.Payout);
		transaction.setUserId(influencerId);
		transaction = transactionDao.save(transaction);
		double sum = 0.0;
		for(Proposal proposal : wallet.getProposals()){
			InfluencerTransactionDocument baseDocument = new InfluencerTransactionDocument();
			baseDocument.setProposal(proposal);
			baseDocument.setAccountName(request.getAccountName());
			baseDocument.setAccountNumber(request.getAccountNumber());
			baseDocument.setAmount(proposal.getPrice());
			baseDocument.setBank(request.getBank());
			baseDocument.setTransactionId(transaction.getTransactionId());
			baseDocument.setType(DocumentType.Base);
			baseDocument.setWalletId(wallet.getWalletId());
			baseDocument.setFullname(wallet.getInfluencer().getFullname());
			baseDocument.setAddress(wallet.getInfluencer().getAddress());
			baseDocument.setIdCardNumber(wallet.getInfluencer().getIdCardNumber());
			baseDocument.setIdCard(wallet.getInfluencer().getIdCard());
			baseDocument = influencerTransactionDocumentDao.save(baseDocument);
			if(proposal.getCampaign().getBrand().getIsCompany()){
				InfluencerTransactionDocument taxDocument = new InfluencerTransactionDocument();
				taxDocument.setProposal(proposal);
				taxDocument.setAccountName(request.getAccountName());
				taxDocument.setAccountNumber(request.getAccountNumber());
				taxDocument.setAmount(-(proposal.getPrice()*0.03));
				taxDocument.setBank(request.getBank());
				taxDocument.setTransactionId(transaction.getTransactionId());
				taxDocument.setType(DocumentType.Tax);
				taxDocument.setWalletId(wallet.getWalletId());
				taxDocument.setFullname(wallet.getInfluencer().getFullname());
				taxDocument.setAddress(wallet.getInfluencer().getAddress());
				taxDocument.setIdCardNumber(wallet.getInfluencer().getIdCardNumber());
				taxDocument.setIdCard(wallet.getInfluencer().getIdCard());
				taxDocument = influencerTransactionDocumentDao.save(taxDocument);
				sum = sum + taxDocument.getAmount();
			}
			//fee document
			InfluencerTransactionDocument feeDocument = new InfluencerTransactionDocument();
			feeDocument.setProposal(proposal);
			feeDocument.setAccountName(request.getAccountName());
			feeDocument.setAccountNumber(request.getAccountNumber());
			feeDocument.setAmount(-proposal.getFee());
			feeDocument.setBank(request.getBank());
			feeDocument.setTransactionId(transaction.getTransactionId());
			feeDocument.setType(DocumentType.Fee);
			feeDocument.setWalletId(wallet.getWalletId());
			feeDocument.setFullname(wallet.getInfluencer().getFullname());
			feeDocument.setAddress(wallet.getInfluencer().getAddress());
			feeDocument.setIdCardNumber(wallet.getInfluencer().getIdCardNumber());
			feeDocument.setIdCard(wallet.getInfluencer().getIdCard());
			feeDocument = influencerTransactionDocumentDao.save(feeDocument);
			sum = sum + baseDocument.getAmount() + feeDocument.getAmount();
		}
		//transfer fee
		InfluencerTransactionDocument transferFeeDocument = new InfluencerTransactionDocument();
		transferFeeDocument.setAccountName(request.getAccountName());
		transferFeeDocument.setAccountNumber(request.getAccountNumber());
		transferFeeDocument.setAmount(-30.0);
		transferFeeDocument.setBank(request.getBank());
		transferFeeDocument.setTransactionId(transaction.getTransactionId());
		transferFeeDocument.setType(DocumentType.TransferFee);
		transferFeeDocument.setWalletId(wallet.getWalletId());
		transferFeeDocument.setFullname(wallet.getInfluencer().getFullname());
		transferFeeDocument.setAddress(wallet.getInfluencer().getAddress());
		transferFeeDocument.setIdCardNumber(wallet.getInfluencer().getIdCardNumber());
		transferFeeDocument.setIdCard(wallet.getInfluencer().getIdCard());
		transferFeeDocument = influencerTransactionDocumentDao.save(transferFeeDocument);
		sum += transferFeeDocument.getAmount();
		
		transaction.setAmount(sum);
		transaction.setTransactionNumber(EncodeUtil.encodeLong(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		
		//send email to admin
		String to = adminEmail;
		String subject = messageSource.getMessage("email.admin.influencer.payout.subject",null,locale)
				.replace("{{Influencer Name}}", wallet.getInfluencer().getUser().getName());
		String body = messageSource.getMessage("email.admin.influencer.payout.message",null,locale)
				.replace("{{Influencer Name}}", wallet.getInfluencer().getUser().getName())
				.replace("{{Payout Amount}}", transaction.getAmount().toString())
				.replace("{{Transaction Id}}", transaction.getTransactionNumber())
				.replace("{{Bank Name}}", request.getBank().getBankName())
				.replace("{{Bank Account Number}}", request.getAccountNumber())
				.replace("{{Bank Account Name}}", request.getAccountName());
		emailService.send(to, subject, body);
		
		return transaction;
	}
	
	public WalletAmount getWalletAmount(Long influencerId) {
		WalletAmount response = new WalletAmount();
		response.setAmount(0.0);
		Wallet wallet = findPendingByIndluencer(influencerId);
		if(null != wallet) {
			Double sum = 0.0;
			for(Proposal proposal : wallet.getProposals()) {
				sum = sum + proposal.getPrice() - proposal.getFee();
				if(proposal.getCampaign().getBrand().getIsCompany()){
					sum = sum - (proposal.getPrice() * this.tax / 100);
				}
			}
			response.setAmount(sum);
		}
		return response;
	}

}
