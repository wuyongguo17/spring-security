package com.imooc.security.core.validate.code;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.image.ImageCode;

//继承OncePerRequestFilter来保证我们的过滤器只会被调用一次，实现InitializingBean接口是为了初始化完毕时，执行我们下面的afterPropertiesSet()方法
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	private AuthenticationFailureHandler authenticationFailureHandler;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	private Set<String> urls = new HashSet<>();

	private SecurityProperties securityProperties;

	// spring的工具类
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		// 将我们的值用“,”割开成一个String数组
		if (securityProperties.getCode().getImage().getUrl() != null) {
			String[] configUrls = StringUtils
					.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
			for (String configUrl : configUrls) {
				urls.add(configUrl);
			}
		}
		urls.add("/authentication/form");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 当做/authentication.form请求时，我们的过滤器才会生效
//		if (StringUtils.equals("/authentication.form", request.getRequestURI())
//				&& StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
//			try {
//				validate(new ServletWebRequest(request));
//			} catch (ValidateCodeException e) {
//				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
//				return;
//			}
//		}
		
		// 改进版
		boolean action = false;
		for (String url : urls) {
			//如果urls中的url与我们的请求的url匹配上
			if (pathMatcher.match(url, request.getRequestURI())) {
				action = true;
			}
		}
		
		if(action) {
			try {
				validate(new ServletWebRequest(request));
			}catch(ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return;
			}
		}
		
		//如果不做上面的请求，就不做任何处理
		filterChain.doFilter(request, response);

	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,
				ValidateCodeController.SESSION_KEY_PREFIX + "CODE");
		// 从我们输入的验证码中获取,imageCode是表单中的<input type="text" name=imageCode>
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码的值不能为空");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}

		if (codeInSession.isExpried()) {
			sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_PREFIX + "CODE");
			throw new ValidateCodeException("验证码已过期");
		}

		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException("验证码不匹配");
		}

		sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_PREFIX + "CODE");
	}

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
