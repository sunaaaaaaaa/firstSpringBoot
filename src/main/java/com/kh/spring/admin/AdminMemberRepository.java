package com.kh.spring.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kh.spring.member.Member;

public interface AdminMemberRepository extends JpaRepository<Member, String>{

	List<Member> findByUserIdAndIsLeave(String userId,boolean isLeave);
	
}
