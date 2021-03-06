package test06.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Scanner;

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
	public int memberRegister(MemberDTO member, Scanner sc) {
		
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
			
			if (result == 1){
				String yn = "";
				do {
					System.out.print(">> 회원가입을 정말로 하시겠습니까?[Y/N] ");
					yn = sc.nextLine();
					
					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();		// 커밋
					}
					else if ("n".equalsIgnoreCase(yn)) {
						conn.rollback();	// 롤백
						result = 0;
					}
					else {
						System.out.println(">>> Y 또는 N만 입력하세요! \n");
					}
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
		}

			
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
				member.setUserid(rs.getString("userid"));
				
				member.setMobile(rs.getString("mobile"));
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			close();
		}
		
		return member; 
	}

	@Override
	public int updateMemberPoint(MemberDTO member) {
		int result = 0;
		
		try {
			
			conn = MyDBConnection.getConn();
			
			String sql = " update jdbc_member set point = point + 10 \n " +
			" where userid = ? ";

			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getUserid());
		
			result = ps.executeUpdate();
		} catch(Exception e) {
			System.out.println(member.getName() + "님의 포인트는 현재 20이라서 30으로 증가가 불가합니다.");
		} finally {
			close();
		}
		
		return result;
	}
	
	

}
