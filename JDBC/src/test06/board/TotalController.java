package test06.board;

import java.util.*;

import test05.singleton.dbconnection.MyDBConnection;

public class TotalController {
	
	// DAO(Data Access Object) => DB 연결하여 관련된 업무(DML, DDL, DQL)를 처리해주는 객체
	InterMemberDAO mdao = new MemberDAO();
	InterBoardDAO bdao = new BoardDAO();
	
	public void menu_Start(Scanner sc) {
		String sChoice = null;
		MemberDTO member = null;
		do {
			 
			if (member != null) {
				System.out.println("\n ------- 시작메뉴[" + member.getName() + " 로그인중..]-----------\n");
				System.out.println("1.회원가입   2.로그아웃   3.프로그램 종료\n");
			} else {
				System.out.println("\n ------- 시작메뉴 -----------\n");
				System.out.println("1.회원가입   2.로그인   3.프로그램 종료\n");
			}
			System.out.println("-----------------------------\n");
			
			System.out.println("메뉴번호 선택 : ");
			sChoice = sc.nextLine();
			
			switch (sChoice) {
				case "1":
					memberRegister(sc);
					break;
				case "2":
					if (member == null) member = login(sc);		// 로그인 시도하기
					else member = null;
					break;
				case "3":	// 프로그램 종료  => Connection 객체 자원반납
					appExit();
					break;
	
				default:
					System.out.println("메뉴에 없는 번호입니다. 다시 선택하세요!");
					
					
					break;
			}
		} while (!("3".equals(sChoice))); 
		
	}
	
	
	private void memberRegister(Scanner sc) {

		System.out.println("\n>>> --- 회원가입 --- <<<");
		
		System.out.print("1. 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print("2. 암호 : ");
		String passwd = sc.nextLine();
		
		System.out.print("3. 회원명 : ");
		String name = sc.nextLine();
		
		System.out.print("4. 연락처(휴대폰) : ");
		String mobile = sc.nextLine();
		
		MemberDTO member = new MemberDTO();
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		int n = mdao.memberRegister(member);
		
		if (n==1) {
			System.out.println("회원가입을 축하드립니다.");
		} else {
			System.out.println("회원가입 실패 !");
		}
		
	}
	
	
	// 로그인 처리 
	private MemberDTO login(Scanner sc) {
		
		MemberDTO member = null;
		
		System.out.println(" >>> --- 로그인 --- <<< ");
		
		System.out.print("아이디 : ");
		String userid = sc.nextLine();
		
		System.out.println("암호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		member = mdao.login(paraMap);
		
		if (member != null) System.out.println(" >>> 로그인 성공 <<<");
		else System.out.println(" 로그인 실패 ");
		
		return member;
	}
	
	// Connection 자원 반납
	private void appExit() {
		MyDBConnection.closeConnection();
	}

	
	

}
