/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.User;
import com.advantech.model.UserProfile;
import com.advantech.security.State;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String jobnumber) throws UsernameNotFoundException {
        User user = userService.findByJobnumber(jobnumber);
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }

        user.addSecurityInfo(user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
        return user;
//        return new org.springframework.security.core.userdetails.User(user.getJobnumber(), user.getPassword(),
//                user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserProfile userProfile : user.getUserProfiles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
        }
        
        System.out.println("authorities :" + authorities);
        return authorities;
    }

}
