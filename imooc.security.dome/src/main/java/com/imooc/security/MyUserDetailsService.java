package com.imooc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService ,SocialUserDetailsService{  //SocialUserDetailsService用于第三方登录的
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		//根据用户名查找用户信息
//		return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
		String password = passwordEncoder.encode("123456");
		logger.info("数据库密码是:" + password);
		//根据查找到的用户信息判断用户是否被冻结
		return new User(username, password, true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("社交登录用户名:" + userId);
		//根据用户名查找用户信息
//		return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
		String password = passwordEncoder.encode("123456");
		logger.info("数据库密码是:" + password);
		//根据查找到的用户信息判断用户是否被冻结
		return new SocialUser(userId, password, true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
