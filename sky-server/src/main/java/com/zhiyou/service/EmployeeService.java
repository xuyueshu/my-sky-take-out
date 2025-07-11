package com.zhiyou.service;

import com.zhiyou.dto.EmployeeChangePasswdDTO;
import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.dto.EmployeePageQueryDTO;
import com.zhiyou.entity.Employee;
import com.zhiyou.Result.PageResult;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmp(EmployeeDTO employeeDTO);

    PageResult<Employee> page(EmployeePageQueryDTO dto);

    void update(EmployeeDTO dto);

    void enableOrDisable(Long id, Integer status);

    Employee getEmploy(Long id);

    void changePassword(EmployeeChangePasswdDTO dto);
}
