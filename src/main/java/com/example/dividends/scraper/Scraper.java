package com.example.dividends.scraper;

import com.example.dividends.model.Company;
import com.example.dividends.model.ScrapedResult;


// 확장성을 위해 인터페이스 구현
public interface Scraper {


    Company scrapCompanyByTicker(String ticker);

    ScrapedResult getScrap(Company company);
}
