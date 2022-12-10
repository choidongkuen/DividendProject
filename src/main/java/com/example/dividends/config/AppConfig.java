package com.example.dividends.config;


import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // 스프링 빈으로 등록
    @Bean
    public Trie<String,String> trie(){
        return new PatriciaTrie<>();
    }
}
