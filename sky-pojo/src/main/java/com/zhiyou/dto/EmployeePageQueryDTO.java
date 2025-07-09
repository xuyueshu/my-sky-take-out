package com.zhiyou.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {
    // 员工名称
    private String name;
    // 页码
    private Integer page = 1;
    // 每页记录数
    private Integer pageSize = 10;
}
