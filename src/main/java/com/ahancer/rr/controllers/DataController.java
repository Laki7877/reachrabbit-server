package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.daos.BankDao;
import com.ahancer.rr.daos.BudgetDao;
import com.ahancer.rr.daos.CategoryDao;
import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Budget;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Media;

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

	@RequestMapping(value="/media",method=RequestMethod.GET)
	public Iterable<Media> getAllMedia() throws Exception{
		return mediaDao.findAll();
	}
	
	@RequestMapping(value="/categories",method=RequestMethod.GET)
	public Iterable<Category> getAllCategories() throws Exception{
		return categoryDao.findAll();
	}
	
	@RequestMapping(value="/banks",method=RequestMethod.GET)
	public Iterable<Bank> getAllBanks() throws Exception{
		return bankDao.findAll();
	}
	
	@RequestMapping(value="/budget",method=RequestMethod.GET)
	public Iterable<Budget> getBudget() throws Exception{
		return budgetDao.findAll();
	}
	
}
