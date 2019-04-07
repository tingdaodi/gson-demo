package com.gson.gsondemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/3 0:08
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    private String username;
    private Integer age;
}
