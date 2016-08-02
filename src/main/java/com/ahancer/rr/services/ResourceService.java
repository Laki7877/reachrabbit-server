package com.ahancer.rr.services;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ahancer.rr.daos.ResourceDao;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.utils.S3Util;
import com.amazonaws.services.s3.model.PutObjectResult;

@Service
@Transactional(rollbackFor=Exception.class)
public class ResourceService {
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private S3Util s3Util;
	
	public String generateResourceName(String filename) throws Exception {
		String extension = FilenameUtils.getExtension(filename);
		
		//Encode with md5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(filename + LocalDateTime.now().toString()));
		return String.format("%032x", new BigInteger(1, md5.digest()));
	}
	
	public Resource upload(MultipartFile multipartFile) throws Exception {
		
		PutObjectResult s3Result = s3Util.upload(multipartFile.getInputStream(), generateResourceName(multipartFile.getOriginalFilename()));
		Resource resource = new Resource();
		
		return null;
	}
}
