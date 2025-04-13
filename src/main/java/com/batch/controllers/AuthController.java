package com.batch.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batch.bos.AuthBO;
import com.batch.bos.AuthResBO;
import com.batch.configs.CustomUserDetails;
import com.batch.configs.JwtService;
import com.batch.enums.Status;

@Controller
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    
    @PostMapping(value = "/auth/generateToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AuthResBO authenticateAndGetToken(@RequestBody AuthBO authRequest) {
    	AuthResBO authRes = new AuthResBO();
    	try {
    		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (!authentication.isAuthenticated()) {
	        	authRes.setMsg("Invalid user request!");
	        	return authRes;
	        }
	        
	        CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
	        
	        authRes.setJwtToken(jwtService.generateToken(userDetails));
	        authRes.setExpiryMinuts(30);
	        authRes.setMsg("success");
	        authRes.setStatus(Status.OK);
		} catch (Exception e) {
			authRes.setMsg(e.getMessage());
        	return authRes;
		}
        return authRes;
    }
    
    @GetMapping({ "", "/" })
    public String index() {
    	return "redirect:swagger-ui.html";
    }
}
