package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();

        //生产验证码
        if(phone!=null){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);

            //阿里云发送短信
//            SMSUtils.sendMessage("reggie","","phone","code");

            session.setAttribute(phone,code);

            return R.success("手机短信发送成功");
        }

        return R.error("手机短信发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpSession session){
        log.info(user.toString());


        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User user1 = userService.getOne(queryWrapper);

        if(user1 == null){
            userService.save(user);
        }
        session.setAttribute("user",user1.getId());
        return R.success(user1);

    }
}
