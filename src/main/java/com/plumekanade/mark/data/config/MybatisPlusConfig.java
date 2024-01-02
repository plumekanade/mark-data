package com.plumekanade.mark.data.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @ClassName MybatisPlusConfig
 * @Description mybatis-plus 分页处理
 * @Author huangshun
 * @Date 2022/1/21 11:42
 */
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 分页拦截器
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(false);
        paginationInnerInterceptor.setMaxLimit(2000L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime dateTime = LocalDateTime.now();
        this.setFieldValByName("createTime", dateTime, metaObject);
        this.setFieldValByName("updateTime", dateTime, metaObject);
//
//        // 针对 oa 模块
//        this.setFieldValByName("deleted", 1, metaObject);
//
//        // 针对 ums 模块
//        this.setFieldValByName("delFlag", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
