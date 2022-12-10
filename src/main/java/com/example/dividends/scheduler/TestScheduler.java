package com.example.dividends.scheduler;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 스케쥴러를 위한 클래스
@Component
public class TestScheduler {

    @Scheduled(cron = "0/5 * * * * *")
    public void test(){
        System.out.println("now -> " +  System.currentTimeMillis());

    }

    // 일정 주기
    public void yahooFinanceScheduling(){

        // 저장된 회사 목록을 조회


        // 회사마다 배당금 정보를 새로 스크래핑


        // 없는 정보를 DB에 저장

    }
}
