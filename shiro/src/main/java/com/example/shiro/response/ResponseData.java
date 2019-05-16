package com.example.shiro.response;

import lombok.Data;

@Data
public class ResponseData<T>{
    private int code;
    private String msg;
    private T Data;

    public ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        Data = data;
    }
}
