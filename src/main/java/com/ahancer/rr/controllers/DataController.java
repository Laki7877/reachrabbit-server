package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.annotations.Authorization;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.BankDao;
import com.ahancer.rr.daos.BudgetDao;
import com.ahancer.rr.daos.CategoryDao;
import com.ahancer.rr.daos.CompletionTimeDao;
import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Budget;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.CompletionTime;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.utils.S3Util;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@RestController
@RequestMapping("/data")
public class DataController {
	
	@Autowired
	private MediaDao mediaDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private BankDao bankDao;
	
	@Autowired
	private BudgetDao budgetDao;
	
	@Autowired
	private CompletionTimeDao completionTimeDao;

	@RequestMapping(value="/media",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Media> getAllMedia() throws Exception{
		return mediaDao.findAllByOrderByMediaId();
	}
	
	@RequestMapping(value="/categories",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Category> getAllCategories() throws Exception{
		return categoryDao.findAllByOrderByCategoryId();
	}
	
	@RequestMapping(value="/banks",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Bank> getAllBanks() throws Exception{
		return bankDao.findAllByIsActiveTrueOrderByBankId();
	}
	
	@RequestMapping(value="/budgets",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Budget> getBudget() throws Exception{
		return budgetDao.findAllByOrderByBudgetId();
	}
	
	@RequestMapping(value="/completiontime",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<CompletionTime> getCompletionTime() throws Exception{
		return completionTimeDao.findAllByOrderByCompletionId();
	}
	
	
	@Autowired
	private S3Util s3Util;
	
	@RequestMapping(value="/clear",method=RequestMethod.DELETE)
	@Authorization(Role.Admin)
	public int clearS3() throws Exception {
		 List<S3ObjectSummary> list = s3Util.list();
		 for(S3ObjectSummary obj : list){
			 if("placeholder-profile-picture-bot.png".equals(obj.getKey())){
				 continue;
			 }
			 s3Util.delete(obj.getKey());
		 }
		 return list.size();
	}
	
}
