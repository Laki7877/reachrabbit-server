package com.ahancer.rr.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

@Component
public class S3Util {
	@Autowired
	private AmazonS3Client amazonS3Client;
	
	public String getUrl(String bucket, String key) {
		return amazonS3Client.getResourceUrl(bucket, key);
	}
	
	public PutObjectResult upload(String filePath,String bucket, String uploadKey) throws FileNotFoundException {
		return upload(new FileInputStream(filePath), bucket, uploadKey);
	}

	public PutObjectResult upload(InputStream inputStream, String bucket, String uploadKey) {
		return upload(inputStream, bucket, uploadKey, new ObjectMetadata());
	}
	public PutObjectResult upload(InputStream inputStream, String bucket, String uploadKey, ObjectMetadata metaData) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, metaData);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
		IOUtils.closeQuietly(inputStream, null);
		return putObjectResult;
	}

	public PutObjectResult upload(MultipartFile file, String bucket, String uploadKey) throws IOException {
		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentType(file.getContentType());
		return upload(file.getInputStream(), bucket, uploadKey, metaData);
	}

	public void delete(String bucket, String key){ 
		amazonS3Client.deleteObject(bucket, key);
	}
	
	public List<PutObjectResult> upload(MultipartFile[] multipartFiles, String bucket) {
		List<PutObjectResult> putObjectResults = new ArrayList<>();

		Arrays.stream(multipartFiles)
				.filter(multipartFile -> !StringUtils.isNullOrEmpty(multipartFile.getOriginalFilename()))
				.forEach(multipartFile -> {
					try {
						putObjectResults.add(upload(multipartFile.getInputStream(),bucket, multipartFile.getOriginalFilename()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

		return putObjectResults;
	}
	public ResponseEntity<byte[]> download(String bucket, String key) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

		S3Object s3Object = amazonS3Client.getObject(getObjectRequest);

		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	public List<S3ObjectSummary> list(String bucket) {
		ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));

		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

		return s3ObjectSummaries;
	}
}
