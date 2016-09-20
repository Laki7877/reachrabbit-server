package com.ahancer.rr.utils;

import java.beans.FeatureDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
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
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        return formatter.parse(utc.replaceAll("Z$", "+0000"));
	}
	
	public static UserResponse getUserResponse(User user){
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getName());
		userResponse.setPhoneNumber(user.getPhoneNumber());
		userResponse.setProfilePicture(user.getProfilePicture());
		userResponse.setRole(user.getRole());
		userResponse.setUserId(user.getUserId());
		if(Role.Brand == user.getRole()){
			user.getBrand().setUser(user);
			BrandResponse brand = new BrandResponse(user.getBrand(),"Brand");
			userResponse.setBrand(brand);
		}else if(Role.Influencer == user.getRole()){
			user.getInfluencer().setUser(user);
			InfluencerResponse influencer = new InfluencerResponse(user.getInfluencer(),"Influencer");
			userResponse.setInfluencer(influencer);
		}
		return userResponse;
	}
}
