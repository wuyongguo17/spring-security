package com.imooc.security.core.validate.code;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.imooc.security.core.validate.code.image.ImageCode;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;

@RestController
public class ValidateCodeController {
	
	public static final String SESSION_KEY_PREFIX = "SESSION_KEY_PREFIX_CODE";
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	private ValidateCodeGenerator imageCodeGenerator;
	
	@Autowired
	private ValidateCodeGenerator smsCodeGenerator;
	
	@Autowired
	private SmsCodeSender smsCodeSender;
	
	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException{
		ImageCode imageCode = (ImageCode) imageCodeGenerator.createImageCode(new ServletWebRequest(request));
		//new ServletWebRequest(request)是拿session，name就是我们设置的session中的key
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_PREFIX+"CODE", imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}
	
	@GetMapping("/code/sms")
	public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException{
		ValidateCode smsCode = smsCodeGenerator.createImageCode(new ServletWebRequest(request));
		//new ServletWebRequest(request)是拿session，name就是我们设置的session中的key
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_PREFIX+"SMS", smsCode);
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
		smsCodeSender.send(mobile, smsCode.getCode());
	}
}
