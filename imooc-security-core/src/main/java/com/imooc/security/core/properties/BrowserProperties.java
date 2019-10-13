package com.imooc.security.core.properties;

public class BrowserProperties {
	private String signUpUrl = "/imooc-signUp.html";
	private String loginPage = "/imooc-signIn.html"; //设置默认值
	private LoginType loginType = LoginType.JSON; //设置登录成功后的默认值（是跳转还是返回json）
	private int rememberMeSeconds = 3600;
	
	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public String getSignUpUrl() {
		return signUpUrl;
	}

	public void setSignUpUrl(String signUpUrl) {
		this.signUpUrl = signUpUrl;
	}
	
	
}
