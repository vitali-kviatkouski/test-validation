package com.vkviatkouski.sample.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.vkviatkouski.sample.service.IService;


public class ChannelExistsValidator implements ConstraintValidator<ChannelExists, String> {
	@Autowired
	private IService service;

	public boolean isValid(String value, ConstraintValidatorContext context) {
		return service.exists(value);
	}

	public void initialize(ChannelExists constraintAnnotation) {
	}

	public void setService(IService service) {
		this.service = service;
	}
}
