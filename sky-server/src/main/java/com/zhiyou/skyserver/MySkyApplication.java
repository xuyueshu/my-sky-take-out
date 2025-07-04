package com.zhiyou.skyserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@EnableScheduling               //开启任务调度功能
@EnableCaching                  //开启缓存注解功能
@MapperScan("com.zhiyou.mapper")   //指定扫描mapper
@EnableTransactionManagement    //开启注解方式的事务管理
public class MySkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySkyApplication.class, args);
        log.info("************启动应用************");
    }

}
