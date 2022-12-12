package com.example.dividends.user.service.Impl;


import com.example.dividends.user.entity.MemberEntity;
import com.example.dividends.user.repository.MemberRepository;
import com.example.dividends.user.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Getter
@Setter
@RequiredArgsConstructor
@Service
@Slf4j

// MemeberEntity -> UserDetails
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.memberRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public MemberEntity register(){
        return null;
    }

    public MemberEntity authentication(){
        return null;
    }
}
