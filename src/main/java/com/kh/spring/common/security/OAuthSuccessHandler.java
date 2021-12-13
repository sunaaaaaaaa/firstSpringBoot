package com.kh.spring.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuthSuccessHandler implements AuthenticationSuccessHandler{

	@Override						//여기에 servlet객체가 넘어왓냐면.. 이걸(filter)통해서 넘어온 파라미터 확인함
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.info(authentication.toString()); //확인
		log.info(authentication.getDetails().toString());
		log.info(authentication.getPrincipal().toString());
		
		//다운캐스팅
		OAuth2User user =(OAuth2User) authentication.getPrincipal();
		//이메일 꺼내오기
		log.info(user.getAttribute("email"));
		
		response.sendRedirect("/");
	}

}
