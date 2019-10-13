package com.imooc.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConstraintValidator.class) //validatedBy表示当我们加上该注解时，执行的逻辑在哪个类上
public @interface MyConstraint {
	String message();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	//以上三个属性是必须的
}
