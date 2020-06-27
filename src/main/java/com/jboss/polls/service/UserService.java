package com.jboss.polls.service;

import com.jboss.polls.exception.AppException;
import com.jboss.polls.model.Role;
import com.jboss.polls.model.RoleName;
import com.jboss.polls.model.User;
import com.jboss.polls.payload.ApiResponse;
import com.jboss.polls.repository.RoleRepository;
import com.jboss.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<?> createUserAccount(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            return new ResponseEntity(new ApiResponse(true, "Username is already taken!", user.getUsername()), HttpStatus.BAD_REQUEST);
        }if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity(new ApiResponse(true, "Email is already taken!", user.getEmail()), HttpStatus.BAD_REQUEST);
        }else{
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
            user.setRoles(Collections.singleton(userRole));

            User result = userRepository.save(user);

//            URI location = ServletUriComponentsBuilder
//                    .fromCurrentContextPath().path("/api/users/{username}")
//                    .buildAndExpand(result.getUsername()).toUri();
            return new ResponseEntity(new ApiResponse(false,"User registered succesfully",result), HttpStatus.OK);

            //return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
        }
    }
}
