package com.imooc.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import com.imooc.security.browser.authentication.ImoocAuthenticationFailureHandler;
import com.imooc.security.browser.authentication.ImoocAuthenticationSuccessHandler;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.SmsCodeFilter;
import com.imooc.security.core.validate.code.ValidateCodeFilter;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private ImoocAuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
	
	@Autowired
	private ImoocAuthenticationFailureHandler imoocAuthenticationFailureHandler;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Autowired
	private SpringSocialConfigurer imoocSocialSecurityConfig;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		//启动时创建表，第二次启动时要注释掉
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		
		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();
		
		//把我们的过滤器放在UsernamePasswordAuthenticationFilter过滤器的前面
		http
		.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) //好像没有生效
		//表单形式任何请求都要认证且需要授权，才能访问。
			.formLogin()
			.loginPage("/imooc-signIn.html")
//			.loginPage("/authentication/require") //使用一个controller来处理
			.loginProcessingUrl("/authentication/form") //表示该请求使用UsernamePasswordAuthenticationFilter来处理
			.successHandler(imoocAuthenticationSuccessHandler) //使用我们自己成功处理器(当表单登录成功时)
			.failureHandler(imoocAuthenticationFailureHandler) //使用失败的处理器
			.and()
			.apply(imoocSocialSecurityConfig) //第三方登录配置生效
			.and()
			//记住我功能
			.rememberMe()
			.tokenRepository(persistentTokenRepository())
			.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
			.userDetailsService(userDetailsService) //配这个是使用userDetailsService来登录
			.and()
			.authorizeRequests()
			.antMatchers("/authentication/require",
					securityProperties.getBrowser().getLoginPage(),"/code/*",securityProperties.getBrowser().getSignUpUrl()
				 ,"/user/regist").permitAll() //表示访问/imooc-signIn.html时，不需要身份认证（不会被拦截进入loginPage()里的配置的资源或路径）。
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable()
			.apply(smsCodeAuthenticationSecurityConfig); //是短信验证生效（手机，账号两种登录方式都能登录），相当于自己写一个类似于UsernamePasswordAuthenticationFilter的手机登录流程
		
		//httpBasic形式登录
//		http.httpBasic()
//		.and()
//		.authorizeRequests()
//		.anyRequest()
//		.authenticated();
	}
	
}
