package com.example.dividends.user.service;

import com.example.dividends.model.Auth;
import com.example.dividends.user.entity.MemberEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {


    public UserDetails loadUserByUsername(String username);

    public MemberEntity register(Auth.SignUp signUp);

    public MemberEntity authentication(Auth.SignIn signIn);


}
