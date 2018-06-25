package com.inc.dayary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inc.dayary.interceptor.SignInInterceptor;

@Configuration
public class ServletContext implements WebMvcConfigurer {
	@Autowired
	private SignInInterceptor signInInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signInInterceptor)
		        .addPathPatterns("/")
		        .addPathPatterns("/member/mypage")
		        .addPathPatterns("/diary/**");
	}
}
