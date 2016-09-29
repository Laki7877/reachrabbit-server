package com.ahancer.rr.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.InstagramProfileResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class InstagramService {
	@Value("${instagram.appKey}")
	private String appKey;
	@Value("${instagram.appSecret}")
	private String appSecret;
	
	//TODO remove hardcoded
	private String adminAccessToken ="3776064946.c428876.6dba7fbf1f7c424bb9fa5bec9e8633e9";
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private MediaDao mediaDao;
	
	public String getAccessToken(String authorizationCode, String redirectUri) {
		org.jinstagram.auth.oauth.InstagramService service = new InstagramAuthService()
				.apiKey(appKey)
				.apiSecret(appSecret)
				.callback(redirectUri)
				.build();
		return service.getAccessToken(new Verifier(authorizationCode)).getToken();
	}
	public String getAdminAccessToken() {
		//TODO: get from storage if null
		return adminAccessToken;
	}
	public void refreshAdminToken(String accessToken) {
		//TODO: save to storage
		adminAccessToken = accessToken;
	}
	public Boolean checkAdminToken() throws InstagramException {
		if(getInstance(getAdminAccessToken()).getCurrentUserInfo().getData() != null) {
			return true;
		} else {
			return false;
		}
	}
	public Post getPostInfo(String postId) throws Exception {
		String urlString = "https://www.instagram.com/p/"+ postId +"/?__a=1";
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("accept-encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("x-requested-with", "XMLHttpRequest");
		con.setRequestProperty("accept-language", "en-US,en;q=0.8");
		con.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("authority", "www.instagram.com");
		InputStream inStream = new GZIPInputStream(con.getInputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JsonParser parser = new JsonParser();
		JsonObject media = parser.parse(response.toString()).getAsJsonObject().getAsJsonObject("media");
		Long commentCount = media.getAsJsonObject("comments").get("count").getAsLong();
		Long likeCount = media.getAsJsonObject("likes").get("count").getAsLong();
		Post postModel = new Post();
		postModel.setViewCount(0L);
		postModel.setCommentCount(commentCount);
		postModel.setLikeCount(likeCount);
		postModel.setShareCount(0L);
		postModel.setSocialPostId(postId);
		return postModel;
	}
	public InstagramProfileResponse getProfile(String socialId) throws InstagramException {
		Instagram instagram = getInstance(getAdminAccessToken());
		UserInfoData userInfo = instagram.getUserInfo(socialId).getData();
		List<MediaFeedData> mediaFeeds = instagram.getRecentMediaFeed(socialId, 20, null, null, null, null).getData();
		
		InstagramProfileResponse resp = new InstagramProfileResponse();

		resp.setUsername(userInfo.getUsername());
		resp.setName(userInfo.getFullName());
		resp.setFollowers(BigInteger.valueOf(userInfo.getCounts().getFollowedBy()));
		resp.setTotalPosts(BigInteger.valueOf(userInfo.getCounts().getMedia()));
		
		BigInteger averageLikes = BigInteger.ZERO;
		BigInteger averageComments = BigInteger.ZERO;
		
		List<InstagramProfileResponse.Post> posts = new ArrayList<>();
		
		for(MediaFeedData data : mediaFeeds) {
			InstagramProfileResponse.Post post = new InstagramProfileResponse.Post();
			post.setType(data.getType());
			if(post.getType().equals("image")) {
				post.setImage(data.getImages().getStandardResolution().getImageUrl());
				post.setWidth(data.getImages().getStandardResolution().getImageWidth());
				post.setHeight(data.getImages().getStandardResolution().getImageHeight());
			} else if(post.getType().equals("video")) {
				post.setVideo(data.getVideos().getStandardResolution().getUrl());
				post.setWidth(data.getVideos().getStandardResolution().getWidth());
				post.setHeight(data.getVideos().getStandardResolution().getHeight());
			}
			post.setComments(BigInteger.valueOf(data.getComments().getCount()));
			post.setLikes(BigInteger.valueOf(data.getLikes().getCount()));
			
			averageLikes = averageLikes.add(post.getLikes());
			averageComments = averageComments.add(post.getComments());
			
			posts.add(post);
		}
		
		averageLikes = averageLikes.divide(BigInteger.valueOf(posts.size()));
		averageComments = averageComments.divide(BigInteger.valueOf(posts.size()));
		
		resp.setAverageComments(averageComments);
		resp.setAverageLikes(averageLikes);
		resp.setPosts(posts);
		
		return resp;
	}
	public Instagram getInstance(String accessToken) {
		return new Instagram(new Token(accessToken, appSecret));
	}
	public OAuthenticationResponse authenticate(String accessToken, String ip) throws Exception {
		Instagram instagram = getInstance(accessToken);
		Instagram instagram2 = getInstance(getAdminAccessToken());
		UserInfoData userInfo = instagram.getCurrentUserInfo().getData();
		
		try{
			if(instagram2.getUserInfo(userInfo.getId()).getData() == null) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.instagram.notpublic");
			}
		} catch(Exception e) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.media.instagram.notpublic");
		}
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(userInfo.getId(), "instagram", ip);
		List<OAuthenticationResponse.Page> pages = new ArrayList<OAuthenticationResponse.Page>();
		pages.add(new OAuthenticationResponse.Page(userInfo.getId(), userInfo.getFullName(), userInfo.getProfilePicture(), BigInteger.valueOf(userInfo.getCounts().getFollowedBy())));
		
		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setName(userInfo.getFullName());
			oauth.setId(userInfo.getId());
			oauth.setMedia(mediaDao.findByMediaId("instagram"));
			oauth.setProfilePicture(userInfo.getProfilePicture());
			oauth.setPages(pages);
			return oauth;
		} else {
			return new OAuthenticationResponse(auth.getToken());
		}
	}
}
