package com.imooc.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.imooc.exception.UserNotExistException;

@ControllerAdvice //表示当前类负责处理其他controller抛出的异常
public class ControllerExceptionHandler {
	
	@ExceptionHandler(UserNotExistException.class) //表示任何一个控制器抛出UserNotExistException异常时，进入这个方法来处理。
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String,Object> handlerUserNotExistException(UserNotExistException ex){
		Map<String,Object> result = new HashMap<>();
		result.put("id",ex.getId());
		result.put("message", ex.getMessage());
		return result;
	}
}
