package com.example.dividends.service;


import com.example.dividends.model.Company;
import com.example.dividends.model.ScrapedResult;
import com.example.dividends.persist.entity.CompanyEntity;
import com.example.dividends.persist.entity.repository.CompanyRepository;
import com.example.dividends.persist.entity.DividendEntity;
import com.example.dividends.persist.entity.repository.DividendRepository;
import com.example.dividends.scraper.Scraper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@Service


// Spring Bean -> 싱글 톤
public class CompanyService {

    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Trie trie;

    public Company save(String ticker) {

        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }

        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker) {

        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {

            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재할 경우, 회상의 배당급 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.getScrap(company);

        // 스크래핑 결과
        // Company -> CompanyEntity
        CompanyEntity companyEntity =
                this.companyRepository.save(new CompanyEntity(company));


        List<DividendEntity> dividendEntities =
                scrapedResult.getDividendEntities().stream()
                             .map(e -> new DividendEntity(companyEntity.getId(), e))
                             .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }

    public Page<CompanyEntity> getAllCompanies(Pageable pageable) {

        // findAll -> 어떤 문제가 있을까?
        // 가져오는 데이터가 많다면 네트워크 부하
        return this.companyRepository.findAll(pageable);
    }

    // 자동완성 기능에 키워드 추가
    public void addAutoCompletekeyWord(String keyword){
        // value 까지는 필요 없음으로, null
        this.trie.put(keyword,null);
    }


    // 자동완성 기능에 키워드 검색
    public List<String> autoComplete(String keyword){

      return (List<String>) this.trie.prefixMap(keyword).keySet()
                             .stream().collect(Collectors.toList());

    }

    // 자동완성 기능에 키워드 삭제
    public void deleteAutoCompleteKeyword(String keyword){

        this.trie.remove(keyword);
    }

    // 자동완성 기능
    // trie 대신 like 기능을 사용
    public List<String> getCompanyNamesByKeyword(String keyword){
        Pageable limit = PageRequest.of(0,10);

        Page<CompanyEntity> result
                = this.companyRepository.findByNameStartingWithIgnoreCase(keyword,limit);

        return result.stream()
                     .map(e -> e.getName())
                     .collect(Collectors.toList());
    }

}