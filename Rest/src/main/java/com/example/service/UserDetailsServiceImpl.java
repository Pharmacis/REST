package com.example.service;

import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.model.Role;
import com.example.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetails userDetails = (UserDetails) userRepository.findUserAndRolesByName (name);
           if(userDetails == null){
             throw new UsernameNotFoundException ("UserDetailsServiceImpl return null");
           }
            else {
             return userDetails;
           }
    }
}
