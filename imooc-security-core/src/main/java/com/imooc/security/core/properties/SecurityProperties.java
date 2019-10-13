package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imooc.security")
public class SecurityProperties {
	private BrowserProperties browser = new BrowserProperties();
	private ValidateCodePropertites code = new ValidateCodePropertites();
	private SocialProperties social = new SocialProperties();
	
	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ValidateCodePropertites getCode() {
		return code;
	}

	public void setCode(ValidateCodePropertites code) {
		this.code = code;
	}

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	
	
}
