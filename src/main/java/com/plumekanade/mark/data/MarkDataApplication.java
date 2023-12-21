package com.plumekanade.mark.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.plumekanade")
@MapperScan("com.plumekanade.mark.data.mapper")    // 设置mapper接口扫描地址
@EnableTransactionManagement(proxyTargetClass = true)   // 开启事务管理, 不写Service接口层, 声明基于类代理(CGlib)
public class MarkDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarkDataApplication.class, args);
	}

}
