package com.zhiyou.skyserver.service.impl;

import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.entity.Employee;
import com.zhiyou.skycommon.constant.MessageConstant;
import com.zhiyou.skycommon.constant.PasswordConstant;
import com.zhiyou.skycommon.constant.StatusConstant;
import com.zhiyou.skycommon.exception.AccountLockedException;
import com.zhiyou.skycommon.exception.AccountNotFoundException;
import com.zhiyou.skycommon.exception.PasswordErrorException;
import com.zhiyou.skyserver.EmployeeService;
import com.zhiyou.skyserver.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.xml.stream.events.DTD;


@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO){

        // 获取用户输入的账号密码
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 处理各种异常情况（用户不存在、密码错误、账号被锁定）

        // 1、用户不存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2、密码错误
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3、账号被锁定
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }


        return employee;
    }

    @Override
    public void addEmp(EmployeeDTO employeeDTO) {
        log.info("EmployeeServiceImpl:线程id={}",Thread.currentThread().getId());
        Employee employee = new Employee();
        // 属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);

        // 1、补充缺失的属性值
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);

    }
}
