package com.example.dividends;

import com.example.dividends.model.Company;
import com.example.dividends.scraper.YahooFinanceScraper;

//@SpringBootApplication
public class DividendsApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DividendsApplication.class, args);

        YahooFinanceScraper yahooFinanceScraper = new YahooFinanceScraper();
//        var result = yahooFinanceScraper.getScrap(Company
//                                .builder()
//                                .ticker("O")
//                                .build());

        var result = yahooFinanceScraper.scrapCompanyByTicker("MMM");
        System.out.println(result);

    }

}

