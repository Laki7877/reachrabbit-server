package com.ahancer.rr;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.MessageCountResponse;
import com.ahancer.rr.response.UpdatePostResponse;
import com.ahancer.rr.services.EmailService;
import com.ahancer.rr.services.FacebookService;
import com.ahancer.rr.services.InstagramService;
import com.ahancer.rr.services.PostService;
import com.ahancer.rr.services.YoutubeService;
import com.ahancer.rr.utils.S3Util;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Component
public class ScheduledTask {
	@Autowired
	private ProposalMessageDao proposalMessageDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private S3Util s3Util;
	@Value("${ui.host}")
	private String uiHost;
	@Value("${server.env}")
	private String env;
	@Value("${cloud.aws.s3.bucket.backup}")
	private String backupBucket;
	@Value("${app.dashboard.history.days}")
	private Integer historyDays;
	@Autowired
	private PostService postService;
	@Autowired
	private FacebookService facebookService;
	@Autowired
	private YoutubeService youtubeService;
	@Autowired
	private InstagramService instagramService;
	@Value("${email.admin}")
	private String adminEmail;
	
	@Scheduled(cron="0 0 * * * *")
    public void sendEmailHourly() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		//cal.add(Calendar.MINUTE, -3);
		Date now = new Date();
		Locale locale = new Locale("th", "TH");
		
		List<MessageCountResponse> brandList = proposalMessageDao.findBrandMessageCount(false, cal.getTime(),now);
		
		String brandSubject = messageSource.getMessage("email.brand.unread.message.subject",null,locale);
		String brandBody = messageSource.getMessage("email.brand.unread.message.message",null,locale)
				.replace("{{Host}}", uiHost);
		
		for(MessageCountResponse message : brandList){
			String to = message.getEmail();
			emailService.send(to
					, brandSubject
					, brandBody.replace("{{Message Count}}", message.getMessageCount().toString()));
		}
		
		List<MessageCountResponse> influencerList = proposalMessageDao.findInfluencerMessageCount(false,cal.getTime(),now);
		String influencerSubject = messageSource.getMessage("email.influencer.unread.message.subject",null,locale);
		String influencerBody = messageSource.getMessage("email.influencer.unread.message.message",null,locale);
		for(MessageCountResponse message : influencerList){
			String to = message.getEmail();
			emailService.send(to
					, influencerSubject
					, influencerBody.replace("{{Message Count}}", message.getMessageCount().toString()));
		}
    }
	
	@Scheduled(cron="0 5 0 * * *")
	public void cleanBackupfileDaily() {
		if(!"production".equals(env)) {
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		List<S3ObjectSummary> list = s3Util.list(backupBucket);
		for(S3ObjectSummary summary : list){
			if("backup/".equals(summary.getKey())){
				continue;
			}
			if(summary.getLastModified().before(cal.getTime())){
				s3Util.delete(backupBucket, summary.getKey());
			}
		}
	}
	
	@Scheduled(cron="0 10 0 * * *")
	@Transactional(rollbackFor=Exception.class)
	public void getPostInfoDaily() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -historyDays);
		List<UpdatePostResponse> postList = postService.getUpdatePost(cal.getTime());
		for(UpdatePostResponse postModel : postList) {
			Post post = null;
			try {
				switch (postModel.getMediaId()){
				case "facebook":
					post = facebookService.getPostInfo(postModel.getSocialPostId());
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("facebook");
					break;
				case "instagram":
					post = instagramService.getPostInfo(postModel.getSocialPostId());
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("instagram");
					break;
				case "google":
					post = youtubeService.getPostInfo(postModel.getSocialPostId());
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("google");
					break;
				default:
					break;
				}
				if(null != post) {
					post.setUrl(postModel.getUrl());
					postService.createNewPostBySys(post);
				}
			} catch (Exception e) {
				e.printStackTrace();
				String trace = ExceptionUtils.getStackTrace(e);
				emailService.send("laki7877@gmail.com", "Stack trace update post information", trace);
			}
		}
		
	}
	

}
