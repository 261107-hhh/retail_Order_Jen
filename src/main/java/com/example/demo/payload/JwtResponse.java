package com.example.demo.payload;

import org.springframework.security.core.userdetails.UserDetails;

//import com.example.demo.Model.User;

public class JwtResponse {

	private String token;
	private UserDto user;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	
	
	
	
	

}
