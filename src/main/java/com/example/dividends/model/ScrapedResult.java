package com.example.dividends.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder

// 스크래핑 결과를 위한 클래스
public class ScrapedResult {

    private Company company;
    private List<Dividend> dividendEntities;

    public ScrapedResult(){
        this.dividendEntities
                = new ArrayList<>();
    }
}


