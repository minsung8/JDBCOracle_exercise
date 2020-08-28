package test06.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import test05.singleton.dbconnection.MyDBConnection;

public class MemberDAO implements InterMemberDAO {

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	// 자원반납 메소드
	public void close() { 
		
		try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	
// DB에 회원가입 메소드 
	@Override
	public int memberRegister(MemberDTO member) {
		
		int result = 0;
		
		try {
			
			conn = MyDBConnection.getConn();
			
			String sql = "insert into jdbc_member(userseq, userid, passwd, name, mobile)\n"+
					"values(userseq.nextval, ?, ?, ?, ?)";

			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getUserid());
			ps.setString(2, member.getPasswd());
			ps.setString(3, member.getName());
			ps.setString(4, member.getMobile());
			
			result = ps.executeUpdate();
			
		} catch (SQLIntegrityConstraintViolationException e1) {
			System.out.println("에러메시지 : " + e1.getMessage());
			System.out.println("에러코드번호 : " + e1.getErrorCode());
			System.out.println(">>> 아이디가  중복되었습니다. 새로운 아이디를 입력하세요! ");
		}  catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}


// 로그인 처리 메소드
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		
		MemberDTO member = null;
		
		try {
			conn = MyDBConnection.getConn();

			
			String sql = "select userseq, userid, passwd, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') as registerday, status\n " + 
					" from jdbc_member\n " + 
					" where userid = ? and passwd = ? ";

			ps = conn.prepareStatement(sql);
			ps.setString(1, paraMap.get("userid"));
			ps.setString(2, paraMap.get("passwd"));
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				member = new MemberDTO();
				member.setName(rs.getString("name"));
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			close();
		}
		
		return member; 
	}

}
