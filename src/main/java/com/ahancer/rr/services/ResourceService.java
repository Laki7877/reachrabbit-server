package com.ahancer.rr.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.ResourceType;
import com.ahancer.rr.daos.ResourceDao;
import com.ahancer.rr.models.Resource;
import com.ahancer.rr.request.ResourceRemoteRequest;
import com.ahancer.rr.utils.S3Util;
import com.ahancer.rr.utils.Util;

@Service
@Transactional(rollbackFor=Exception.class)
public class ResourceService {
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private S3Util s3Util;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	public Resource getById(Long id) {
		return resourceDao.findOne(id);
	}
	
	public String generateResourceName(String filename) throws Exception {
		//Get extension
		String extension = FilenameUtils.getExtension(filename).split("\\?")[0];
		
		//Encode with md5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(filename + LocalDateTime.now().toString()));
		
		//{MD5}.{extension}
		return String.format("%032x", new BigInteger(1, md5.digest())) + "." + extension;
	}
	
	public Resource upload(MultipartFile multipartFile) throws Exception {
		//Upload with generated name
		String resourcePath = generateResourceName(multipartFile.getOriginalFilename());
		String desPath = ApplicationConstant.TemporaryFolder + "/" + resourcePath;
		File file = null;
		try {
			BufferedImage inputImage = ImageIO.read(multipartFile.getInputStream());
			BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(),
					inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			newBufferedImage.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);

			file = Util.compressImage(newBufferedImage, desPath, 0.7f);
			s3Util.upload(desPath, bucket, resourcePath);
			//Save resource
			Resource resource = new Resource();
			resource.setResourcePath(resourcePath);
			return resourceDao.save(resource);
		}
		catch(Exception e) {
			s3Util.delete(bucket,resourcePath);
			throw e;
		} finally {
			if(null != file){
				file.delete();
			}
		}
	}
	
	public Resource upload(ResourceRemoteRequest request) throws Exception {
		//Download from remote url
		URL url = new URL(request.getUrl());
		URLConnection conn = url.openConnection();
		conn.connect();
		
		//Generate resource name
		String resourcePath = generateResourceName(url.getFile());

		//Upload to s3
		s3Util.upload(conn.getInputStream(), bucket, resourcePath);
		
		//Save resource
		try {
			Resource resource = new Resource();
			resource.setResourceType(ResourceType.Image);
			resource.setResourcePath(resourcePath);
			return resourceDao.save(resource);
		}
		catch(Exception e) {
			s3Util.delete(bucket, resourcePath);
			throw e;
		}
	}
}
