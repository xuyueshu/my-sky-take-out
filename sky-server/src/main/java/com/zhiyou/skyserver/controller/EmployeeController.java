package com.zhiyou.skyserver.controller;

import com.zhiyou.dto.EmployeeDTO;
import com.zhiyou.dto.EmployeeLoginDTO;
import com.zhiyou.dto.EmployeePageQueryDTO;
import com.zhiyou.entity.Employee;
import com.zhiyou.skycommon.Result.PageResult;
import com.zhiyou.skycommon.constant.JwtClaimsConstant;
import com.zhiyou.skycommon.properties.JwtProperties;
import com.zhiyou.skycommon.utils.JwtUtil;
import com.zhiyou.skyserver.EmployeeService;
import com.zhiyou.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.result.Result;
import java.util.HashMap;

@RestController
@Slf4j
@Api(tags = "员工相关接口")
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    @ApiOperation("员工登录")
    @PostMapping ("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录:{}",employeeLoginDTO);
        // 查询数据库
        Employee employee = employeeService.login(employeeLoginDTO);
        // 登录成功后，生成jwt令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        // 封装response数据
        EmployeeLoginVO loginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .userName(employee.getUsername())
                .token(token)
                .build();
        return Result.success(loginVO);
    }

    @ApiOperation("新增员工")
    @PostMapping
    public Result addEmp(@RequestBody EmployeeDTO employeeDTO){
        log.info("EmployeeController:线程id={}",Thread.currentThread().getId());
        log.info("新增员工：{}",employeeDTO);
        employeeService.addEmp(employeeDTO);
        return Result.success();
    }

    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult<Employee>> page(EmployeePageQueryDTO dto){
        log.info("员工分页查询：{}",dto);
        PageResult<Employee> pageResult = employeeService.page(dto);
        return Result.success(pageResult);
    }
}
