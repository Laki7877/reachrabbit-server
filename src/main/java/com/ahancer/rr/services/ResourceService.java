package com.ahancer.rr.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ahancer.rr.models.Resource;
import com.ahancer.rr.request.ResourceRemoteRequest;

@Service
public interface ResourceService {
	public Resource getById(Long id)  throws Exception;
	public String generateResourceName(String filename) throws Exception;
	public Resource upload(MultipartFile multipartFile) throws Exception;
	public Resource upload(ResourceRemoteRequest request) throws Exception;
}
