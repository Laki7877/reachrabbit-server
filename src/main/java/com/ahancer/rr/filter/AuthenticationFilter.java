package com.ahancer.rr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.TokenResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncodeUtil;
import com.ahancer.rr.utils.Util;

@Component
@Order(2)
public class AuthenticationFilter implements Filter {
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest)req;
		String path = request.getRequestURI();
		
		String method = request.getMethod().toLowerCase();
		if("options".equals(method)){
			return;
		}
		else if (path.startsWith("/auth") 
				|| path.startsWith("/signup")
				|| path.startsWith("/webjar") 
				|| path.startsWith("/images") 
				|| path.startsWith("/resources") 
				|| path.startsWith("/swagger-ui") 
				|| path.startsWith("/swagger-resources") 
				|| path.startsWith("/configuration") 
				|| path.startsWith("/v2")
				|| path.startsWith("/subscription")
				|| path.startsWith("/test")) {
			chain.doFilter(req, res);
		} else {
			try {
				String token = request.getHeader(ApplicationConstant.TokenHeader);
				TokenResponse tokenObject = (TokenResponse) EncodeUtil.decodeObject(token);
				if(!request.getRemoteAddr().equals(tokenObject.getIp())){
					throw new Exception();
				}
				UserResponse userResponse = (UserResponse) cacheUtil.getCacheObject(ApplicationConstant.UserRequestCache, token);
				if(null == userResponse) {
					Long userId = tokenObject.getUserId();
					User user = authenticationService.getUserById(userId);
					if(null != user){
						userResponse = Util.getUserResponse(user);
						cacheUtil.putCacheObject(ApplicationConstant.UserRequestCache, token, userResponse);
					}
				}
				if(null != userResponse) {
					request.setAttribute(ApplicationConstant.UserRequest, userResponse);
					request.setAttribute(ApplicationConstant.TokenAttribute, token);
					chain.doFilter(req, res);
				} else {
					throw new Exception();
				}
			}catch (Exception e) {
				response.sendError(401);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
