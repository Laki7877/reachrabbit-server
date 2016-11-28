package com.ahancer.rr.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.PostDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.request.PostRequest;
import com.ahancer.rr.response.UpdatePostResponse;
import com.ahancer.rr.services.FacebookService;
import com.ahancer.rr.services.InstagramService;
import com.ahancer.rr.services.PostService;
import com.ahancer.rr.services.YoutubeService;

@Component
@Transactional(rollbackFor=Exception.class)
public class PostServiceImpl implements PostService {
	
	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
	
	@Autowired
	private PostDao postDao;
	@Autowired
	private ProposalDao proposalDao;
	@Autowired
	private FacebookService facebookService;
	@Autowired
	private InstagramService instagramService;
	@Autowired
	private YoutubeService youtubeService;
	@Value("${app.dashboard.history.days}")
	private Integer historyDays;

	public List<UpdatePostResponse> getUpdatePost(Date minDate) throws Exception{
		return postDao.findUpdatePost(minDate);
	}
	
	public Post createNewPostBySys(Post post) throws Exception {
		return postDao.save(post);
	}
	
	public Post createPostByAdmin(Long proposalId, PostRequest request) throws Exception {
		if(StringUtils.isEmpty(request.getUrl())) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
		}
		Proposal proposal = proposalDao.findOne(proposalId);
		if(null == proposal) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.proposal.invalid");
		}
		Post post = new Post();
		post.setIsPersonalAccountPost(true);
		post.setProposalId(proposalId);
		post.setMediaId(request.getMedia().getMediaId());
		@SuppressWarnings("unused")
		Post tmpPost = null;
		String[] splitUrl = null;
		Long count = 0L;
		switch(request.getMedia().getMediaId()){
		case "facebook":
			splitUrl = request.getUrl().split("/");
			String pageId = StringUtils.EMPTY;
			if(splitUrl.length == 1 && splitUrl[0].contains("_")) {
				post.setSocialPostId(splitUrl[0]);
			} else {
				boolean isFbRegister = false;
				for(InfluencerMedia media : proposal.getInfluencer().getInfluencerMedias()){
					if(media.getMedia().getMediaId().equals("facebook")){
						isFbRegister = true;
						pageId = media.getPageId();
						break;
					}
				}
				if(StringUtils.isEmpty(pageId)){
					if(!isFbRegister){
						throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.facebook.pageid.requre");
					}
					else {
						
						//String url = "https://www.facebook.com/KieChioz/posts/1210717579022931";

						URL obj = new URL(request.getUrl());
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();

						// optional default is GET
						con.setRequestMethod("GET");

						//add request header
						//con.setRequestProperty("User-Agent", USER_AGENT);

						int responseCode = con.getResponseCode();
						if(responseCode != 200) {
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						con.disconnect();
						if(!response.toString().contains("likecount:") && !response.toString().contains("commentcount:")){
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
					}
				}
				else if (splitUrl.length == 4) {
					String[] splitParam = splitUrl[3].split("\\?");
					if(splitParam.length < 2){
						throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
					}
					List<NameValuePair> args= URLEncodedUtils.parse(splitParam[1], Charset.defaultCharset());
					boolean isMatch = false;
					for (NameValuePair arg:args) {
			            if ("story_fbid".equals(arg.getName())) {
			            	isMatch = true;
			                post.setSocialPostId(pageId + "_" + arg.getValue());
			            }
					}
					if(!isMatch){
						throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
					}
				} else if(splitUrl.length < 5) {
					throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
				} else {
					switch(splitUrl[4]){
					case "photos":
						if(splitUrl.length < 7) {
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
						post.setSocialPostId(pageId + "_" + splitUrl[6]);
						break;
					case "videos":
						if(splitUrl.length < 6) {
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
						post.setSocialPostId(pageId + "_" + splitUrl[5]);
						break;
					case "posts":
						if(splitUrl.length < 6) {
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
						post.setSocialPostId(pageId + "_" + splitUrl[5]);
						break;
					default:
						throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
					}
				}
			}
			if(!StringUtils.isEmpty(post.getSocialPostId())){
				count = postDao.countByMediaIdAndSocialPostId(post.getProposalId(), post.getMediaId(), post.getSocialPostId());
				if(count > 0L) {
					throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.proposal.duplicate");
				}
				tmpPost = facebookService.getPostInfo(post.getSocialPostId());
				post.setIsPersonalAccountPost(false);
			}
			break;
		case "instagram":
			splitUrl = request.getUrl().split("/");
			if(splitUrl.length < 5) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
			}
			post.setSocialPostId(splitUrl[4]);
			count = postDao.countByMediaIdAndSocialPostId(post.getProposalId(), post.getMediaId(), post.getSocialPostId());
			if(count > 0L) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.proposal.duplicate");
			}
			tmpPost = instagramService.getPostInfo(post.getSocialPostId());
			break;
		case "google":
			splitUrl = request.getUrl().split("/");
			if(splitUrl.length < 4) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
			}
			splitUrl = splitUrl[3].split("=");
			if(splitUrl.length < 2){
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
			}
			post.setSocialPostId(splitUrl[1]);
			count = postDao.countByMediaIdAndSocialPostId(post.getProposalId(), post.getMediaId(), post.getSocialPostId());
			if(count > 0L) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.proposal.duplicate");
			}
			tmpPost = youtubeService.getPostInfo(post.getSocialPostId());
			break;
		default:
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.invalid.media");
		}
		post.setCommentCount(0L);
		post.setLikeCount(0L);
		post.setShareCount(0L);
		post.setViewCount(0L);
		post.setUrl(request.getUrl());
		post.setDataDate(new Date());
		post = postDao.save(post);
		proposal.setHasPost(true);
		proposal = proposalDao.save(proposal);
		return post;
	}
	
	public void createPostSchedule(Date dataDate) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -historyDays);
		List<UpdatePostResponse> postList = getUpdatePost(cal.getTime());
		for(UpdatePostResponse postModel : postList) {
			Post post = null;
			try {
				switch (postModel.getMediaId()){
				case "facebook":
					if(!postModel.getIsPersonalAccountPost()){
						post = facebookService.getPostInfo(postModel.getSocialPostId());
					} else {
						URL obj = new URL(postModel.getUrl());
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();
						con.setRequestMethod("GET");
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						con.disconnect();
						if(!response.toString().contains("likecount:") && !response.toString().contains("commentcount:")){
							throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.url.invalid");
						}
						post = new Post();
						post.setViewCount(0L);
						post.setCommentCount(Long.parseLong(response.toString().split("commentcount:")[1].split(",")[0]));
						post.setLikeCount(Long.parseLong(response.toString().split("likecount:")[1].split(",")[0]));
						post.setShareCount(Long.parseLong(response.toString().split("sharecount:")[1].split(",")[0]));
						post.setSocialPostId(null);
					}
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("facebook");
					break;
				case "instagram":
					post = instagramService.getPostInfo(postModel.getSocialPostId());
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("instagram");
					break;
				case "google":
					post = youtubeService.getPostInfo(postModel.getSocialPostId());
					post.setProposalId(postModel.getProposalId());
					post.setMediaId("google");
					break;
				default:
					break;
				}
				if(null != post) {
					post.setUrl(postModel.getUrl());
					post.setDataDate(dataDate);
					post.setIsPersonalAccountPost(postModel.getIsPersonalAccountPost());
					createNewPostBySys(post);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MDC.put("User", "system");
				MDC.put("ProposalId", postModel.getProposalId().toString());
				MDC.put("MediaId", postModel.getMediaId());
				logger.error("Exception caught",e);
			}
		}
	}
	
	public List<UpdatePostResponse> getListPost(Long proposalId) throws Exception {
		return postDao.findPostByProposalId(proposalId);
	}
	
	public int deletePost(Long proposalId, PostRequest request) throws Exception {
		Long proposalPostCount = postDao.countByProposalId(proposalId);
		int deletedItem = postDao.deletePost(proposalId, request.getMedia().getMediaId(), request.getSocialPostId());
		if((proposalPostCount - deletedItem) <= 0){
			proposalDao.updateHasPost(false, proposalId);
		}
		return deletedItem;
	}
	
	
}
