package com.zxac.rabbit_provider.controller;

import com.zxac.rabbit_provider.mapper.UserMapper;
import com.zxac.rabbit_provider.utils.RandomValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsertTestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("mybatisTest")
    public Object mybatisTest() {
        userMapper.insert(RandomValue.getAddress());

        return "success";
    }

}
