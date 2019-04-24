package com.example.miaosha.miaosha.response;

public class ResponseEntity<T> {
    public enum Status{
        SUCCESS(200),FAIL(400);
        private int code;

        Status(int i) {
            this.code = i;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    private Status code;
    private String message;
    private T data;

    public Status getCode() {
        return code;
    }

    public void setCode(Status code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity(Status code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
