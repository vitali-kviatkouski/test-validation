package com.vkviatkouski.sample.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ChannelExistsValidator.class)
public @interface ChannelExists {
	String message() default "Error";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}