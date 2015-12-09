package com.vkviatkouski.sample.service;

import org.springframework.stereotype.Service;

@Service
public class Service1 implements IService {

	@Override
	public boolean exists(String id) {
		return "1".equals(id);
	}

}
