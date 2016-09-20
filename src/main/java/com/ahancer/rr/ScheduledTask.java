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

import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.response.MessageCountResponse;
import com.ahancer.rr.services.EmailService;

@Component
public class ScheduledTask {
	
	@Autowired
	private ProposalMessageDao proposalMessageDao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("${ui.host}")
	private String uiHost;
	
	//@Scheduled(fixedRate = 60000)
	@Scheduled(cron="0 0 * * * *")
	//@Scheduled(fixedRate = 180000)
    public void reportCurrentTime() {
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

}
