package test02.member;

import java.util.Scanner;

public class MemberCtrl{
	
	MemberDAO mdao = new MemberDAO();

	// 시작메뉴
	public void menu_Start(Scanner sc) {
		String sChoice = null;
		do {
			
			System.out.println("\n ------- 시작메뉴 -----------\n");
			System.out.println("1.회원가입   2.로그인   3.프로그램 종료\n");
			System.out.println("-----------------------------\n");
			
			System.out.println("메뉴번호 선택 : ");
			sChoice = sc.nextLine();
			
			switch (sChoice) {
				case "1":
					memberRegister(sc);
					break;
				case "2":
					
					break;
				case "3":
					
					break;
	
				default:
					System.out.println("메뉴에 없는 번호입니다. 다시 선택하세요!");
					
					
					break;
			}
		} while (!("3".equals(sChoice))); 
		
	}

	// 회원가입
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
	
}
