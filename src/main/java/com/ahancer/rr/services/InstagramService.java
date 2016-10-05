package com.ahancer.rr.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.daos.InfluencerMediaDao;
import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.InfluencerMediaId;
import com.ahancer.rr.models.Media;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.request.InstagramAuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.InstagramAuthenticationResponse;
import com.ahancer.rr.response.InstagramProfileResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class InstagramService {
	@Value("${instagram.appKey}")
	private String appKey;
	@Value("${instagram.appSecret}")
	private String appSecret;
	@Autowired
	private InfluencerMediaDao influencerMediaDao;
	@Autowired
	private CacheUtil cacheUtil;

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
		con.disconnect();
		JsonParser parser = new JsonParser();
		JsonObject media = parser.parse(response.toString()).getAsJsonObject().getAsJsonObject("media");
		Long commentCount = media.getAsJsonObject("comments").get("count").getAsLong();
		Long likeCount = media.getAsJsonObject("likes").get("count").getAsLong();
		Long videoViews = 0L;
		if(media.has("video_views")){
			videoViews = media.get("video_views").getAsLong();
		}
		Post postModel = new Post();
		postModel.setViewCount(videoViews);
		postModel.setCommentCount(commentCount);
		postModel.setLikeCount(likeCount);
		postModel.setShareCount(0L);
		postModel.setSocialPostId(postId);
		return postModel;
	}
	public InstagramProfileResponse getProfile(String socialId) throws Exception {
		String urlString = "https://www.instagram.com/"+ socialId +"/?__a=1";
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseProfile = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			responseProfile.append(inputLine);
		}
		in.close();
		con.disconnect();
		JsonParser parser = new JsonParser();
		JsonObject user = parser.parse(responseProfile.toString()).getAsJsonObject().getAsJsonObject("user");
		JsonObject media = user.getAsJsonObject("media");
		JsonArray nodes = media.getAsJsonArray("nodes");
		List<InstagramProfileResponse.Post> posts = new ArrayList<>();
		BigInteger averageLikes = BigInteger.ZERO;
		BigInteger averageComments = BigInteger.ZERO;
		for(JsonElement element : nodes){
			InstagramProfileResponse.Post post = new InstagramProfileResponse.Post();
			if(element.getAsJsonObject().get("is_video").getAsBoolean()){
				post.setType("video");
				String postId = element.getAsJsonObject().get("code").getAsString();
				String vidoPostUrlString = "https://www.instagram.com/p/"+ postId +"/?__a=1";
				URL videoPostUrl = new URL(vidoPostUrlString);
				HttpsURLConnection videoPostCon = (HttpsURLConnection) videoPostUrl.openConnection();
				videoPostCon.setRequestMethod("GET");
				BufferedReader videoPostInputStream = new BufferedReader(new InputStreamReader(videoPostCon.getInputStream()));
				String inputLinePost;
				StringBuffer responseVideoPost = new StringBuffer();
				while ((inputLinePost = videoPostInputStream.readLine()) != null) {
					responseVideoPost.append(inputLinePost);
				}
				videoPostInputStream.close();
				videoPostCon.disconnect();
				JsonObject mediaVideoPost = parser.parse(responseVideoPost.toString()).getAsJsonObject().getAsJsonObject("media");
				post.setVideo(mediaVideoPost.get("video_url").getAsString());
				JsonObject videoDimension = mediaVideoPost.getAsJsonObject("dimensions");
				post.setWidth(videoDimension.get("width").getAsInt());
				post.setHeight(videoDimension.get("height").getAsInt());
				post.setVideoViews(element.getAsJsonObject().get("video_views").getAsBigInteger());
			} else {
				post.setType("image");
				post.setImage(element.getAsJsonObject().get("display_src").getAsString());
				JsonObject dimension = element.getAsJsonObject().getAsJsonObject("dimensions");
				post.setWidth(dimension.get("width").getAsInt());
				post.setHeight(dimension.get("height").getAsInt());
			}
			if(element.getAsJsonObject().has("caption")) {
				post.setCaption(element.getAsJsonObject().get("caption").getAsString());
			}
			
			post.setComments(element.getAsJsonObject().get("comments").getAsJsonObject().get("count").getAsBigInteger());
			post.setLikes(element.getAsJsonObject().get("likes").getAsJsonObject().get("count").getAsBigInteger());

			averageLikes = averageLikes.add(post.getLikes());
			averageComments = averageComments.add(post.getComments());

			posts.add(post);
		}


		InstagramProfileResponse response = new InstagramProfileResponse();
		response.setUsername(user.get("username").getAsString());
		response.setName(user.get("full_name").getAsString());
		response.setFollowers(user.get("followed_by").getAsJsonObject().get("count").getAsBigInteger());
		response.setTotalPosts(media.get("count").getAsBigInteger());
		response.setAverageComments(averageComments);
		response.setAverageLikes(averageLikes);
		response.setPosts(posts);
		return response;





		//		Instagram instagram = getInstance(getAdminAccessToken());
		//		UserInfoData userInfo = instagram.getUserInfo(socialId).getData();
		//		List<MediaFeedData> mediaFeeds = instagram.getRecentMediaFeed(socialId, 20, null, null, null, null).getData();
		//		
		//		InstagramProfileResponse resp = new InstagramProfileResponse();
		//
		//		resp.setUsername(userInfo.getUsername());
		//		resp.setName(userInfo.getFullName());
		//		resp.setFollowers(BigInteger.valueOf(userInfo.getCounts().getFollowedBy()));
		//		resp.setTotalPosts(BigInteger.valueOf(userInfo.getCounts().getMedia()));
		//		
		//		BigInteger averageLikes = BigInteger.ZERO;
		//		BigInteger averageComments = BigInteger.ZERO;
		//		
		//		List<InstagramProfileResponse.Post> posts = new ArrayList<>();
		//		
		//		for(MediaFeedData data : mediaFeeds) {
		//			InstagramProfileResponse.Post post = new InstagramProfileResponse.Post();
		//			post.setType(data.getType());
		//			if(post.getType().equals("image")) {
		//				post.setImage(data.getImages().getStandardResolution().getImageUrl());
		//				post.setWidth(data.getImages().getStandardResolution().getImageWidth());
		//				post.setHeight(data.getImages().getStandardResolution().getImageHeight());
		//			} else if(post.getType().equals("video")) {
		//				post.setVideo(data.getVideos().getStandardResolution().getUrl());
		//				post.setWidth(data.getVideos().getStandardResolution().getWidth());
		//				post.setHeight(data.getVideos().getStandardResolution().getHeight());
		//			}
		//			post.setComments(BigInteger.valueOf(data.getComments().getCount()));
		//			post.setLikes(BigInteger.valueOf(data.getLikes().getCount()));
		//			
		//			averageLikes = averageLikes.add(post.getLikes());
		//			averageComments = averageComments.add(post.getComments());
		//			
		//			posts.add(post);
		//		}
		//		
		//		averageLikes = averageLikes.divide(BigInteger.valueOf(posts.size()));
		//		averageComments = averageComments.divide(BigInteger.valueOf(posts.size()));
		//		
		//		resp.setAverageComments(averageComments);
		//		resp.setAverageLikes(averageLikes);
		//		resp.setPosts(posts);
		//		
		//		return resp;
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
	@Transactional(rollbackFor=Exception.class)
	public UserResponse validateUser(UserResponse user,String token,InstagramAuthenticationRequest request) throws Exception {
		String urlString = "https://www.instagram.com/accounts/login/ajax/";
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("origin", "https://www.instagram.com");
		con.setRequestProperty("accept-encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("accept-language", "en-US,en;q=0.8");
		con.setRequestProperty("x-requested-with", "XMLHttpRequest");
		con.setRequestProperty("cookie", "mid=V_NhzQAEAAE8lekQlcNn8Cnd-riN; ig_pr=1; ig_vw=1280; csrftoken=VqQPIQJSstnUg1ytGgmg3XWp5b3P393U");
		con.setRequestProperty("x-csrftoken", "VqQPIQJSstnUg1ytGgmg3XWp5b3P393U");
		con.setRequestProperty("pragma", "no-cache");
		con.setRequestProperty("x-instagram-ajax", "1");
		con.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
		con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("cache-control", "no-cache");
		con.setRequestProperty("authority", "www.instagram.com");
		con.setRequestProperty("referer", "https://www.instagram.com/");
		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
		String data = "username=" + request.getUsername() + "&password=" + request.getPassword();
		writer.write(data);
		writer.flush();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		writer.close();
		in.close();
		con.disconnect();

		ObjectMapper mapper = new ObjectMapper();
		InstagramAuthenticationResponse obj = mapper.readValue(response.toString(), InstagramAuthenticationResponse.class);
		if(!obj.getAuthenticated()) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.influencer.instagram.validation.invalid");
		}
		
		
		InstagramProfileResponse profile = getProfile(obj.getUser());
		
		Long influencerId = user.getInfluencer().getInfluencerId();
		InfluencerMedia influencerMedia = new InfluencerMedia();
		Media media = mediaDao.findOne("instagram");
		influencerMedia.setMedia(media);
		influencerMedia.setSocialId(obj.getUser());
		InfluencerMediaId influencerMediaId = new InfluencerMediaId();
		influencerMediaId.setInfluencerId(influencerId);
		influencerMediaId.setMediaId(media.getMediaId());
		influencerMedia.setInfluencerMediaId(influencerMediaId);
		influencerMediaDao.insertInfluencerMedia(influencerId ,media.getMediaId() ,profile.getFollowers().longValue(), null , influencerMedia.getSocialId());
		user.getInfluencer().getInfluencerMedias().add(influencerMedia);
		cacheUtil.updateCacheObject(ApplicationConstant.UserRequestCache, token, user);
		return user;
	}

}
