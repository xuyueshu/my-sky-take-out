package com.zhiyou.skyserver;

import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.entity.Employee;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmp(EmployeeDTO employeeDTO);
}
