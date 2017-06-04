package com.zxiao.web.controller;

import com.zxiao.service.bean.User;
import com.zxiao.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zxiao.cm.bean.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zxiao on 2017/6/3.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/test1")
    public String test1(HttpServletRequest request, HttpServletResponse response){
        int total=userService.getotalUser();
        System.out.print(total);
        return "/index";
    }


}
