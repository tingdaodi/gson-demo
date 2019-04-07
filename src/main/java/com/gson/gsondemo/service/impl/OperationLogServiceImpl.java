package com.gson.gsondemo.service.impl;

import com.google.gson.Gson;
import com.gson.gsondemo.model.OperationLogEntity;
import com.gson.gsondemo.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/7 23:00
 * @since 1.0
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Override
    public void add(OperationLogEntity entity) {
        System.out.println("add operation log.");
        System.out.println(new Gson().toJson(entity));
    }

    @Override
    public String obtain() {
        return "success";
    }
}
