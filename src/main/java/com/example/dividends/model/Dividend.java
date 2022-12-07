package com.example.dividends.model;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Dividend {

    private LocalDateTime date;
    private String dividend;
}

