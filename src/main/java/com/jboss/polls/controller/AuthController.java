package com.jboss.polls.controller;

import com.jboss.polls.model.User;
import com.jboss.polls.payload.ApiResponse;
import com.jboss.polls.payload.JwtRequest;
import com.jboss.polls.payload.JwtResponse;
import com.jboss.polls.repository.RoleRepository;
import com.jboss.polls.repository.UserRepository;
import com.jboss.polls.security.JwtToken;
import com.jboss.polls.service.MyUserDetailsService;
import com.jboss.polls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // login new user
    @PostMapping("/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest){
            try{
                authenticate(authenticationRequest.getUsernameOrEmail(), authenticationRequest.getPassword());
                final UserDetails userDetails = myUserDetailsService

                        .loadUserByUsername(authenticationRequest.getUsernameOrEmail());

                Optional<User> userData = userRepository.findByUsername(authenticationRequest.getUsernameOrEmail());
                String token = jwtToken.generateToken(userDetails);

                return ResponseEntity.ok(new JwtResponse(token, userData.get().getId(), userData.get().getName(), userData.get().getUsername(), userData.get().getEmail(), userData.get().getPassword()));
            }catch(Exception ex){
                return new ResponseEntity<>(new ApiResponse(true, ex.getMessage(), authenticationRequest), HttpStatus.OK);
            }
    }

    private void authenticate(String usernameOrEmail, final String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
        } catch (final DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (final BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user){
        String password = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        return userService.createUserAccount(user);
    }
}

