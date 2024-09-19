package com.ManShirtShop.common.mapperUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFormat {

	public ResponseEntity<Object> response(Integer code, Object data, String message){
		Map<String,Object> response = new HashMap<>();
		response.put("code", code);
		response.put("data", data);
		response.put("message", message);
		return ResponseEntity.ok(response); 
	}
	
}
