package com.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dao.UserDAO;
import com.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserDAO userDAO;

    // Tải thông tin người dùng bằng email để xác thực
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // Trả về đối tượng UserDetails bao gồm email, mật khẩu và quyền hạn
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            true,
            true,
            true,
            true,
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName()))
        );
    }

    // Kiểm tra trạng thái kích hoạt của người dùng
    public boolean isActiveUser(String email) {
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getStatus() == 1; // Giả sử 1 có nghĩa là active (kích hoạt)
    }
}
