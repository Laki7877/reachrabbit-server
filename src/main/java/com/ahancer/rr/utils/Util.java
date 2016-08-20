package com.ahancer.rr.utils;

import java.beans.FeatureDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.BrandResponse;
import com.ahancer.rr.response.InfluencerResponse;
import com.ahancer.rr.response.UserResponse;

public class Util {
	public static String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	public static void copyProperties(Object source, Object target, String ...ignoreProperties) {
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(ignoreProperties));
		set.addAll(Arrays.asList(getNullPropertyNames(source)));
		BeanUtils.copyProperties(source, target, set.toArray(new String[set.size()]));
	}
	
	public static Date parseJacksonDate(String utc) throws ParseException {
		if(utc == null) {
			return null;
		}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.parse(utc.replaceAll("Z$", "+0000"));
	}
	
	public static UserResponse getUserResponse(User user){
		UserResponse userResponse = new UserResponse();
		userResponse.setBank(user.getBank());
		userResponse.setBankAccount(user.getBankAccount());
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getName());
		userResponse.setPhoneNumber(user.getPhoneNumber());
		userResponse.setProfilePicture(user.getProfilePicture());
		userResponse.setRole(user.getRole());
		userResponse.setUserId(user.getUserId());
		if(Role.Brand == user.getRole()){
			BrandResponse brand = new BrandResponse();
			brand.setAbout(user.getBrand().getAbout());
			brand.setBrandId(user.getBrand().getBrandId());
			brand.setBrandName(user.getBrand().getBrandName());
			brand.setWebsite(user.getBrand().getWebsite());
			userResponse.setBrand(brand);
		}else if(Role.Influencer == user.getRole()){
			InfluencerResponse influencer = new InfluencerResponse();
			influencer.setAbout(user.getInfluencer().getAbout());
			influencer.setBirthday(user.getInfluencer().getBirthday());
			influencer.setCategories(user.getInfluencer().getCategories());
			influencer.setGender(user.getInfluencer().getGender());
			influencer.setInfluencerId(user.getInfluencer().getInfluencerId());
			influencer.setInfluencerMedias(user.getInfluencer().getInfluencerMedias());
			influencer.setWeb(user.getInfluencer().getWeb());
			userResponse.setInfluencer(influencer);
		}
		return userResponse;
	}
}
