package com.example.springjpa.exception;

public class NoDataFoundException extends RuntimeException{


    private static final long serialVersionId = 1L;

    public NoDataFoundException(String msg){
        super(msg);
    }
}
