package com.ai_ss.global.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheckController implements HealthCheckApi {

	@Override
	@GetMapping
	public String healthcheck() {
		return "OK";
	}
}
