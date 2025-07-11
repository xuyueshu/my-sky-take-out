package com.zhiyou.aspect;

import com.zhiyou.anno.Autofill;
import com.zhiyou.constant.AutoFillConstant;
import com.zhiyou.context.BaseContext;
import com.zhiyou.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Before("execution(* com.zhiyou.mapper.*.*(..)) && @annotation(com.zhiyou.anno.Autofill)" )
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充");

        // 1、获取目标方法上的注解，并拿到注解里面的属性值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Autofill autofill = method.getAnnotation(Autofill.class);
        OperationType operationType = autofill.value();

        // 2、获取到目标方法的参数对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 3、判断注解中的属性值，如果是INSERT，就补充四个字段（创建时间、更新时间、创建人、更新人）
        try {

            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            if (operationType == OperationType.INSERT) {
                setCreateTime.invoke(entity,LocalDateTime.now());
                setUpdateTime.invoke(entity,LocalDateTime.now());
                setCreateUser.invoke(entity, BaseContext.getCurrentId());
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
            } else if (operationType == OperationType.UPDATE) {
                setUpdateTime.invoke(entity,LocalDateTime.now());
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
            }
        } catch (Exception e) {
            log.error("公共字段自动填充失败：{}",e.getMessage());
        }


    }

}
