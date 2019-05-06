package com.example.thymeleaf.service;

import com.example.thymeleaf.mapper.TLogsPOMapper;
import com.example.thymeleaf.model.TLogsPO;
import com.example.thymeleaf.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    TLogsPOMapper logsPOMapper;

    public void insertLog(String action, String data, String ip, Integer authorId) throws Exception{
        TLogsPO logs = new TLogsPO();
        logs.setAction(action);
        logs.setData(data);
        logs.setIp(ip);
        logs.setAuthorId(authorId);
        logs.setCreated(DateUtils.getCurrentUnixTime());
        logsPOMapper.insert(logs);
    }
}
