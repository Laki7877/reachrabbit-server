package com.ahancer.rr.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

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
		Date date = new DateTime(utc).toDate();
        return date;
	}
	public static UserResponse getUserResponse(User user){
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getName());
		userResponse.setPhoneNumber(user.getPhoneNumber());
		userResponse.setProfilePicture(user.getProfilePicture());
		userResponse.setRole(user.getRole());
		userResponse.setUserId(user.getUserId());
		switch(user.getRole()){
		case Brand:
			user.getBrand().setUser(user);
			BrandResponse brand = new BrandResponse(user.getBrand(),"Brand");
			userResponse.setBrand(brand);
			break;
		case Influencer:
			user.getInfluencer().setUser(user);
			InfluencerResponse influencer = new InfluencerResponse(user.getInfluencer(),"Influencer");
			userResponse.setInfluencer(influencer);
			break;
		case Admin:
			break;
		default:
			break;
		}
		return userResponse;
	}
	
	public static File compressImage(BufferedImage image, String destImage, float compressRatio) throws FileNotFoundException, IOException{
		File file = new File(destImage);
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(compressRatio);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        writer.setOutput(new FileImageOutputStream(
                file));

        writer.write(null, new IIOImage(image, null, null), jpegParams);
        return file;
	}
	public static File resizeImage(InputStream sourceImg, String destImg, Integer Width, Integer Height) throws Exception {
        BufferedImage origImage;
        origImage = ImageIO.read(sourceImg);
        if(Height > 0 && Width > 0){
            int fHeight = Height;
            int fWidth = Width;
        	//Note: If both W , H is 0 then u don't need to resize 
        	//Work out the resized width/height
        	int type = origImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : origImage.getType();
	        if (origImage.getHeight() > Height || origImage.getWidth() > Width) {
	            fHeight = Height;
	            int wid = Width;
	            float sum = (float)origImage.getWidth() / (float)origImage.getHeight();
	            fWidth = Math.round(fHeight * sum);
	            if (fWidth > wid) {
	                fHeight = Math.round(wid/sum);
	                fWidth = wid;
	            }
	        }
	        BufferedImage resizedImage = new BufferedImage(fWidth, fHeight, type);
	        Graphics2D g = resizedImage.createGraphics();
	        g.setComposite(AlphaComposite.Src);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.drawImage(origImage, 0, 0, fWidth, fHeight, null);
	        g.dispose();
	        return compressImage(resizedImage, destImg, 0.65f);
        }
        return compressImage(origImage, destImg, 0.65f);
    }
	
}
