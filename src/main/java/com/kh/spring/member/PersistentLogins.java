package com.kh.spring.member;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity //토큰 DB에 저장하기위한 Entity
@Data
public class PersistentLogins {

	//api Security Reference보고 필요한거 적어줌 10.12.4 참고
	@Column(columnDefinition = "varchar2(64 char) not null")
	private String username;
	
	@Id
	@Column(columnDefinition = "varchar2(64 char)")
	private String series;
	
	//로그인시마다 token이 바뀜
	@Column(columnDefinition = "varchar2(64 char) not null")
	private String token;
	
	@Column(columnDefinition = "timestamp not null")
	private LocalDateTime lastUsed;
	
	
}
