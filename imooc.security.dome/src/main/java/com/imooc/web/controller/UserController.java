package com.imooc.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import com.imooc.dto.User.UserDetailView;
import com.imooc.dto.User.UserSimpleView;
import com.imooc.dto.UserQueryCondition;
import com.imooc.exception.UserNotExistException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController //告诉spring该类提供Restful服务
public class UserController {
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	
	/*
	 * @GetMapping("/me") public Object getCurrentUser() { return
	 * SecurityContextHolder.getContext().getAuthentication(); }
	 */
	
	//上面的可以这样写
	/*
	 * @GetMapping("/me") public Object getCurrentUser(Authentication
	 * authentication) { return authentication; }
	 */
	
	//再次改进，上面返回的信息太多了，我们想要返回的信息少点，即只返回UserDetails
	@GetMapping("/me")
	public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
		return user;
	}
	
	@DeleteMapping("/user/{id:\\d+}")
	public void delete(@PathVariable String id) {
		System.out.println(id);
	}
	
	@PostMapping("/user")
	public User create(@Valid @RequestBody User user,BindingResult errors) {
		if(errors.hasErrors()) {
			errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
		}
		
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getId());
		System.out.println(user.getBirthday());
		user.setId("1");
		return user;
	}
	
	@PutMapping("/user/{id:\\d+}")
	public User update(@Valid @RequestBody User user,BindingResult errors) {
		if(errors.hasErrors()) {
			errors.getAllErrors().stream().forEach(error -> 
			{
				FieldError fieldEror = (FieldError) error;
				//获取错误字段及信息
				String message = fieldEror.getField() +" "+error.getDefaultMessage();
				System.out.println(message);
			
			});
		}
		
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getId());
		System.out.println(user.getBirthday());
		user.setId("1");
		return user;
	}
	
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@JsonView(UserSimpleView.class)
	@ApiOperation(value = "用户查询") //value值会代替方法名显示
	public List<User> query(UserQueryCondition condition,@PageableDefault(page = 2,size = 17,sort = "username,asc") Pageable pageable){
		System.out.println(ReflectionToStringBuilder.toString(condition,ToStringStyle.MULTI_LINE_STYLE));
		System.out.println(pageable.getPageSize());
		System.out.println(pageable.getPageNumber());
		System.out.println(pageable.getSort());
		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}
	
	//:\\d+正则表达式，表示只接受数字
	@RequestMapping(value = "/user/{id:\\d+}",method = RequestMethod.GET)
	@JsonView(UserDetailView.class)
	public User getInfo(@ApiParam(value = "用户id") @PathVariable String id) {
		System.out.println("进入getInfo服务");
//		throw new RuntimeException("user not exsit");
//		throw new UserNotExistException(id); //这个异常被ControllerExceptionHandler处理，所以afterCompletion()中的ex是空的。
		User user = new User();
		user.setUsername("tom");
		return user;
	}
	
	@PostMapping("/user/regist")
	public void regist(User user, HttpServletRequest request) {
		
		//不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
		String userId = user.getUsername();
		providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
	}
}
