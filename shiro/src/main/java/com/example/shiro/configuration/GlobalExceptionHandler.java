package com.example.shiro.configuration;

import com.example.shiro.response.ErrorEnum;
import com.example.shiro.response.ResponseData;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData exceptionHandler(Exception e) {
        return new ResponseData<String>(ErrorEnum.E_400.getErrorCode(),ErrorEnum.E_400.getErrorMsg(),e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseData httpRequestMethodHandler() throws Exception {
        return new ResponseData<String>(ErrorEnum.E_500.getErrorCode(),ErrorEnum.E_500.getErrorMsg(),null);
    }


    /**
     * 权限不足报错拦截
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseData unauthorizedExceptionHandler() throws Exception {
        return new ResponseData<String>(ErrorEnum.E_502.getErrorCode(),ErrorEnum.E_502.getErrorMsg(),null);
    }

    /**
     * 未登录报错拦截
     * 在请求需要权限的接口,而连登录都还没登录的时候,会报此错
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseData unauthenticatedException() throws Exception {
        return new ResponseData<String>(ErrorEnum.E_20011.getErrorCode(),ErrorEnum.E_20011.getErrorMsg(),null);
    }
}
