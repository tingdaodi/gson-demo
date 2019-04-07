package com.gson.gsondemo.annotation;

import com.gson.gsondemo.enums.OperationLogEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作类型
     *
     * @return OperationLogEnum
     */
    OperationLogEnum type() default OperationLogEnum.GET;

    /**
     * 描述
     *
     * @return String
     */
    String description() default "";

}
