package com.example.thymeleaf.controller;

import com.example.thymeleaf.model.TUsersPo;
import com.example.thymeleaf.utils.MapCache;
import com.example.thymeleaf.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {
    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();

    public String render(String viewName){
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request,String title){
        request.setAttribute("title",title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public TUsersPo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }

}
