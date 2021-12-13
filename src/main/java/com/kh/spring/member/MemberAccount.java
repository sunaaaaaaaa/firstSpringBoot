package com.kh.spring.member;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class MemberAccount extends User{ 
	//UserDetails implements하면 클래스에 Ovverride(구현체들..)뜸. 이거다 우리가 직접 구현해줘야함
	//Api : UserDetails
	//귀찮으니까 User를 extends로 바꿔줌

	private static final long serialVersionUID = 2256290252175108169L;
	private Member member;
	
	public MemberAccount(Member member) {
		//User에서 필수매개변수 넣어줌								Api:GrantedAutority에서 implements 골라서 넣어줌
		super(member.getUserId() , member.getPassword() , List.of(new SimpleGrantedAuthority(member.getGrade()))); 
		this.member = member;
	}

	public Member getMember() { //게시글 upload시 필요
		return member;
	}
	
	public String getPassword() {
		return member.getPassword(); //얘는 html에서 crendentials로 보여줘도되는데, 굳이 글자를 보여줘야겟으면 이렇게해줌
	}
	
	public String getEmail() {
		return member.getEmail(); //getter만들어줌
	}
	
	public String getTell() {
		return member.getTell();
	}
	
	public String getGrade() {
		return member.getGrade();
	}
}
