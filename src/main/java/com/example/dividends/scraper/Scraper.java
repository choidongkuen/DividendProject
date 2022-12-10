package com.example.dividends.scraper;

import com.example.dividends.model.Company;
import com.example.dividends.model.ScrapedResult;
import org.springframework.stereotype.Component;


// 확장성을 위해 인터페이스 구현
@Component
public interface Scraper {


    Company scrapCompanyByTicker(String ticker);

    ScrapedResult getScrap(Company company);
}
