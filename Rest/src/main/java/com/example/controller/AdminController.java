package com.example.controller;

import com.example.model.Role;
import com.example.repository.UserRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.example.model.User;
import com.example.service.UserService;

import java.util.*;

@RestController
public class AdminController {
    private com.example.service.UserService userService;
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOAuth2AuthorizedClientService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(value = "user/userAuth")

    public ResponseEntity<User> getUserAuth() {
        Object authentication =  SecurityContextHolder
                .getContext ()
                .getAuthentication ()
                .getPrincipal ();
        if (authentication == null) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        if (authentication.toString().contains ("given_name")){
            DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication;
            String userEmail = (String) defaultOidcUser.getAttributes ().get ("email");
            try{
               User user = userService.getUserByName (userEmail);
                if(user.getRoles ().toArray ().length==2){
                    Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
                    List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
                    updatedAuthorities.add(authority);
                    updatedAuthorities.addAll(oldAuthorities);

                    SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                            SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                            updatedAuthorities)
            );
                }
                return new ResponseEntity<User> (user, HttpStatus.OK);
            }catch (NullPointerException ex){
                User user2 = new User ();
                user2.setName (userEmail);
                HashSet<Role> roles = new HashSet<Role>();
                roles.add (new Role (1L,"USER_ROLE"));
                user2.setRoles (roles);
                user2.setProfession ("seller");
                user2.setPassword (bCryptPasswordEncoder.encode ("San"));
                userService.addUser (user2);
                return new ResponseEntity<User> (user2, HttpStatus.OK);
            }
        }
        return new ResponseEntity<User> ((User) authentication, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers ();
        if (users.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (users, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser (userId);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }
        userService.addUser (user);
        return new ResponseEntity<> (user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }
        userService.addUser (user);
        return new ResponseEntity<> (user, HttpStatus.CREATED);
    }
}
