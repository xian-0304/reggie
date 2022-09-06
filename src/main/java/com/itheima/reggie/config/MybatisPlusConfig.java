package com.itheima.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置MP的分页插件
 */
@Configuration
public class MybatisPlusConfig {


    /**
     * mybatisPlusConfig2 重复命名，修改即可
     * https://blog.csdn.net/qq_37663871/article/details/105577394
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusConfig2(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
