package com.kh.spring.common.code;

public enum Config {
	
	DOMAIN("http://localhost:9090"),
	COMPANY_EMAIL("tjsdk7075@gmail.com"),
	SMTP_AUTHENTICATION_ID("tjsdk7075@gmail.com"),
	SMTP_AUTHENTICATION_PASSWORD("Chltjsdk1!"),
	UPLOAD_PATH("C:\\CODE\\upload\\");

	public final String DESC;
	
	Config(String desc) {
		this.DESC = desc;
	}

	
	
}
