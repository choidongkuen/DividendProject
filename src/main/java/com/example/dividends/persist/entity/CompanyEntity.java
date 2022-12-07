package com.example.dividends.persist.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "COMPANY")

public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DB 에게 기본값 설정 위임
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String name;
}
