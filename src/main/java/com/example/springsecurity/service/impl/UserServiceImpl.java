package com.example.springsecurity.service.impl;

import com.example.springsecurity.domain.User;
import com.example.springsecurity.dto.JoinDTO;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void joinProcess(JoinDTO joinDTO) {

        // db에 이미 동일한 userId를 가진 회원이 존재하는지 검증
        boolean result = userRepository.existsByUsername(joinDTO.getUsername());

        if (result) {
            return;
        }

        User user = new User();

        user.setUsername(joinDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }
}
