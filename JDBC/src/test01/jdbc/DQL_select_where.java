package test01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DQL_select_where {

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
				
				StringBuilder sb2 = new StringBuilder();
				sb2.append(" >>> 조회할 대상 <<< \n");
				sb2.append(" 1.글번호\t2.글쓴이\t3.글내용\t4.종료 \n");
				sb2.append("-----------------------------------\n");
				String menu = sb2.toString();
				
				String menuNo = "";
				do  {
					System.out.println(menu);
					System.out.print("선택번호 : ");
					menuNo = sc.nextLine();
					
					String colName = "";		// where 절에 들어올 컬럼명
					
					switch (menuNo) {
						case "1":
							colName = "no";
							break;
						case "2":
							colName = "name";
							break;
						case "3":
							colName = "msg";
							break;
						case "4":
							break;
						default:
							System.out.println("메뉴에 없는 번호 입니다. \n");
							break;
						}
					if ("1".equals(menuNo) || "2".equals(menuNo) || "3".equals(menuNo)) {
						System.out.println("검색어 : ");
						String search = sc.nextLine();
						
						sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday "								// 쿼리문은 앞뒤로 공백 주기 (에러예방)
								+ " from jdbc_tbl_memo ";
						
						if (!"3".equals(menuNo)) {
							sql += " where " + colName +  " = ? ";
						} else {
							sql += " where " + colName + " like '%'|| ? ||'%' ";
						}
						
						sql += " order by no desc ";
						
						ps = conn.prepareStatement(sql);
						
						ps.setString(1, search);
						
						rs = ps.executeQuery();
						
						System.out.println("--------------------------------------------------------");
						System.out.println("글번호\t글쓴이\t글내용\t작성일자");
						System.out.println("--------------------------------------------------------");
						
						// stringbuilder 초기화
						sb.delete(0, sb.length());
						// or
						sb.setLength(0);
						
						
						while (rs.next()) {
							
							int no = rs.getInt(1);
							String name = rs.getString(2);
							String msg = rs.getString(3);
							String writeday = rs.getString(4);
							
							sb.append(no);
							sb.append("\t" + name);
							sb.append("\t" + msg);
							sb.append("\t" + writeday);
							sb.append("\n");
							
						}
						System.out.println(sb.toString());

					}
					
				} while (!("4".equals(menuNo)));
				
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


