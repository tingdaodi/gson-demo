package com.gson.gsondemo.service;

import com.gson.gsondemo.model.OperationLogEntity;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/7 22:59
 * @since 1.0
 */
public interface OperationLogService {

    void add(OperationLogEntity entity);

    String obtain();

}
