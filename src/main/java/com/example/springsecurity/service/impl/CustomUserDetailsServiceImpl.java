package com.example.springsecurity.service.impl;

import com.example.springsecurity.domain.User;
import com.example.springsecurity.dto.CustomUserDetails;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userRepository.findByUsername(username);

        if (byUsername != null) {
            return new CustomUserDetails(byUsername);
        }
        return null;
    }
}
