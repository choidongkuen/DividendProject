package com.example.dividends.web;

import com.example.dividends.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 배당금 관련 API
@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor

public class FinanceController {

    private final FinanceService financeService;

    // 회사 이름을 매개변수로 넣을 시, 배당금 정보 리턴
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }
}
