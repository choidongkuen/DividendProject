package com.example.dividends.security;


import com.example.dividends.user.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider{

    private static final long TOKEN_EXPIRE_DATE = 1000 * 60 * 60; // 1시간
    private static final String KEY_ROLES = "roles";

    private final MemberService memberService;


    @Value("{spring.jwt.secret}")
    private String secretKey;

   // 토큰 생성 메소드
    public String generateToken(String username, String roles){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles); // key - value

        var now = new Date();
        var expiredAt = new Date(now.getTime() + TOKEN_EXPIRE_DATE);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String getUserName(String token){
        return this.parseClaims(token).getSubject();
    }

    // parsing claim
    private Claims parseClaims(String token){

        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // 토큰의 유효성 검증(필터)
    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    // 인증 정보 토큰을 통해서 가져오기(필터)
    public Authentication getAuthentication(String token) {

        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
    }

}
