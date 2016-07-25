package com.ahancer.rr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahancer.rr.request.AuthenticationRequest;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.utils.TokenUtils;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenUtils tokenUtils;


	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

		
		
//		String password = authenticationRequest.getPassword();
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String hashedPassword = passwordEncoder.encode(password);
//		authenticationRequest.setPassword(hashedPassword);
		
		// Perform the authentication
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),
						authenticationRequest.getPassword()
						)
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		String token = this.tokenUtils.generateToken(userDetails, device);

		// Return the token
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}


}
