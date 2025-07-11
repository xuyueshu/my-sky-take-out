package com.zhiyou.mapper;

import com.github.pagehelper.Page;
import com.zhiyou.anno.Autofill;
import com.zhiyou.entity.Employee;
import com.zhiyou.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Autofill(OperationType.INSERT)
    @Insert("insert into employee(id,name,username,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user) " +
        "values (null,#{username},#{name},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

    Page<Employee> list(@Param("name") String name);

    @Autofill(OperationType.UPDATE)
    void update(Employee dto);
}
