package com.boda.onlineqandaspring.config;

import com.boda.onlineqandaspring.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 设置默认首页
        registry.addViewController("/").setViewName("redirect:/user/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源路径
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        // 添加 /Images/** 映射（大写 I）
        registry.addResourceHandler("/Images/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，排除公开路径
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",           // 登录页面
                        "/user/register",        // 注册页面
                        "/user/logout",          // 登出页面
                        "/captcha/**",           // 验证码相关
                        "/static/**",            // 静态资源
                        "/images/**",            // 图片资源
                        "/error"                 // 错误页面
                );
    }
}