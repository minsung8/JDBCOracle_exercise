package test06.board;

import java.sql.*;
import java.util.*;

import test05.singleton.dbconnection.MyDBConnection;

public class BoardDAO implements InterBoardDAO {

	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private void close() {
		try {
			if (rs != null); rs.close();
			if (ps != null); ps.close();			
			
		} catch (Exception e) {

		}
	}
	
	@Override
	public int write(BoardDTO bdto) {

		int result = 0;
		
		try {
			
			conn = MyDBConnection.getConn();
			String sql = " insert into jdbc_board(boardno, fk_userid, subject, contents, boardpasswd) \n " + 
			" values(board_seq.nextval, ?, ?, ?, ?) ";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, bdto.getFk_userid());
			ps.setString(2, bdto.getSubject());
			ps.setString(3, bdto.getContents());
			ps.setString(4, bdto.getBoardpass());
			
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	@Override
	public List<BoardDTO> boardList() {
		
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();
		
		try {
			
			conn = MyDBConnection.getConn();
			
			String sql = "select B.boardno, B.subject, \n"+
					"M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
					", B.viewcount, nvl(C.commentcount, 0)\n"+
					"from jdbc_board B join jdbc_member M\n"+
					"on B.fk_userid = M.userid\n"+
					"left join\n"+
					"(\n"+
					"select fk_boardno, count(*) as commentcount\n"+
					"from jdbc_comment\n"+
					"group by fk_boardno\n"+
					") C\n"+
					"on B.boardno = C.fk_boardno";
					
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				BoardDTO bdto = new BoardDTO();
				bdto.setBoardno(rs.getInt(1));
				if (rs.getString(2).length() > 10) {
					String temp = rs.getString(2).substring(0, 10) + "..";
					bdto.setSubject(temp);
				} else {
					bdto.setSubject(rs.getString(2));
				}
				
				MemberDTO member = new MemberDTO();
				member.setName(rs.getString(3));
				bdto.setMember(member);
				bdto.setWriteday(rs.getString(4));
				bdto.setViewcount(rs.getInt(5));
				bdto.setCommentcnt(rs.getInt(6));
				
				boardList.add(bdto);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	@Override
	public BoardDTO viewContents(Map<String, String> paraMap) {
		BoardDTO bdto = null;
		try {
			conn = MyDBConnection.getConn();

			String sql = " select contents, fk_userid, subject "+
					" from jdbc_board ";
			
			if (paraMap.get("boardPasswd") != null) {
				sql += " where boardno = ? and boardpasswd = ? ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, paraMap.get("boardNo"));
				ps.setString(2, paraMap.get("boardPasswd"));
			} else {
				sql += " where boardno = ? ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, paraMap.get("boardNo"));
			}
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bdto = new BoardDTO();
				bdto.setContents(rs.getString(1));
				bdto.setFk_userid(rs.getString(2));
				bdto.setSubject(rs.getString(3));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return bdto;
	}

	@Override
	public void updateViewCount(String boardNo) {
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = " update jdbc_board set viewcount = viewcount + 1 where boardno = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, boardNo);

			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	@Override
	public int writeComment(BoardCommentDTO cmdto) {

		int n = 0;
		
		try {
			
			conn = MyDBConnection.getConn();
			
			String sql = " insert into jdbc_comment (commentno, fk_boardno, fk_userid, contents) "
					+ "values (seq_comment.nextval, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, cmdto.getFk_boardno());
			ps.setString(2, cmdto.getFk_userid());
			ps.setString(3, cmdto.getContents());
			
			n = ps.executeUpdate();
			
			if (n == 1) conn.commit();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(">>> 원글번호" + cmdto.getFk_boardno() + "은 존재하지 않습니다 <<<");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return n;
	}

	@Override
	public List<BoardCommentDTO> commenList(String boardNo) {
		List<BoardCommentDTO> commentList = null;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "select C.contents, M.name, to_char(C.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
					"from jdbc_comment C join jdbc_member M\n"+
					"on C.fk_userid = M.userid\n"+
					"where C.fk_boardno = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, boardNo);
			
			rs = ps.executeQuery();
			
			int cnt = 0;
			if(rs.next()) {
				BoardCommentDTO cmdto = new BoardCommentDTO();
				cmdto.setContents(rs.getString(1));
				
				MemberDTO member = new MemberDTO();
				member.setName(rs.getString(2));
				
				cmdto.setMember(member);
				
				cmdto.setWriteday(rs.getString(3));
				
				cnt += 1;
				if (cnt == 1) commentList = new ArrayList<>();
				commentList.add(cmdto);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return commentList;
	}

	@Override
	public int updateBoard(Map<String, String> paraMap) {
		
		int result = 0;
		try {
			conn = MyDBConnection.getConn();
			
			String sql = " update jdbc_board set subject = ?, contents = ? where boardno = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, paraMap.get("subject"));
			ps.setString(2, paraMap.get("contents"));
			ps.setString(3, paraMap.get("boardNo"));

			ps.executeUpdate();
			conn.commit();
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}	
		return result;
	}
}