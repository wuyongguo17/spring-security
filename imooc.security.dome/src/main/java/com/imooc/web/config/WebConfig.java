package com.imooc.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.imooc.filter.TimeFilter;
import com.imooc.web.interceptor.TimeInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
	@Autowired
	private TimeInterceptor timeInterceptor;
	
	@Override
	//针对异步请求注册拦截器
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//		configurer.registerCallableInterceptors(interceptors)
		super.configureAsyncSupport(configurer);
	}
	
	//暂时注解掉，防止影响后面学习的内容
//	@Bean
//	public FilterRegistrationBean timeFilter() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		TimeFilter timeFilter = new TimeFilter();
//		filterRegistrationBean.setFilter(timeFilter);
//		
//		//设置起用的路径
//		List<String> urls = new ArrayList<>();
//		urls.add("/*"); //表示所有路径
//		filterRegistrationBean.setUrlPatterns(urls);
//		return filterRegistrationBean;
//		
//	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(timeInterceptor);
	}
}
