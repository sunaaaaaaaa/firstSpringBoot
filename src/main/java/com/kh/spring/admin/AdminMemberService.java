package com.kh.spring.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kh.spring.common.util.pagination.Paging;
import com.kh.spring.member.Member;

@Service
public class AdminMemberService {
	
	@Autowired
	private AdminMemberRepository adminMemberRepository;
	
	public Map<String,Object> findAllMembers(int page){
		//탈퇴안한 회원들만 조회
		int cntPerPage =1; //한페이지에 한명씩
		List<Member> members =adminMemberRepository
				.findAll(PageRequest.of(page-1, cntPerPage,Direction.DESC,"userId"))
				.getContent();
		
		Paging paging =Paging.builder()
				.url("/admin/member/member-list")
				.total((int)adminMemberRepository.count())
				.cntPerPage(cntPerPage)
				.curPage(page)
				.blockCnt(2)
				.build();
		
		return Map.of("members",members,"paging",paging);
	}
	
	
	
	
	
	
	

}
