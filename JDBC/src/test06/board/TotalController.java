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
					if (member == null) {
						member = login(sc);		// 로그인 시도하기
						if (member != null) {
							menu_Board(member, sc);
						}

					}
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
		
		int n = mdao.memberRegister(member, sc);
		
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
	
	public void menu_Board(MemberDTO loginMember, Scanner sc) {
		
		String adminMenu = ("admin".equalsIgnoreCase(loginMember.getUserid()))?"10.모든회원정보조회":"";
		System.out.println(loginMember.getUserid());
		String menuNo = "";
		do {
			System.out.println("\n---------- 게시판메뉴[" + loginMember.getName() + "님 로그인중..]------------\n"
					+ " 1.글목록보기	2.글내용보기	3.글쓰기	4.댓글쓰기\n"
					+ " 5.글수정하기	6.글삭제하기	7.최근1주일간 일자별 게시글 작성건수\n"
					+ " 8.이번달 일자별 게시글 작성건수	9.나가기  " + adminMenu + "\n"
					+ "-----------------------------------------------------------------------------------"
					);
			System.out.print("메뉴번호 선택 : ");
			menuNo = sc.nextLine();
			
			switch (menuNo) {
			case "1":			// 글목록보기
				
				break;
			case "2":			// 글내용보기
				
				break;
			case "3":			// 글쓰기
				
				break;
			case "4":			// 댓글쓰기
				
				break;
			case "5":			// 글수정하기
				
				break;
			case "6":			// 글삭제하기
				
				break;
			case "7":			// 최근1주일간 일자별 게시글 작성건수
				
				break;
			case "8":			// 이번달 일자별 게시글 작성건수
				
				break;
			case "9":			// 나가기
				
				break;
			case "10":			// 모든회원정보조회(관리자 전용 메뉴)
				
				if ( "admin".equals(loginMember.getUserid()) ) {
					
				} else {
					System.out.println(">> 메뉴에 없는 번호 입니다. << \n");
				}
				
				break;
				
			default:
				System.out.println(">> 메뉴에 없는 번호 입니다. << \n");
				break;
			}
		} while (!"9".equals(menuNo));
		
	}
	
	// Connection 자원 반납
	private void appExit() {
		MyDBConnection.closeConnection();
	}

}
