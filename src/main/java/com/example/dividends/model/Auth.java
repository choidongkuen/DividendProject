package com.example.dividends.model;

import com.example.dividends.user.entity.MemberEntity;
import lombok.Data;

import java.util.List;

public class Auth {


    // 로그인
    @Data
    public static class SignIn{

        private String username;

        private String password;



    }


    // 회원 가입
    @Data
    public static class SignUp{


        private String username;

        private String password;

        private String roles;


        // SignUp -> MemberEntity
        public MemberEntity toEntity(){

            return MemberEntity.builder().
                               userName(this.username)
                               .password(this.password)
                               .roles(this.roles)
                               .build();

        }

    }
}
