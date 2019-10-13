package com.imooc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.HelloService;

//第一个泛型表示自定义的注解，第二个泛型表示使用该注解的属性的类型
//该校验器可以注入Spring的任何对象，因为我们实现了ConstraintValidator，Spring会自动管理我们的校验器，而不需要添加@Component注解
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object>{
	@Autowired
	private HelloService HelloService;
	
	@Override
	public void initialize(MyConstraint constraintAnnotation) {
		System.out.println("my validator init");
	}
	
	//校验逻辑在这里写，value表示要校验的值
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		HelloService.greeting("tom");
		System.out.println(value);
		return false; //返回false表示校验失败
	}

}
