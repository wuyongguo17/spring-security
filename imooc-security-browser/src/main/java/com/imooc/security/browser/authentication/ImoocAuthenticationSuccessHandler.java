package com.imooc.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.core.properties.LoginType;
import com.imooc.security.core.properties.SecurityProperties;
@Component
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler /* implements AuthenticationSuccessHandler */ {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ObjectMapper objectMapper; //springMVC在启动时会自动注册，我们使用@Autowired注解就可以获得
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * 登录成功会调用该方法，返回的UserDetails是包装在下面的参数authentication中的
	 */
	
	  @Override 
	  public void onAuthenticationSuccess(HttpServletRequest request,
	  HttpServletResponse response, Authentication authentication) throws
	  IOException, ServletException { 
		  
		  logger.info("登录成功");
		  if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			  response.setContentType("application/json;charset=UTF-8");
			  //将authentication写成json字符串
			  response.getWriter().write(objectMapper.writeValueAsString(authentication));
		  }else {
			  //调用父类的方法，父类默认是跳转的(跳到你一开始访问的那个地址)
			  super.onAuthenticationSuccess(request, response, authentication);
		  }
		  
		 
	  }
	 

}
