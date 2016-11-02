package com.ahancer.rr.services.impl;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.InfluencerMediaDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ReferralDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.InfluencerMediaId;
import com.ahancer.rr.models.Referral;
import com.ahancer.rr.models.User;
import com.ahancer.rr.request.InfluencerSignUpRequest;
import com.ahancer.rr.request.PayoutRequest;
import com.ahancer.rr.request.ProfileRequest;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.EmailService;
import com.ahancer.rr.services.InfluencerService;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncryptionUtil;
import com.ahancer.rr.utils.Util;

@Component
@Transactional(rollbackFor=Exception.class)
public class InfluencerServiceImpl implements InfluencerService {
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private InfluencerDao influencerDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProposalDao proposalDao;
	@Autowired
	private InfluencerMediaDao influencerMediaDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private MessageSource messageSource;
	@Value("${ui.host}")
	private String uiHost;
	@Autowired
	private EncryptionUtil encrypt;
	@Autowired
	private ReferralDao referralDao;

	public UserResponse updateInfluencerUser(Long userId, ProfileRequest request, String token) throws Exception {
		User user = userDao.findOne(userId);
		if(user == null || !Role.Influencer.equals(user.getRole())) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.not.found");
		}
		for(InfluencerMedia link2 : user.getInfluencer().getInfluencerMedias()) {
			boolean has = false;
			for(InfluencerMedia link: request.getInfluencer().getInfluencerMedias()) {
				InfluencerMediaId id = new InfluencerMediaId(userId, link.getMedia().getMediaId());
				link.setInfluencerMediaId(id);
				if(link.getMedia().getMediaId().equals(link2.getMedia().getMediaId())) {
					has = true;
					break;
				}
			}
			if(!has) {
				if(proposalDao.countByInfluencerIdAndMediaMediaId(userId, link2.getMedia().getMediaId()) > 0) {
					throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.has.proposals");
				}
			}
		}
		if(request.getInfluencer().getInfluencerMedias().size() == 0){
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.atlest.one");
		}
		influencerMediaDao.deleteByInfluencerId(userId);
		for(InfluencerMedia link: request.getInfluencer().getInfluencerMedias()) {
			influencerMediaDao.insertInfluencerMedia(userId,link.getMedia().getMediaId(),link.getFollowerCount(), link.getPageId(), link.getSocialId());
			InfluencerMediaId id = new InfluencerMediaId(userId,link.getMedia().getMediaId());
			link.setInfluencerMediaId(id);
		}
		//Validate duplicate Email
		if(StringUtils.isNotEmpty(user.getEmail()) 
				&& !user.getEmail().equals(request.getEmail())) {
			int countEmail = userDao.countByEmailAndRole(request.getEmail(),Role.Influencer);
			if(0 < countEmail){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.email.duplicate");
			}
		}
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		Influencer influencer = user.getInfluencer();
		influencer.setAbout(request.getInfluencer().getAbout());
		influencer.setAddress(request.getInfluencer().getAddress());
		influencer.setBirthday(request.getInfluencer().getBirthday());
		influencer.setCategories(request.getInfluencer().getCategories());
		influencer.setWeb(request.getInfluencer().getWeb());
		influencer.setGender(request.getInfluencer().getGender());
		influencer.setFullname(request.getInfluencer().getFullname());
		influencer.setIdCardNumber(request.getInfluencer().getIdCardNumber());
		influencer.setIdCard(request.getInfluencer().getIdCard());
		//Check if verify
		influencer.setIsVerify(false);
		if(StringUtils.isNotEmpty(influencer.getFullname())
				&& StringUtils.isNotEmpty(influencer.getAddress())
				&& StringUtils.isNotEmpty(influencer.getIdCardNumber())
				&& null != influencer.getIdCard() && 0L != influencer.getIdCard().getResourceId()) {
			influencer.setIsVerify(true);
		}
		user.setInfluencer(influencer);
		user.setProfilePicture(request.getProfilePicture());
		user = userDao.save(user);
		UserResponse userResponse = Util.getUserResponse(user);
		userResponse.getInfluencer().setInfluencerMedias(request.getInfluencer().getInfluencerMedias());
		cacheUtil.updateCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		return userResponse;
	}

	public UserResponse updateBankDetail(PayoutRequest request,Long influencerId, String token) throws Exception{
		User oldUser = userDao.findOne(influencerId);
		if(null == oldUser) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.not.found");
		}
		oldUser.getInfluencer().setBank(request.getBank());
		oldUser.getInfluencer().setAccountName(request.getAccountName());
		oldUser.getInfluencer().setAccountNumber(request.getAccountNumber());
		User user = userDao.save(oldUser);
		UserResponse userResponse = Util.getUserResponse(user);
		cacheUtil.updateCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
		return userResponse;
	}

	public User signupInfluencer(InfluencerSignUpRequest request,Locale locale) throws Exception {
		//Validate duplicate Email
		int emailCount = userDao.countByEmailAndRole(request.getEmail(),Role.Influencer);
		if(0 < emailCount) {
			throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"error.email.duplicate");
		} 
		//Setup user object
		User user = new User();
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setProfilePicture(request.getProfilePicture());
		user.setRole(Role.Influencer);
		String hashPassword = encrypt.hashPassword(request.getPassword());
		user.setPassword(hashPassword);
		if(StringUtils.isNotEmpty(request.getRef())) {
			Referral referral = referralDao.findOne(request.getRef());
			if(null != referral) {
				user.setReferral(referral);
			}
		}
		user = userDao.save(user);
		//Setup user object
		Influencer influencer = new Influencer();
		influencer.setIsVerify(false);
		influencer.setInfluencerId(user.getUserId());
		influencer.setInfluencerMedias(request.getInfluencerMedia());
		//Check if media link exists
		for(InfluencerMedia link : influencer.getInfluencerMedias()) {
			if(influencerMediaDao.countByMediaIdAndSocialId(link.getMedia().getMediaId(), link.getSocialId()) > 0) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.already.exist");
			}
			link.setInfluencer(influencer);
			link.setInfluencerMediaId(new InfluencerMediaId(influencer.getInfluencerId(), link.getMedia().getMediaId()));
		}
		influencer = influencerDao.save(influencer);
		user.setInfluencer(influencer);

		String to = user.getEmail();
		String subject = messageSource.getMessage("email.influencer.signup.subject",null,locale);
		String body = messageSource.getMessage("email.influencer.signup.message",null,locale)
				.replace("{{Infuencer Name}}", user.getName())
				.replace("{{Host}}", uiHost);
		emailService.send(to, subject, body);

		return user;
	}

}
