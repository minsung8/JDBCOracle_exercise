package test01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class DML_delete {

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
				conn.setAutoCommit(false);
				System.out.println("삭제할 글번호 : ");
				String md_no = sc.nextLine();
				
				String sql2 = " delete from jdbc_tbl_memo "
						+ " where no = ?";
				
				
				ps = conn.prepareStatement(sql2);
				ps.setString(1, md_no);
					
				// 행의 개수
				int n  = ps.executeUpdate();
				if (n == 1) {
					while (true) {
						System.out.println("정말로 삭제하시겠습니까?[Y/N]");
						String yn = sc.nextLine();
						
						if ("y".equalsIgnoreCase(yn)) {
							conn.commit();
							System.out.println("데이터 삭제 성공");
							break;
						}
						else if ("n".equalsIgnoreCase(yn)) {
							conn.rollback();
							System.out.println("데이터 삭제 취소");
							break;
						} else {
							System.out.println("다시 입력해주세요 ");
							continue;
						} 
					}
				} else {
					System.out.println("삭제할 글번호 " + md_no + "는 존재하지 않는 글번호 입니다.");
				}
				
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