package com.vkviatkouski.sample.rest;

import com.vkviatkouski.sample.validation.ChannelExists;

public interface ITestController {
	/**
	 * Here goes REST description
	 * http://localhost:8080/TestValidation2/mvc/status?id=1 - for success
	 * http://localhost:8080/TestValidation2/mvc/status?id=2 - for error
	 * @param id
	 * @return
	 */
	String result(@ChannelExists String id);
}