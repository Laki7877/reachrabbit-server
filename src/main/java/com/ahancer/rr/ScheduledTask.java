package com.ahancer.rr;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	//@Scheduled(fixedRate = 60000)
	@Scheduled(fixedRate = 180000)
    public void reportCurrentTime() {
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.HOUR, -1);
		cal.add(Calendar.MINUTE, -3);
		Date now = new Date();
		List<MessageCountResponse> list = proposalMessageDao.findBrandMessageCount(false, cal.getTime(),now);
		list.addAll(proposalMessageDao.findInfluencerMessageCount(false,cal.getTime(),now));
		Locale locale = new Locale("th", "TH");
		String superSubject = messageSource.getMessage("email.unread.message.subject",null,locale);
		String superBody = messageSource.getMessage("email.unread.message.message",null,locale);
		for(MessageCountResponse message : list){
			String to = message.getEmail();
			emailService.send(to
					, superSubject
					, superBody.replaceAll("{{Message Count}}", message.getMessageCount().toString()));
		}
		
    }

}
