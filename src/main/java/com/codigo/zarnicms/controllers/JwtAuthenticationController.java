package com.codigo.zarnicms.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codigo.zarnicms.common.CustomException;
import com.codigo.zarnicms.config.JwtTokenUtil;
import com.codigo.zarnicms.data.request.JwtRequest;
import com.codigo.zarnicms.data.response.JwtResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@GetMapping(value = "/test")
	public String test() throws Exception {
		return "Welcome";
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@GetMapping(value = "/refreshToken")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {

		String token = request.getHeader("Authorization");
		token = token.replaceAll("Bearer ", "");
	    String user = jwtTokenUtil.getUsername(token);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(user);

		final String newToken = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(newToken));
	}

	
	private void authenticate(String username, String password) throws Exception {
		try {
			Objects.requireNonNull(username);
			Objects.requireNonNull(password);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new CustomException(HttpStatus.FORBIDDEN, "USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
		} catch (NullPointerException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
		}
		
	}
}