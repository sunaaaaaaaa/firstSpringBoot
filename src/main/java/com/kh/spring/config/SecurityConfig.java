package com.kh.spring.config;

import javax.sql.DataSource;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.kh.spring.common.security.OAuthFailureHandler;
import com.kh.spring.common.security.OAuthSuccessHandler;
import com.kh.spring.member.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity //Spring Security의 기본설정대신 선언된 클래스에서 설정한 내용을 시큐리티에 적용 
@RequiredArgsConstructor // Authentication땜에 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter{ //Api : WebSecurityConfigurerAdapter

	//시큐리티 Dialect를 빈으로 만들어줌 -> 이유는 영상봐야됨
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
	//Authentication 인증방법
	//토큰을 DB에 저장하는 방식 / 쿠키로 저장하는 방식
	//토큰방식 쓸거임
	private final DataSource dataSource;
	private final MemberService memberService;
	
	//oAuth
	private final OAuthSuccessHandler oAuthSuccessHandler;
	private final OAuthFailureHandler oAuthFailureHandler;
	
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		
		web.ignoring()
			.mvcMatchers("/static/**")	//import는 PathRequest 서블릿패키지꺼 사용
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		//resourses Handler에 등록한 경로에 대해서 관리해줌 (파일다운로드 경로 등..)
	}
	
	//여기서 본격적으로 권한관리를 함
	//어떤경로에 들어온 것에대해 어떻게 관리를 할건지(구체적으로..)
	@Override 			
	public void configure(HttpSecurity http) throws Exception { //Api: HttpSecurity참고
		
		http.authorizeRequests()		//마이페이지랑 로그아웃은 인증된사용자에게만 허가
			.mvcMatchers(HttpMethod.GET,"/member/mypage","/member/logout").authenticated() //authenticated()뿐만아니라 hasRole("USER")이나 뭐 api찾아보면 딴것도나옴
			.anyRequest().permitAll() //permitAll -> 열어줌
			.and()
			.oauth2Login(); //outh2Login가능하게해줌
		
		//oauth2로 구글로그인
		http.oauth2Login()
			.loginPage("/member/login")
			.successHandler(oAuthSuccessHandler) //여기서 handler만들어준거 설정해줌
			.failureHandler(oAuthFailureHandler);
		
		 //로그인도 열어쥼
		http.formLogin() //자동으로 로그인처리해줌
			.loginProcessingUrl("/member/login")
			.usernameParameter("userId")
			.loginPage("/member/login")
			.defaultSuccessUrl("/");	//성공했을때 리다이렉트 해줄 url
			//.failureForwardUrl("/member/login"); //실패했을때
		
		//로그아웃 (a태그(url)가 아닌 post여야 가능함)
		http.logout()
			.logoutUrl("/member/logout")
			.logoutSuccessUrl("/"); //메인으로 리다이렉트해줌
		
		//mail로 들어오는 요청에대해서 csrf를 해제
		http.csrf().ignoringAntMatchers("/mail");
		
		
		//Authentication 인증(브라우저꺼도 로그인 유지)
		http.rememberMe()
			.userDetailsService(memberService)
			.tokenRepository(tokenRepository());
		
		
		
	}
	
	
	
	
	
}
