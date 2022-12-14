package com.example.dividends.exception.impl;

import com.example.dividends.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoCompanyException extends AbstractException {

    @Override
    public int getStatusCode() {

        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {

        return "존재하지 않은 서비스명 입니다.";
    }
}
