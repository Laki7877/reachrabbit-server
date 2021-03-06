package com.ahancer.rr.services.impl;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.FacebookProfileResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.services.FacebookService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
@Transactional(rollbackFor=Exception.class)
public class FacebookServiceImpl implements FacebookService {
	@Value("${facebook.appKey}")
	private String appKey;
	@Value("${facebook.appSecret}")
	private String appSecret;
	private FacebookConnectionFactory connectionFactory;
	@Autowired
	private MediaDao mediaDao;
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostConstruct
	public void init() {
		connectionFactory = new FacebookConnectionFactory(appKey, appSecret);
		connectionFactory.setScope("manage_pages,publish_pages");
	}
	
	public Facebook getInstance(String accessToken) throws Exception {
		Facebook fb = new FacebookTemplate(accessToken);
		if(!fb.isAuthorized()) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.unauthorized");
		}
		return fb;
	}
	
	public String getAccessToken(String authorizationCode, String redirectUri) throws Exception {
		AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(authorizationCode, redirectUri, null);
		return accessGrant.getAccessToken();
	}
	
	public String getAppAccessToken() throws Exception {
		return appKey + "|" + appSecret;
	}
	
	public Post getPostInfo(String postId) throws Exception {
		Gson gson = new Gson();
		Facebook fb = getInstance(getAppAccessToken());
		org.springframework.social.facebook.api.Post post = fb.fetchObject(postId, org.springframework.social.facebook.api.Post.class, "comments.limit(0).summary(true),likes.limit(0).summary(true),shares,reactions.limit(0).summary(true)");
		JsonObject ext = gson.toJsonTree(post).getAsJsonObject().getAsJsonObject("extraData");
		Long likes = ext.getAsJsonObject("reactions").getAsJsonObject("summary").get("total_count").getAsLong();
		Long comments = ext.getAsJsonObject("comments").getAsJsonObject("summary").get("total_count").getAsLong();
		Long shares = (long) post.getShares();
		Post postModel = new Post();
		postModel.setViewCount(0L);
		postModel.setCommentCount(comments);
		postModel.setLikeCount(likes);
		postModel.setShareCount(shares);
		postModel.setSocialPostId(postId);
		return postModel;
	}
	
	public FacebookProfileResponse getProfile(String pageId) throws Exception {
		Gson gson = new Gson();
		Facebook fb = getInstance(getAppAccessToken());
		Page page = fb.fetchObject(pageId, Page.class, "engagement", "posts.limit(20){message,comments.limit(0).summary(true),shares,likes.limit(0).summary(true),full_picture,source,link}", "name", "id", "picture.type(large)", "link");
		JsonObject ext = gson.toJsonTree(page).getAsJsonObject().getAsJsonObject("extraData");
		
		//New facebook profile object
		FacebookProfileResponse resp = new FacebookProfileResponse();
		resp.setIsPage(true);
		
		resp.setId(page.getId());
		resp.setName(page.getName());
		resp.setPicture(ext.getAsJsonObject("picture").getAsJsonObject("data").get("url").getAsString());
		resp.setLikes(BigInteger.valueOf(page.getEngagement().getCount()));
		resp.setLink(page.getLink());
		
		// get posts info
		JsonArray arr = ext.getAsJsonObject("posts").getAsJsonArray("data");
		List<FacebookProfileResponse.Post> posts = new ArrayList<>();
		BigInteger averageLikes = BigInteger.ZERO;
		BigInteger averageComments = BigInteger.ZERO;
		BigInteger averageShares = BigInteger.ZERO;
		
		//convert json to obj
		for(JsonElement elm : arr) {
			JsonObject o = elm.getAsJsonObject();
			FacebookProfileResponse.Post post = new FacebookProfileResponse.Post();

			//set everyhing
			if(o.has("full_picture")) {
				post.setPicture(o.getAsJsonPrimitive("full_picture").getAsString());
			}
			if(o.has("source")) {
				post.setVideo(o.getAsJsonPrimitive("source").getAsString());
			}
			
			if(o.has("message")) {
				post.setMessage(o.getAsJsonPrimitive("message").getAsString());
			}
			post.setLikes(BigInteger.valueOf(0));
			if(o.has("likes") && !o.getAsJsonObject("likes").isJsonNull()){
				JsonObject likes = o.getAsJsonObject("likes");
				if(likes.has("summary") && !likes.getAsJsonObject("summary").isJsonNull()){
					JsonObject summary = likes.getAsJsonObject("summary");
					if(summary.has("total_count") && summary.get("total_count").isJsonPrimitive()){
						post.setLikes(o.getAsJsonObject("likes").getAsJsonObject("summary").get("total_count").getAsBigInteger());
					}
				}
			}
			post.setComments(BigInteger.valueOf(0));
			if(o.has("comments") && !o.getAsJsonObject("comments").isJsonNull()){
				JsonObject commentes = o.getAsJsonObject("comments");
				if(commentes.has("summary") && !commentes.getAsJsonObject("summary").isJsonNull()){
					JsonObject summary = commentes.getAsJsonObject("summary");
					if(summary.has("total_count") && summary.get("total_count").isJsonPrimitive()){
						post.setComments(o.getAsJsonObject("comments").getAsJsonObject("summary").get("total_count").getAsBigInteger());
					}
				}
			}
			
			if(o.has("shares")) {
				post.setShares(o.getAsJsonObject("shares").get("count").getAsBigInteger());
			} else {
				post.setShares(BigInteger.ZERO);
			}
			
			averageLikes = averageLikes.add(post.getLikes());
			averageComments = averageComments.add(post.getComments());
			averageShares = averageShares.add(post.getShares());
			posts.add(post);
		}
		
		averageLikes = averageLikes.divide(BigInteger.valueOf(posts.size()));
		averageComments = averageComments.divide(BigInteger.valueOf(posts.size()));
		averageShares = averageShares.divide(BigInteger.valueOf(posts.size()));
		
		resp.setPosts(posts);
		resp.setAverageLikes(averageLikes);
		resp.setAverageComments(averageComments);
		resp.setAverageShares(averageShares);
		
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	public OAuthenticationResponse authenticate(String accessToken, String ip) throws Exception {
		Facebook fb = getInstance(accessToken);
		org.springframework.social.facebook.api.User fbUser = fb.userOperations().getUserProfile();
		List<Account> accounts = fb.pageOperations().getAccounts();
		List<OAuthenticationResponse.Page> pages = new ArrayList<OAuthenticationResponse.Page>(); 
		
		if(accounts.size() == 0) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.facebook.nopage");
		}
		String profilePic = "https://graph.facebook.com/"+fbUser.getId()+"/picture?type=large"; 
		pages.add(new OAuthenticationResponse.Page(null,fbUser.getName(),profilePic,BigInteger.valueOf(1),false));
		for(Account account : accounts) {
			Page page = fb.fetchObject(account.getId(), Page.class, "engagement", "name", "picture.type(large)", "id");
			String url = ((LinkedHashMap<String, LinkedHashMap<String, String>>)page.getExtraData().get("picture")).get("data").get("url");
			pages.add(new OAuthenticationResponse.Page(account.getId(), page.getName(), url, BigInteger.valueOf(page.getEngagement().getCount()),true));
		}
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(fbUser.getId(), "facebook", ip);

		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setId(fbUser.getId());
			oauth.setName(fbUser.getName());
			oauth.setEmail(fbUser.getEmail());
			oauth.setMedia(mediaDao.findByMediaId("facebook"));
			oauth.setPages(pages);
			return oauth;
		} else {
			return new OAuthenticationResponse(auth.getToken());
		}
	}
}
