package com.batch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.batch.configs.CustomUserDetails;
import com.batch.entities.User;
import com.batch.repo.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
    private UserRepository userRepository;
	
	 @Autowired
	 private PasswordEncoder encoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Sorry! you entered invalid username and password !!!");
		}
		return new CustomUserDetails(user);
    }
    
    
    public String createDefaultUser() {
    	if(userRepository.findByUsername("user") == null) {
    		User userInfo = new User();
    		userInfo.setUsername("user");
        	userInfo.setName("Test User");
        	userInfo.setRoles("USER");
            userInfo.setPassword(encoder.encode("Pass@123"));
            userRepository.save(userInfo);
    	}
        return "User Added Successfully";
    }
}
