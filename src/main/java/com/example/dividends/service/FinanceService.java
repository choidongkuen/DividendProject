package com.example.dividends.service;


import com.example.dividends.model.Company;
import com.example.dividends.model.Dividend;
import com.example.dividends.model.ScrapedResult;
import com.example.dividends.model.constants.CacheKey;
import com.example.dividends.persist.entity.CompanyEntity;
import com.example.dividends.persist.entity.DividendEntity;
import com.example.dividends.persist.entity.repository.CompanyRepository;
import com.example.dividends.persist.entity.repository.DividendRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
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


    // 캐싱이 필요한가?
    // 요청이 자주 들어오는가?
    // 자주 변경되는 데이터 인가?
    // 캐싱 대상이 되는 메소드에 어노테이션 설정



    // Spring boot 에서 캐시 저장 공간을 제공해주며
    // @Cacheable 어노테이션을 통해 캐시 기능을 사용할 수 있다.
    // value = 캐시 저장소 이름, key = 캐시 데이터의 key 값 (구분)

    // 그러나 DB의 데이터가 업데이트가 되면 자동으로 캐시 업데이트를 할 수 없다.
    // 부분적 수정 => @CachePut
    // 전제 삭제 => @CacheEvict

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE_KEY)
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
                                                   .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                                   .collect(Collectors.toList());

        return new ScrapedResult(new Company(companyEntity.getTicker(),companyEntity.getName())
                ,dividends);
    }
}
