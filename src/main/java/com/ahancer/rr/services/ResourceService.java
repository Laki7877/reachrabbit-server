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

import com.ahancer.rr.custom.type.ResourceType;
import com.ahancer.rr.daos.ResourceDao;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.utils.S3Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class ResourceService {
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private S3Util s3Util;
	
	public Resource getById(Long id) {
		return resourceDao.findOne(id);
	}
	
	public String generateResourceName(String filename) throws Exception {
		//Get extension
		String extension = FilenameUtils.getExtension(filename);
		
		//Encode with md5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(filename + LocalDateTime.now().toString()));
		
		//{MD5}.{extension}
		return String.format("%032x", new BigInteger(1, md5.digest())) + "." + extension;
	}
	
	public Resource upload(MultipartFile multipartFile) throws Exception {
		//Upload with generated name
		String resourcePath = generateResourceName(multipartFile.getOriginalFilename());
		s3Util.upload(multipartFile.getInputStream(), resourcePath);
		
		//Save resource
		Resource resource = new Resource();
		resource.setResourcePath(resourcePath);
		resource.setResourceType(ResourceType.Binary);
		return resourceDao.save(resource);
	}
}
