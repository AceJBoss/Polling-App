package com.jboss.polls.service;

import com.jboss.polls.model.User;
import com.jboss.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService{
	 @Autowired
	 private UserRepository userRepository;

	 @Override
	 public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
	     User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(()->
			 new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		 List<GrantedAuthority> authorities = user.getRoles().stream()
				 .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		 return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				 authorities);
	}
}
