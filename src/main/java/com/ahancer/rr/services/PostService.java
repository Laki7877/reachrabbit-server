package com.ahancer.rr.services;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.PostDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.request.PostRequest;
import com.ahancer.rr.response.UpdatePostResponse;

@Service
@Transactional(rollbackFor=Exception.class)
public class PostService {
	
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
	
	
	public List<UpdatePostResponse> getUpdatePost(Date minDate){
		return postDao.findUpdatePost(minDate);
	}
	
	public Post createNewPostBySys(Post post){
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
		post.setProposalId(proposalId);
		post.setMediaId(request.getMedia().getMediaId());
		@SuppressWarnings("unused")
		Post tmpPost = null;
		String[] splitUrl = null;
		Long count = 0L;
		switch(request.getMedia().getMediaId()){
		case "facebook":
			splitUrl = request.getUrl().split("/");
			if(splitUrl.length == 1 && splitUrl[0].contains("_")) {
				post.setSocialPostId(splitUrl[0]);
			} else {
				String pageId = StringUtils.EMPTY;
				for(InfluencerMedia media : proposal.getInfluencer().getInfluencerMedias()){
					if(media.getMedia().getMediaId().equals("facebook")){
						pageId = media.getPageId();
						break;
					}
				}
				if(StringUtils.isEmpty(pageId)){
					throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.facebook.pageid.requre");
				}
				if (splitUrl.length == 4) {
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
			count = postDao.countByMediaIdAndSocialPostId(post.getProposalId(), post.getMediaId(), post.getSocialPostId());
			if(count > 0L) {
				throw new ResponseException(HttpStatus.BAD_REQUEST, "error.post.proposal.duplicate");
			}
			tmpPost = facebookService.getPostInfo(post.getSocialPostId());
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
//		post.setCommentCount(tmpPost.getCommentCount());
//		post.setLikeCount(tmpPost.getLikeCount());
//		post.setShareCount(tmpPost.getShareCount());
//		post.setViewCount(tmpPost.getViewCount());
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
	
	public List<UpdatePostResponse> getListPost(Long proposalId){
		return postDao.findPostByProposalId(proposalId);
	}
	
	public int deletePost(Long proposalId, PostRequest request) {
		Long proposalPostCount = postDao.countByProposalId(proposalId);
		int deletedItem = postDao.deletePost(proposalId, request.getMedia().getMediaId(), request.getSocialPostId());
		if((proposalPostCount - deletedItem) <= 0){
			proposalDao.updateHasPost(false, proposalId);
		}
		return deletedItem;
	}
	
	
}
