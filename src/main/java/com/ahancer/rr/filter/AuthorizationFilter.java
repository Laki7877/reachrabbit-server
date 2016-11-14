package com.ahancer.rr.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.response.UserResponse;


@Component
@Order(3)
public class AuthorizationFilter implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		com.ahancer.rr.annotations.Authorization role = handlerMethod.getMethodAnnotation(com.ahancer.rr.annotations.Authorization.class);

		if(role != null) {
			UserResponse user = (UserResponse) request.getAttribute(ApplicationConstant.UserRequest);
			if(null != user) {
				if(0 == role.value().length) {
					// Check with all roles
					Role[] roles = Role.values();
					if(ArrayUtils.contains(roles, user.getRole())) {
						return true; 
					}
				} else if(ArrayUtils.contains(role.value(), user.getRole())) {
					// found role
					return true;
				}
			}
		} else {
			// no annotation, so just pass on
			return true;
		}

		// Some authentication error by role
		response.sendError(405);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
