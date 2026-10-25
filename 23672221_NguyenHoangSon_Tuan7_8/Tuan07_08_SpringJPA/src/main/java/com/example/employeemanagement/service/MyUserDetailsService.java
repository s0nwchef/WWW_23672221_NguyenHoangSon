package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.UserAccount;
import com.example.employeemanagement.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepository;

    public MyUserDetailsService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy: " + username));
        return User.withUsername(account.getUsername())
                .password(account.getPassword())   // mật khẩu thô như đề (không BCrypt)
                .roles(account.getRole())          // "ADMIN" -> ROLE_ADMIN
                .build();
    }
}
