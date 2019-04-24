package com.example.miaosha.miaosha.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.example.miaosha.miaosha.mapper.UserInfoPOMapper;
import com.example.miaosha.miaosha.model.UserInfoPO;
import com.example.miaosha.miaosha.response.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserInfoPOMapper mapper;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/list")
    public ResponseEntity listUserInfo(){
        try {
            List<UserInfoPO> pos = mapper.selectAll();
            if (pos.size()!=0){
                return new ResponseEntity<List>(ResponseEntity.Status.SUCCESS,"succ",pos);
            }
            return new ResponseEntity<List>(ResponseEntity.Status.SUCCESS,"succ",null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<List>(ResponseEntity.Status.FAIL,e.getMessage(),null);
        }
    }

    @GetMapping("/list/{name}")
    public ResponseEntity queryUserInfoByName(@PathVariable String name){
        try {
            UserInfoPO po = new UserInfoPO();
            po.setName(name);
            UserInfoPO pos = mapper.selectOne(po);
            if (pos != null){
                return new ResponseEntity<UserInfoPO>(ResponseEntity.Status.SUCCESS,"succ",pos);
            }
            return new ResponseEntity<List>(ResponseEntity.Status.SUCCESS,"succ",null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<List>(ResponseEntity.Status.FAIL,e.getMessage(),null);
        }
    }


    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
