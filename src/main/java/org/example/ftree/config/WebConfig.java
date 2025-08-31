package org.example.ftree.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BaseInterceptor baseInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add interceptor:{}", baseInterceptor.getClass().getSimpleName());
        // 注册日志拦截器，拦截所有请求
        registry.addInterceptor(baseInterceptor)
                // 拦截所有路径
                .addPathPatterns("/**")
                // 排除静态资源和错误路径
                .excludePathPatterns("/static/**", "/error");
        // 注册Token拦截器，拦截所有请求
        registry.addInterceptor(tokenInterceptor)
                // 拦截所有路径
                .addPathPatterns("/**")
                // 排除静态资源和错误路径
                .excludePathPatterns("/static/**", "/error");
    }
}
