package com.ahancer.rr.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.models.MongoModel;

@RestController
@RequestMapping("/test")
public class Test {
	
//	@Autowired
//	private MongoDao mongoDao;

	
	@RequestMapping(method = RequestMethod.GET)
	public List<MongoModel> test() throws Exception{
		return null;
		
//		mongoDao.deleteAll();
//		MongoModel mongoModel = new MongoModel();
//		mongoModel.setFirstName("Laki");
//		mongoModel.setLastName("Sik");
//		mongoModel.setPhoneNumber("0874414008");
//		mongoModel = mongoDao.save(mongoModel);
//		return mongoDao.findAll();
	}

}
