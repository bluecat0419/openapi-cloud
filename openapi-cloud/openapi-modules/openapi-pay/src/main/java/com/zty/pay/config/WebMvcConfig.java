package com.zty.pay.config;

import com.zty.mvcconfig.interceptor.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangTianYou
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    LoginUserInfoInterceptor loginUserInfoInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //添加获取用户信息拦截器,由于每个服务的拦截路径不一样所以需要单独在每个服务中配置
        registry.addInterceptor(loginUserInfoInterceptor).addPathPatterns("/**").excludePathPatterns("/alipay/notifyUrl");
        super.addInterceptors(registry);
    }

    /**
     * 让 Mvc 加载 Swagger 的静态资源
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 解决返回值为String时转换异常问题
     * @param converters
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
