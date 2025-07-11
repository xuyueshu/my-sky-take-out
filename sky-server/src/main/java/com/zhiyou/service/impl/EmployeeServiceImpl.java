package com.zhiyou.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhiyou.dto.EmployeeChangePasswdDTO;
import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.dto.EmployeePageQueryDTO;
import com.zhiyou.entity.Employee;
import com.zhiyou.Result.PageResult;
import com.zhiyou.constant.MessageConstant;
import com.zhiyou.constant.PasswordConstant;
import com.zhiyou.constant.StatusConstant;
import com.zhiyou.exception.AccountLockedException;
import com.zhiyou.exception.AccountNotFoundException;
import com.zhiyou.exception.PasswordErrorException;
import com.zhiyou.service.EmployeeService;
import com.zhiyou.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


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

    @Override
    public PageResult<Employee> page(EmployeePageQueryDTO dto) {
        // 1、设置分页参数
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        // 2、查询数据库，将结果强转为Page
        Page<Employee> page = employeeMapper.list(dto.getName());

        // 3、封装PageResult并返回
        return new PageResult<>((int) page.getTotal(),page.getResult());
    }

    @Override
    public void update(EmployeeDTO dto) {
        if (dto.getId() == null){
            throw new RuntimeException("id不能为空！");
        }
        if (StringUtils.isBlank(dto.getSex())){
            throw new RuntimeException("性别不能为空！");
        }
        if (StringUtils.isBlank(dto.getPhone())){
            throw new RuntimeException("电话号码不能为空！");
        }
        if (StringUtils.isBlank(dto.getName())){
            throw new RuntimeException("姓名不能为空！");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto,employee);
        employeeMapper.update(employee);
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        employeeMapper.updateStatusById(id,status);
    }

    @Override
    public Employee getEmploy(Long id) {
        return employeeMapper.getEmployById(id);
    }

    @Override
    public void changePassword(EmployeeChangePasswdDTO dto) {

        Long id = dto.getId();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();

        // 加密
         oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
         newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        // 1、根据id查询员工信息
        Employee employee = employeeMapper.getEmployById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 2、将旧密码加密后进行比对
        if (!oldPassword.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        Employee newEmp = new Employee();
        BeanUtils.copyProperties(dto,newEmp);
        newEmp.setPassword(newPassword);

        // 3、将新密码加密后更新到数据库
        employeeMapper.update(newEmp);
    }
}
