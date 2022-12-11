package com.example.dividends.scheduler;


import com.example.dividends.model.Company;
import com.example.dividends.model.ScrapedResult;
import com.example.dividends.persist.entity.CompanyEntity;
import com.example.dividends.persist.entity.DividendEntity;
import com.example.dividends.persist.entity.repository.CompanyRepository;
import com.example.dividends.persist.entity.repository.DividendRepository;
import com.example.dividends.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// 스케쥴러를 위한 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class TestScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinaceScraper;


    // 매 정각마다 업데이트
    @Scheduled(cron = "${scheduler.scrap.yahoo}")

    // 일정 주기
    public void yahooFinanceScheduling() {

        log.info("scraping scheduler is started");

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {

            ScrapedResult scrapeResult =
                    this.yahooFinaceScraper.getScrap(Company.builder()
                                                    .ticker(company.getTicker())
                                                    .name(company.getName())
                                                    .build());

            // 없는 정보를 DB에 저장
            scrapeResult.getDividendEntities().stream()
                        // Dividend -> DividendEntity
                        .map(e -> new DividendEntity(company.getId(), e))
                        // 존재하지 않은 경우 DividendEntity 저장
                        .forEach(e -> {
                            boolean exists = this.dividendRepository.existsByCompanyIdAndDate(
                                    e.getCompanyId(), e.getDate()
                            );

                            if (!exists) {
                                this.dividendRepository.save(e);
                            }
                        });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
// Thread pool 이 필요한 이유 :
// 기본적으로 스케줄러는 하나의 쓰레드로 동작
// 여러 쓰레드 생성을 위해 Thread pool 이 필요
