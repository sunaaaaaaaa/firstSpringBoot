package com.kh.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	
	//InterceptorRegistry로 만든거 삭제함->Security로 관리
	
	@Override
	public void	addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/file/**")
				.addResourceLocations("file:///C:/CODE/after/upload/");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
