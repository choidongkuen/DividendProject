package com.example.dividends.web;

import com.example.dividends.model.Company;
import com.example.dividends.model.constants.CacheKey;
import com.example.dividends.entity.CompanyEntity;
import com.example.dividends.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final CacheManager redisCacheManager;

    // 자동완성 검색
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyWord){

        var result = this.companyService.getCompanyNamesByKeyword(keyWord);
        return ResponseEntity.ok(result);

    }

    // 회사 추가
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){

            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        this.companyService.addAutoCompletekeyWord(company.getName()); // 트라이에 저장
        return ResponseEntity.ok(company);

    }

    // 회사 삭제
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker){

        String companyName = this.companyService.deleteCompany(ticker);
        return ResponseEntity.ok(companyName);

    }


    // 회사 검색
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companies = this.companyService.getAllCompanies(pageable);
        return ResponseEntity.ok(companies);
    }

    // 캐시 삭제
    public void clearFinanceCache(String companyName){

        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE_KEY).evict(companyName);

    }

}
