package test01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DML_insert {
			
	public static void main(String[] args) {
		
		// Connection는 오라클 DB와 연결 해주는 객체
		Connection conn = null;
		
		// Connection conn(특정 오라클서버)에 전송할 SQL문을 전송하는 객체
		PreparedStatement ps = null; 
		
		Scanner sc = new Scanner(System.in);
		
		try {
		/* 1. oracle 드라이버 로딩
			- OracleDriver(오라클 드라이버) 역할 
				1) OracleDriver를 메모리에 로딩
				2) OracleDriver 객체를 생성
				3) OracleDriver 객체를 DriverManager에 등록시켜준다 => DriverManager : 여러 드라이버들을 Vector에 저장하여 관리해주는 클래스
			
			2. 어떤 oracle 서버에 연결할지 결정
			
			3. SQL문 작성
				
			4. 연결한 오라클 서버(conn)에 SQL문 전달할 PreparedStatement 객체 생성
			
			5. PreparedStatement ps 객체가 SQL문을 오라클서버에 보내서 실행
			 1) executeupdate => DML
			 2) executequery => select
			
			6. 자원 반납 (생성 순서의 반대)
		*/	
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.println("연결할 오라클 서버의 IP 주소 : ");
			String ip = sc.nextLine();
			
			// conn의 기본값은 auto commit => rollback 불가능
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + ip + ":1521:xe", "HR", "cclass");
			
			// conn을 수동 commit으로 변경
			// conn.setAutoCommit(false);
			
			System.out.println("글쓴이 : ");
			String name = sc.nextLine();
			System.out.println("내용 : ");
			String msg = sc.nextLine();
			
			// ? : 위치홀더
			String sql = "insert into jdbc_tbl_memo(no, name, msg) values (jdbc_seq_memo.nextval, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			
			
			// ? 위치에 값 mapping
			ps.setString(1, name);
			ps.setString(2, msg);
			
			// 행의 개수
			int n  = ps.executeUpdate();
			
			if (n == 1) {
				while (true) {
					System.out.println("정말로 입력하시겠습니까?[Y/N]");
					String yn = sc.nextLine();
					
					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println("데이터 입력 성공");
						break;
					}
					else if ("n".equalsIgnoreCase(yn)) {
						conn.rollback();
						System.out.println("데이터 입력 취소");
						break;
					} else {
						System.out.println("다시 입력해주세요 ");
						continue;
					}
				}
			} else {
				System.out.println("데이터 입력의 오류가 발생");
			}
			 
			
		} catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar 파일이 없습니다");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
			if (ps != null) ps.close(); 
			if (conn != null) conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		sc.close();
		System.out.println("프로그램 종료");
	}
}