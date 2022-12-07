package com.example.dividends.model.constants;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public enum Month{

    JAN("Jan",1),
    FEB("Feb",2),
    MAR("MAR",3),
    APR("APR",4),
    MAY("MAY",5),
    JUN("JAN",6),
    JUL("JUL",7),
    AUG("AUG",8),
    SEP("SEP",9),
    OCT("OCT",10),
    NOV("NOV",11),
    DEC("DEC",12);

    private String s;
    private int number; // 각 enum 객체의 원소의 구성 요소

    public static int strToNumber(String s){

        for(Month m: Month.values()){
            if(m.s.equals(s)){
                return m.number;
            }
        }

        return -1; // 못찾는 경우
    }

}
