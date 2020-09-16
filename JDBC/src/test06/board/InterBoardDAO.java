package test06.board;

import java.util.List;
import java.util.Map;

public interface InterBoardDAO {
	
	int write(BoardDTO bdto);
	
	List<BoardDTO> boardList(); 
	
	BoardDTO viewContents(Map<String, String> paraMap);
	
	void updateViewCount(String boardNo);
	
	int writeComment(BoardCommentDTO cmdto);
	
	List<BoardCommentDTO> commenList(String boardNo);
	
	int updateBoard(Map<String, String> paraMap);



}