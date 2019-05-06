package com.example.thymeleaf.controller;

import com.example.thymeleaf.constant.WebConst;
import com.example.thymeleaf.dto.LogActions;
import com.example.thymeleaf.model.TUsersPo;
import com.example.thymeleaf.response.ResponseEntity;
import com.example.thymeleaf.service.LogService;
import com.example.thymeleaf.service.UserService;
import com.example.thymeleaf.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @GetMapping("/login")
    public String Login(){
        return "/admin/login";
    }

    /**
     *
     * @param username
     * @param password
     * @param remeber_me
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity doLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String remeber_me,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        Integer error_count = cache.get("login_error_count");
        try {
            TUsersPo usersPo = userService.login(username, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY,usersPo);
            if (StringUtils.isNotEmpty(remeber_me))
                TaleUtils.setCookie(response, usersPo.getUid());
                logService.insertLog(LogActions.LOGIN.getAction(),null,request.getRemoteAddr(),usersPo.getUid());
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return new ResponseEntity.Builder<String>(null).error("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", error_count, 10 * 60);
            return new ResponseEntity.Builder<String>(null).error("登录失败");
        }
        return new ResponseEntity.Builder<String>(null).success();
    }

    /**
     * 页面跳转
     * @return
     */
    @GetMapping("/index")
    public String index(HttpServletRequest request){
        LOGGER.info("Enter admin index method");
        LOGGER.info("Exit admin index method");
        return "main";
    }


    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
