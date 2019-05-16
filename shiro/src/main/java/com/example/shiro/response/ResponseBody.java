package com.example.shiro.response;


import com.alibaba.fastjson.JSON;

public class ResponseBody<T> {
    private String msg;
    private int code;
    private T data;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public ResponseBody(Builder<T> builder) {
        this.msg = builder.msg;
        this.code = builder.code;
        this.data = builder.data;
    }

    public static class Builder<T> {
        private String msg;
        private int code;
        private T data;

        public Builder() {
        }

        public Builder(T data) {
            this.data = data;
        }

        public ResponseBody<T> success(String msg){
            this.msg = msg;
            return new ResponseBody<>(this);
        }

        public ResponseBody<T> success(){
            this.msg = "successful";
            this.code = 1;
            return new ResponseBody<>(this);
        }

        public ResponseBody<T> error(String msg){
            this.msg = msg;
            this.code = 0;
            return new ResponseBody<>(this);
        }
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(new ResponseBody.Builder<String>("name").success()));
    }
}
