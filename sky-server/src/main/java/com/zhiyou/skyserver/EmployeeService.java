package com.zhiyou.skyserver;

import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.dto.EmployeePageQueryDTO;
import com.zhiyou.entity.Employee;
import com.zhiyou.skycommon.Result.PageResult;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmp(EmployeeDTO employeeDTO);

    PageResult<Employee> page(EmployeePageQueryDTO dto);
}
