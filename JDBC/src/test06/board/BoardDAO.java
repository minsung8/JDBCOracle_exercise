package test06.board;

import java.sql.*;

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

}
