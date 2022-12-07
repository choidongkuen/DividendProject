package com.example.dividends.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/company")
public class CompanyController {


    // 자동완성 검색
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyWord){


        return null;

    }

    // 회사 검색
    @GetMapping("")
    public ResponseEntity<?> searchCompany(){


        return null;


    }

    // 회사 추가
    @PostMapping("")
    public ResponseEntity<?> addCompany(){

        return null;
    }

    // 회사 삭제
    @DeleteMapping("")
    public ResponseEntity<?> deleteCompany(){

        return null;
    }

}
