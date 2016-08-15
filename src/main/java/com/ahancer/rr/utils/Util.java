package com.ahancer.rr.utils;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

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
}
