package test01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class DQL_select {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		Connection conn = null;
		PreparedStatement ps = null; 
		ResultSet rs = null;
		
		try {

				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				System.out.println("연결할 오라클 서버의 IP 주소 : ");
				String ip = sc.nextLine();
				
				conn = DriverManager.getConnection("jdbc:oracle:thin:@" + ip + ":1521:xe", "HR", "cclass");
				
				String sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday "								// 쿼리문은 앞뒤로 공백 주기 (에러예방)
						+ " from jdbc_tbl_memo "
						+ " order by no desc ";
				
				ps = conn.prepareStatement(sql);
				
				rs = ps.executeQuery();
				
				System.out.println("--------------------------------------------------------");
				System.out.println("글번호\t글쓴이\t글내용\t작성일자");
				System.out.println("--------------------------------------------------------");
				
				StringBuilder sb = new StringBuilder();
				
				// rs.next()는 select 되어진 결과물에서 커서의 위치를 다음으로 옮긴 후 행이 존재하면 True, 없다면 False를 반환
				while (rs.next()) {
					int no = rs.getInt(1);		// 컬럼의 위치값으로 값 받기       or   re.get("no") => 컬럼명으로 받기 
					String name = rs.getString(2);
					String msg = rs.getString(3);
					String writeday = rs.getString("writeday");		// alias 필요
					
					sb.append(no);
					sb.append("\t" + name);
					sb.append("\t" + msg);
					sb.append("\t" + writeday);
					sb.append("\n");
				}
				
				System.out.println(sb.toString());
				 
				
			} catch (ClassNotFoundException e) {
				System.out.println("ojdbc6.jar 파일이 없습니다");
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				try {
				if (rs != null) rs.close();
				if (ps != null) ps.close(); 
				if (conn != null) conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("프로그램 종료");
			sc.close();
	
		}
	}


//pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 
// 그 타입은 ResultSet 으로 가져온다. 
/*
 *  === 인터페이스 ResultSet 의 주요한 메소드 === 
 * 1. next() : select 되어진 결과물에서 커서를 다음으로 옮겨주는 것 리턴타입은 boolean 
 * 2. first() : select 되어진 결과물에서 커서를 가장 처음으로 옮겨주는 것 리턴타입은 boolean 
 * 3. last() : select 되어진 결과물에서 커서를 가장 마지막으로 옮겨주는 것 리턴타입은 boolean
 *  == 커서가 위치한 행에서 컬럼의 값을 읽어들이는 메소드 ==
 *   getInt(숫자) : 컬럼의 타입이 숫자이면서 정수로 읽어들이때 파라미터 숫자는 컬럼의 위치값
 *   getInt(문자) : 컬럼의 타입이 숫자이면서 정수로 읽어들이때 파라미터 문자는 컬럼명 또는 alias명
 *   getLong(숫자) : 컬럼의 타입이 숫자이면서 정수로 읽어들이때 파라미터 숫자는 컬럼의 위치값 
 *   getLong(문자) : 컬럼의 타입이 숫자이면서 정수로 읽어들이때 파라미터 문자는 컬럼명 또는 alias명
 *   getFloat(숫자) : 컬럼의 타입이 숫자이면서 실수로 읽어들이때 파라미터 숫자는 컬럼의 위치값
 *   getFloat(문자) : 컬럼의 타입이 숫자이면서 실수로 읽어들이때 파라미터 문자는 컬럼명 또는 alias명
 *   getDouble(숫자) : 컬럼의 타입이 숫자이면서 실수로 읽어들이때 파라미터 숫자는 컬럼의 위치값
 *   getDouble(문자) : 컬럼의 타입이 숫자이면서 실수로 읽어들이때 파라미터 문자는 컬럼명 또는 alias명
 *   getString(숫자) : 컬럼의 타입이 문자열로 읽어들이때 파라미터 숫자는 컬럼의 위치값
 *   getString(문자) : 컬럼의 타입이 문자열로 읽어들이때 파라미터 문자는 컬럼명 또는 alias명 
 * */
 
/*
	StringBuffer => multi Thread 사용
	StringBuilder => single Thread 사용 (좀 더 가벼움)
*/