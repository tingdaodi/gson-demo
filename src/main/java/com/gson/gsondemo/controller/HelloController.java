package com.gson.gsondemo.controller;

import com.gson.gsondemo.annotation.OperationLog;
import com.gson.gsondemo.enums.OperationLogEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SOS
 *
 * @author Roye.L
 * @date 2019/4/2 22:31
 * @since 1.0
 */
@RestController
public class HelloController {

    @OperationLog(type = OperationLogEnum.GET, description = "获取用户信息")
    @GetMapping(value = "/user")
    public String obtainUser() {
        return "roy";
    }


}
