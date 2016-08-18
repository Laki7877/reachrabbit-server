package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.google.api.client.util.Lists;

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
	public List<Media> getAllMedia() throws Exception{
		return Lists.newArrayList(mediaDao.findAll());
	}
	
	@RequestMapping(value="/categories",method=RequestMethod.GET)
	public List<Category> getAllCategories() throws Exception{
		return Lists.newArrayList(categoryDao.findAll());
	}
	
	@RequestMapping(value="/banks",method=RequestMethod.GET)
	public List<Bank> getAllBanks() throws Exception{
		return Lists.newArrayList(bankDao.findAll());
	}
	
	@RequestMapping(value="/budget",method=RequestMethod.GET)
	public List<Budget> getBudget() throws Exception{
		return Lists.newArrayList(budgetDao.findAll());
	}
	
	@RequestMapping(value="/completiontime",method=RequestMethod.GET)
	public List<CompletionTime> getCompletionTime() throws Exception{
		return Lists.newArrayList(completionTimeDao.findAll());
	}
	
}
