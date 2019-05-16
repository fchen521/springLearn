package com.example.shiro.controller;

import com.example.shiro.model.SysUser;
import com.example.shiro.response.ResponseBody;
import com.example.shiro.response.ResponseData;
import com.example.shiro.service.UserService;
import com.example.shiro.util.RSAUtils;
import com.example.shiro.util.SecurityCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/login")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //??????????????
    private static final Map<String,String> CACHE_MAP = new HashMap<>();
   /* @GetMapping
    public ResponseData UserInfo(@RequestParam(required = false) String name){
        return new ResponseData<SysUser>(200,"succ",userService.getUserByName(name));
    }*/



    @PostMapping("/auth")
    public ResponseData authLogin(@RequestBody SysUser.ResponseU user) {
        if (StringUtils.isAnyEmpty(user.getUsername(),user.getPassword(),user.getCode())) {
            return new ResponseData<String>(500,"????","????????????п??");
        }
        try {
            String priKey = CACHE_MAP.get(user.getUsername());
            byte[] bytes = RSAUtils.decryptByPrivateKey(Base64.getDecoder().decode(user.getPassword()), priKey);
            String encoded1 = Base64.getEncoder().encodeToString(bytes);
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),new String(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(encoded1)));
            subject.login(token);
            return new ResponseData<String>(200,"succ","??????");
        }catch (IncorrectCredentialsException e) {
            return new ResponseData<>(400,"???????",null);
        }catch (LockedAccountException e) {
            return new ResponseData<>(401,"????????",null);
        }catch (AuthenticationException e) {
            return new ResponseData<>(402,"?????????",null);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(500,"????",e.getMessage());
        }finally {
            CACHE_MAP.clear();
        }
    }

    @GetMapping("/reg")
    public ResponseData signIn(@RequestParam(required = false) String username) throws Exception {
        if (StringUtils.isBlank(username)) {
            return new ResponseData<String>(400,"error",null);
        }
        Map<String, Object> map = RSAUtils.genKeyPair();
        Object publicKey =  map.get("RSAPublicKey");
        Object privateKey = map.get("RSAPrivateKey");
        Map<String,Object> map1=new HashMap<>();
        map1.put("RSAPublicKey",publicKey);
        String pubKey = RSAUtils.getPublicKey(map1);
        Map<String,Object> map2=new HashMap<>();
        map2.put("RSAPrivateKey",privateKey);
        String priKey = RSAUtils.getPrivateKey(map2);
        if (priKey!=null) {
            CACHE_MAP.put(username,priKey);
        }
        return new ResponseData<String>(200,"succ",pubKey);
    }

    @GetMapping("/logout")
    public ResponseBody logout(){
       // SecurityUtils.getSubject().logout();
        //return new ResponseData<String>(200,"???",null);
       // return new ResponseBody.Builder<String>("name").success();
        return new ResponseBody.Builder<String>("ceshi").error("错误");
    }

    @GetMapping("image")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Pragma","No-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //????????????????
        //???????????????????????????????????????
        Object[] obj = SecurityCodeUtil.createImage();
        //??????????Session
        request.getSession().setAttribute("imageCode",obj[0]);
        BufferedImage image = (BufferedImage) obj[1];
        response.setContentType("image/png");
        //??????????????
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
