package com.zhiyou.aspect;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    // 指定切点
    @Pointcut("execution(* com.zhiyou.controller..*(..))")
    public void ControllerPointCut(){

    }

    // 采用环绕通知，打印接收到的参数和返回值
    @Around("ControllerPointCut()")
    public Object printControllerInfo(ProceedingJoinPoint joinPoint){
        // 获取目标方法的签名信息，描述了当前连接点对应的方法、构造函数或字段的签名
        Signature signature = joinPoint.getSignature();
        // 获取目标方法名
        String methodName = signature.getName();
        // 获取目标方法参数
        Object[] args = joinPoint.getArgs();
        // 获取@ApiOperation注解中的描述
        MethodSignature methodSignature = (MethodSignature) signature; // 强转为MethodSignature
        Method method = methodSignature.getMethod(); // 获取到目标方法
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class); // 通过目标方法获取到注解
        String apiDoc = apiOperation.value();

        // 获取controller中的url
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest(); // 获取request对象
        String methodPath = request.getRequestURL().toString();

        try {
            log.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
            log.info("当前接口名称：{}，方法名：{},url:{}",apiDoc,methodName,methodPath);
            log.info("接收到的参数：{}",args);
            // 执行目标方法，并获取结果
            Object result = joinPoint.proceed();
            log.info("返回结果：{}",result);
            log.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
            // 将结果返回
            return result;
        } catch (Throwable e) {
            log.error("{}方法-{}-执行报错：{}",methodName,e.getMessage());
            log.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
            return null;
        }
    }
}
