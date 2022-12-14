package com.example.dividends.user.service.Impl;


import com.example.dividends.model.Auth;
import com.example.dividends.user.entity.MemberEntity;
import com.example.dividends.user.repository.MemberRepository;
import com.example.dividends.user.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Setter
@RequiredArgsConstructor
@Service
@Slf4j

// MemeberEntity -> UserDetails
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        // spring security 에서 loadUserByUsername에서 해당값으로 조회 후 저장되어 있는 유저 정보를 가져온다.
        // spring security는 해당 유저의 정보를 조회시. 세팅된 값으로 조회를 한 후 로직 처리
        return this.memberRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public MemberEntity register(Auth.SignUp signUp){

        boolean exists = this.memberRepository.existsByUserName(signUp.getUsername());

        if(exists){
            throw new RuntimeException("이미 사용 중인 아이디 입니다.");
        }

        // password 같은 민감한 정보는 암호화(encoding) 해서 DB 저장
        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));


        return memberRepository.save(signUp.toEntity());
    }

    /**
     * 입력 받은 정보와 레포지토리에 있는 정보 비교
     * 1. 식별자를 기준으로 존재여부 판단
     * 2. 존재하는 경우 인코딩된 비밀번호와 입력 받은 비밀번호 같은지 판단
     * @param member
     * @return
     */
    public MemberEntity authentication(Auth.SignIn member){

        var result = memberRepository.findByUserName(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재 하지 않는 ID 입니다."));

        // 존재하는 경우
        if(!this.passwordEncoder.matches(member.getPassword(),result.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return result;
    }


}
