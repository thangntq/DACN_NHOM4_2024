package com.ManShirtShop.Authentication.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.Authentication.custom.TokenUtilities;
import com.ManShirtShop.Authentication.dto.request.LoginRequest;
import com.ManShirtShop.Authentication.dto.response.JwtResponse;
import com.ManShirtShop.Authentication.dto.response.RefreshTokenResponse;
import com.ManShirtShop.Authentication.dto.response.ResponseUser;
import com.ManShirtShop.Authentication.security.jwt.JwtUtils;
import com.ManShirtShop.Authentication.sercurity.services.AuthIm;
import com.ManShirtShop.Authentication.sercurity.services.UserDetailsImpl;

import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/")
@Tag(name = "Authentication Controller")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final AuthenticationManager authenticationManager;

    // Constructor injection to avoid circular dependency
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	TokenUtilities tokenUtilities;
	

	@PostMapping(value="/login" , consumes = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		StringBuilder roleStr = new StringBuilder();

		int i = 0;
		for (String tmp : roles) {
			if (tmp.startsWith("ROLE_")) {
				roleStr.append(tmp.substring(5, tmp.length()));
				i++;
				if (i < roles.size())
					roleStr.append(", ");
			}
		}

		String str = roleStr.toString();

		HttpHeaders responseHeaders = new HttpHeaders();
		if (!str.equals(""))
			responseHeaders.set("Roles", str);
		responseHeaders.set("Authorization", "Bearer " + jwt);
		responseHeaders.set("Access-Control-Expose-Headers", "Authorization, Roles");

		String message = "Đăng nhập thành công!";
		String action = "";	
				
		
		if (userDetails.getStatus() == 1) {
			Map<String, Object> response = new HashMap<>();
			logger.info("Tài khoản chưa được kích hoạt!");
			response.put("code", HttpServletResponse.SC_FORBIDDEN);
			response.put("data", "unactive");
			response.put("message", "Tài khoản chưa được kích hoạt! Vui lòng liên hệ với bộ phận CSKH để được hỗ trợ !");
			
			return ResponseEntity.ok(response);
		}
			

		return ResponseEntity.ok().headers(responseHeaders)
				.body(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
						userDetails.getStatus(), message, action, roles));
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		Map claims = tokenUtilities.getClaimsProperty(request);
		String new_token = jwtUtils.doGenerateRefreshToken(claims, claims.get("sub").toString());
		return ResponseEntity.ok(new RefreshTokenResponse(new_token));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
	@PostMapping("/logout")
	public ResponseEntity<Object> logOut() {
		Map<String, Object> response = new HashMap<>();
		logger.info("Đăng xuất thành công!");
		response.put("code", HttpServletResponse.SC_OK);
		response.put("data", null);
		response.put("message", "Đăng xuất thành công!");
		return ResponseEntity.ok(response);
	}
}
