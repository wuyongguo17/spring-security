package com.imooc.web.aspect;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class TimeAspect {
	//切入点:1,在哪些方法上起作用。2，在什么时候起作用
	
	@Around("execution(* com.imooc.web.controller.UserController.*(..))")
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {  //pjp表示拦截住的方法的对象，相当于使用intercceptor时，方法中的handler。
		System.out.println("time aspect start");
		
		Object[] args = pjp.getArgs();
		
		for (Object arg : args) {
			System.out.println("arg is" + " " + arg);
		}
		
		long start = new Date().getTime();
		
		//返回的object表示所调用的controller方法中的返回值
		Object object = pjp.proceed(); //相当于使用Filter时的chain.doFilter(request, response);
		
		
		System.out.println("time aspect耗时:" + (new Date().getTime() - start));
		
		System.out.println("time aspect end");
		
		return object;
	}
}
