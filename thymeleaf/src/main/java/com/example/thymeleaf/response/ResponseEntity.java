package com.example.thymeleaf.response;

public class ResponseEntity<T> {
    private String code;
    private String massage;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }

    public T getData() {
        return data;
    }

    public ResponseEntity(Builder<T> builder) {
        this.code = builder.code;
        this.massage = builder.massage;
        this.data = builder.data;
    }

    public static class Builder<T>{
        private String code;
        private String massage;
        private T data;


        public Builder(){

        }

        public Builder(T data) {
            this.data = data;
        }

        public ResponseEntity success(String massage){
            this.massage = massage;
            this.code = "200";
            return new ResponseEntity<>(this);
        }

        public ResponseEntity success(){
            this.massage = "success";
            this.code = "200";
            return new ResponseEntity<>(this);
        }

        public ResponseEntity error(String massage){
            this.massage = massage;
            this.code = "400";
            return new ResponseEntity<>(this);
        }

        public ResponseEntity error(){
            this.massage = "error";
            this.code = "400";
            return new ResponseEntity<>(this);
        }
    }
}
