package com.ahancer.rr;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.response.MessageCountResponse;
import com.ahancer.rr.services.EmailService;
import com.ahancer.rr.services.PostService;
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
	@Autowired
	private PostService postService;
	
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
		String influencerBody = messageSource.getMessage("email.influencer.unread.message.message",null,locale)
				.replace("{{Host}}", uiHost);
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
		Calendar dataCal = Calendar.getInstance();
		dataCal.add(Calendar.DATE, -1);
		postService.createPostSchedule(dataCal.getTime());
	}
	

}
