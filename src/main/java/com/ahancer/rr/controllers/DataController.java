package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import io.swagger.annotations.ApiOperation;

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
	@Autowired
	private S3Util s3Util;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	@ApiOperation(value = "Get media list")
	@RequestMapping(value="/media",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Media> getAllMedia() throws Exception{
		return mediaDao.findAllByOrderByMediaId();
	}
	@ApiOperation(value = "Get category list")
	@RequestMapping(value="/categories",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Category> getAllCategories() throws Exception{
		return categoryDao.findAllByOrderByCategoryId();
	}
	@ApiOperation(value = "Get bank list")
	@RequestMapping(value="/banks",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Bank> getAllBanks() throws Exception{
		return bankDao.findAllByIsActiveTrueOrderByBankId();
	}
	@ApiOperation(value = "Get budget list")
	@RequestMapping(value="/budgets",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<Budget> getBudget() throws Exception{
		return budgetDao.findAllByOrderByBudgetId();
	}
	@ApiOperation(value = "Get completion time list")
	@RequestMapping(value="/completiontime",method=RequestMethod.GET)
	@Authorization({Role.Brand,Role.Influencer,Role.Admin})
	public List<CompletionTime> getCompletionTime() throws Exception{
		return completionTimeDao.findAllByOrderByCompletionId();
	}
	@ApiOperation(value = "Clear s3 picture")
	@RequestMapping(value="/clear",method=RequestMethod.DELETE)
	@Authorization(Role.Admin)
	public int clearS3() throws Exception {
		 List<S3ObjectSummary> list = s3Util.list(bucket);
		 for(S3ObjectSummary obj : list){
			 if("placeholder-profile-picture-bot.png".equals(obj.getKey())){
				 continue;
			 }
			 s3Util.delete(bucket,obj.getKey());
		 }
		 return list.size();
	}
}
