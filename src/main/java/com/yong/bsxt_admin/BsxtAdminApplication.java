package com.yong.bsxt_admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author yong
 * @version 1.0
 * @Description: 启动类
 * @Date: 2019/12/23 18:23
 * @see
 */
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication()
public class BsxtAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BsxtAdminApplication.class, args);
    }

}
