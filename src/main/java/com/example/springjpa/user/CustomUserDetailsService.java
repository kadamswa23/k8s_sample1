package com.example.springjpa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         MyUser user = userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
         return User.builder()
                 .username(user.getUserName())
                 .password(user.getPassword())
                 .build();

    }
}
