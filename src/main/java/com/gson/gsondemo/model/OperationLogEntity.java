package com.gson.gsondemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/7 22:33
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogEntity implements Serializable {

    private String logId;           //日志主键
    private String type;            //日志类型
    private String title;           //日志标题
    private String remoteAddr;      //请求地址
    private String requestUri;      //URI
    private String method;          //请求方式
    private String params;          //提交参数
    private String exception;       //异常
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateDate;           //开始时间
    private String timeout;         //结束时间
    private String userId;          //用户ID
    private String resultParams;    //返回参数


    /**
     * 设置请求参数
     *
     * @param paramMap 请求参数
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null) {
            return;
        }

        this.params = new Gson().toJson(params);
    }

}
