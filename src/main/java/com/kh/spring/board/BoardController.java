package com.kh.spring.board;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.member.Member;
import com.kh.spring.member.MemberAccount;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final BoardService boardService;
	
	@GetMapping("board-form")
	public void boardForm() {};
	
	@PostMapping("upload")
	public String uploadBoard(
				@RequestParam List<MultipartFile> fileList
				,@AuthenticationPrincipal MemberAccount memberAccount //Member가 아닌 MemberAccount
				, Board board) {
		
		board.setMember(memberAccount.getMember()); //memberAccount에서 꺼내옴
		boardService.persistBoard(fileList, board);
		return "redirect:/";
	}
	
	@GetMapping("board-detail")
	public void boardDetail(Model model, Long bdIdx) {
		Board board = boardService.findBoardByIdx(bdIdx);
		model.addAttribute("board", board);
	}
	
	
	@GetMapping("board-list")
	public void boardList(Model model, 
						@RequestParam(required = false, defaultValue = "1") int page) {
		
		 model.addAllAttributes(boardService.findBoardsByPage(page));
	}

	
	@GetMapping("board-modify")
	public void boardmodify(Model model, long bdIdx) {
		model.addAttribute("board",boardService.findBoardByIdx(bdIdx));
	}
	
	
	@PostMapping("modify")
	public String modifyBoard(Board board,
					@RequestParam List<MultipartFile> fileList,
					@RequestParam(required=false) //삭제할수도 안할수도있어서 required
					Optional<List<Long>> removeFlIdx) {
					//removeFlIdx가 null이면 서비스에서 처리못하니까 optional써줌
		boardService.modifyBoard(board,fileList,removeFlIdx.orElse(List.of()));
		
		return "redirect:/board/board-detail?bdIdx="+board.getBdIdx();
	}
	
	
	
	
	

}
