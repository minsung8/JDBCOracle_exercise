package test05.singleton.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import test04.singletonPattern.SingletonNumber;

public class MyDBConnection {
	
	private static Connection conn = null;
	
	// singleton 구현
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "MYORAUSER", "cclass");
			conn.setAutoCommit(false);
			
		}catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar 파일이 없습니다.");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
	}
	
	private MyDBConnection() {}
	
	public static Connection getConn() {
		return conn;
	}
	
	// 자원반납 
	public static void closeConnection() {
		try {
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
