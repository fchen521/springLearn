package com.example.shiro.configuration;

import com.example.shiro.response.ErrorEnum;
import com.example.shiro.response.ResponseData;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainsiteErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    /**
     * 主要是登陆后的各种错误路径  404页面改为返回此json
     * 未登录的情况下,大部分接口都已经被shiro拦截,返回让用户登录了
     *
     * @return 501的错误信息json
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseData handleError() {
        return new ResponseData<String>(ErrorEnum.E_501.getErrorCode(),ErrorEnum.E_501.getErrorMsg(),null);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
