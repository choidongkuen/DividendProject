package com.example.dividends.scraper;

import com.example.dividends.model.Company;
import com.example.dividends.model.Dividend;
import com.example.dividends.model.ScrapedResult;
import com.example.dividends.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    // 멤버 변수로 설정시 : 1. 유지 보수 good , 2. 메모리 관점
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;


    // 회사 정보를 가지고 배당금 정보 스크래핑하는 메서드
    @Override
    public ScrapedResult getScrap(Company company) {

        
        // ScrapResult 
        // Company 
        // List<Dividend>
        ScrapedResult scrapResult = new ScrapedResult();
        

        try {

            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTICS_URL, company.getName(), START_TIME, now);

            Connection connection = Jsoup.connect(STATISTICS_URL);
            Document document = connection.get();

            Elements parsedDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element table = parsedDivs.get(0); // table 전체

            Element tbody = table.children().get(1); // thead - tbody - tfoot
            List<Dividend> dividends = new ArrayList<>(); // 모든 배당금(Dividend) 정보가 들어갈 리스트

            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividends")) {
                    continue;
                }

                String[] splits = txt.split(" ");

                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", " "));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(new Dividend(LocalDateTime.of(year,month,day,0,0),
                        dividend));

            }
            
            scrapResult.setDividendEntities(dividends);

        } catch (IOException e) {
            // TODO
            e.printStackTrace();

        }
        
        return scrapResult;
    }

    // ticker 정보를 가지고 Company 정보 찾는 메소드
    @Override
    public Company scrapCompanyByTicker(String ticker) {

        String url = String.format(SUMMARY_URL, ticker, ticker);
        
        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[1].trim();

            return new Company(ticker,title);

        } catch (IOException e) {
            throw new RuntimeException("Error!");
        }
    }
}
