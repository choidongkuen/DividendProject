package com.example.dividends.web;


import com.example.dividends.model.Auth;
import com.example.dividends.security.TokenProvider;
import com.example.dividends.user.entity.MemberEntity;
import com.example.dividends.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j

public class AuthController {

    private final MemberService memberService;  // (authentication)
    private final TokenProvider tokenProvider;

    // 회원 가입
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request){

        MemberEntity result = memberService.register(request);
        return ResponseEntity.ok(result);
    }

    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request){
        MemberEntity member = memberService.authentication(request); // 1. 인증
        String token = this.tokenProvider.generateToken(member.getUsername(),member.getRoles()); // 2. 인증 완료시 토큰 발행

        log.info("user login -> " +request.getUsername());
        return ResponseEntity.ok(token);
    }
}
