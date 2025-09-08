package com.example.customse.service;

import com.example.customse.entity.User;
import com.example.customse.repository.UserRepositary;
import com.example.customse.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepositary userRepositary;

    public CustomUserDetailsService(UserRepositary userRepositary) {
        this.userRepositary = userRepositary;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepositary.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
        return new MyUserDetails(user);
    }
}
