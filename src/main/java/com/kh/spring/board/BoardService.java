package com.kh.spring.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandlableException;
import com.kh.spring.common.util.file.FileInfo;
import com.kh.spring.common.util.file.FileUtil;
import com.kh.spring.common.util.pagination.Paging;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService{
	
	private final BoardRepository boardRepository;
	
	@Transactional
	public void persistBoard(List<MultipartFile> multiparts, Board board) {
		
		List<FileInfo> fileInfos = new ArrayList<>();
		FileUtil fileUtil = new FileUtil();
		
		for (MultipartFile multipartFile : multiparts) {
			if(!multipartFile.isEmpty()) {
			fileInfos.add(fileUtil.fileUpload(multipartFile));
			}
		}
		
		board.setFiles(fileInfos);
		boardRepository.save(board);
	}

	public Board findBoardByIdx(Long bdIdx) {
		return boardRepository.findById(bdIdx)
				.orElseThrow(() -> new HandlableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR));
	}

	public Map<String, Object> findBoardsByPage(int pageNumber) {
		
		int cntPerPage = 5;
		Page<Board> page = boardRepository.findAll(PageRequest.of(pageNumber-1, cntPerPage, Direction.DESC, "bdIdx"));
		List<Board> boards = page.getContent();
		Paging pageUtil = Paging.builder()
					.url("/board/board-list")
					.total((int)boardRepository.count())
					.cntPerPage(cntPerPage)
					.blockCnt(10)
					.curPage(pageNumber)
					.build();
		
		return Map.of("boardList", boards, "paging", pageUtil);
	}

	@Transactional
	public void modifyBoard(Board board, List<MultipartFile> fileList, List<Long> removeFlIdx) {
		
		//db에서 삭제
		Board boardEntity = boardRepository //의도적으로 bdidx잘못넘길경우
				.findById(board.getBdIdx()).orElseThrow(()->new HandlableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR));
		
		boardEntity.setTitle(board.getTitle()); //제목수정
		boardEntity.setContent(board.getContent());//내용수정
		
		FileUtil util = new FileUtil();
		//bdIdx포함이되어있으면 지워줌
						//removeIf를 Predicate<>로 반환해줌 (match에서 햇음)
		boardEntity.getFiles().removeIf(e ->{
			if(removeFlIdx.contains(e.getFlIdx())) {
				e.setIsDel(true); //삭제됫다고설정
				util.removeFile(e.getDownloadPath());
				return true;
			}
			return false;
		}); 
		
		//파일추가시
		fileList.forEach(e->{
			if(!e.isEmpty()) { //비어있지않을때
				FileInfo file = util.fileUpload(e); //업로드해줌
				boardEntity.getFiles().add(file);
			}
		});
		
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
}
