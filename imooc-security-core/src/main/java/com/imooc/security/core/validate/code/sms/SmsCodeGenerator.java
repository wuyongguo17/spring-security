package com.imooc.security.core.validate.code.sms;


import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.ValidateCodeGenerator;

@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityPropertites;
	
	@Override
	public ValidateCode createImageCode(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(securityPropertites.getCode().getSms().getLength());
		return new ValidateCode(code, securityPropertites.getCode().getSms().getExpireIn());
	}

	public SecurityProperties getSecurityProperties() {
		return securityPropertites;
	}

	public void setSecurityProperties(SecurityProperties securityPropertites) {
		this.securityPropertites = securityPropertites;
	}

	

}
