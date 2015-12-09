package com.vkviatkouski.sample.rest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Validated
@Controller
public class TestController implements ITestController {
	// here goes mapping for SpringMVC
	// mapping for bean validation see in IController
	@RequestMapping("status")
	@ResponseBody
	public String result(@RequestParam("id") String id ) {
		return "Hello " + id;
	}

	@ResponseBody
	@ExceptionHandler(RestBusinessException.class)
	public String handler(RestBusinessException e) {
		return "Validation message: code=" + e.getCode() + ", message=" + e.getMessage();
	}
}
