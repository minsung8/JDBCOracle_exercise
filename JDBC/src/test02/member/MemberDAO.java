package test02.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MemberDAO implements InterMemberDAO{

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	// 자원반납 메소드
	public void close() {
		
		try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// DB에 회원가입 메소드 
	@Override
	public int memberRegister(MemberDTO member) {
		
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "MYORAUSER", "cclass");
			
			String sql = "insert into jdbc_member(userseq, userid, passwd, name, mobile)\n"+
					"values(userseq.nextval, ?, ?, ?, ?)";

			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getUserid());
			ps.setString(2, member.getPasswd());
			ps.setString(3, member.getName());
			ps.setString(4, member.getMobile());
			
			result = ps.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar 파일이 없습니다.");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

}
