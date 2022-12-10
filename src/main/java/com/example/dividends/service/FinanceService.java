package com.example.dividends.service;


import com.example.dividends.model.Company;
import com.example.dividends.model.Dividend;
import com.example.dividends.model.ScrapedResult;
import com.example.dividends.persist.entity.CompanyEntity;
import com.example.dividends.persist.entity.DividendEntity;
import com.example.dividends.persist.entity.repository.CompanyRepository;
import com.example.dividends.persist.entity.repository.DividendRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@RequiredArgsConstructor
@Service
public class FinanceService {


    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity
                = this.companyRepository.findByName(companyName).orElseThrow(()
                -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 2. 회사 아이디로 배당금 조회
        List<DividendEntity> dividendEntities
                = this.dividendRepository.findByCompanyId(companyEntity.getId());


        // 3, 결과 조합 후 반환(DividendEntity -> Dividend)
        List<Dividend> dividends = dividendEntities.stream()
                                           .map(e -> Dividend.builder()
                                           .date(e.getDate())
                                           .dividend(e.getDividend())
                                           .build())
                                  .collect(Collectors.toList());

        return new ScrapedResult(Company.builder()
                    .ticker(companyEntity.getTicker())
                    .name(companyEntity.getName())
                    .build(),
                    dividends);
    }
}
