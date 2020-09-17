package test06.board;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import my.util.MyUtil;
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
				boardList();
				break;
			case "2":			// 글내용보기
				viewContents(loginMember, sc);
				break;
			case "3":			// 글쓰기
				int n = write(loginMember, sc);
				if (n == 1) System.out.println(">> 글쓰기 성공!! <<");
				else if (n == -1) System.out.println(">> 글쓰기를 취소하셨습니다!! <<");
				else System.out.println(">> 장애가 발생하여 글쓰기가 실패되었습니다!! <<");
				break;
			case "4":			// 댓글쓰기
				n = writeComment(loginMember, sc);
				if (n == 1) System.out.println(">> 댓글쓰기 성공!! <<");
				else System.out.println(">> 댓글쓰기 실패!! <<");
				break;
			case "5":			// 글수정하기
				n = updateBoard(loginMember, sc);
				if (n==0) {
					System.out.println(">> 수정할 글번호가 글목록에 존재하지 않습니다. <<\n");
				}
				else if (n==1) System.out.println(">> 다른 사용자의 글은 수정 불가합니다! <<\n");
				else if (n==2) System.out.println(">> 글암호가 올바르지 않습니다! <<\n");
				else if (n==3) System.out.println(">> 글수정 실패! <<\n");
				else if (n==4) System.out.println(">> 글수정 취소! <<\n");
				else if (n==5) System.out.println(">> 글수정 성공! <<\n");

				break;
			case "6":			// 글삭제하기
				n = deleteBoard(loginMember, sc);
				break;
			case "7":			// 최근1주일간 일자별 게시글 작성건수
				weekcnt();
				break;
			case "8":			// 이번달 일자별 게시글 작성건수
				monthcnt();
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
	
	
	private void monthcnt() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");
		
		String currentMonth = dateFormat.format(currentDate.getTime());
		
		System.out.println(">>> ["+ currentMonth +" 일자별 게시글 작성건수 ] <<<");
		System.out.println("-----------------------------------------------");
		System.out.println(" 작성일자\t작성건수");
		System.out.println("-----------------------------------------------");
		
		List<Map<String, String>> list = bdao.monthcnt();
		
		if (list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			
			for (int i=0; i<list.size(); i++) {
				sb.append(list.get(i).get("WRITEDAY") + " ");
				sb.append(list.get(i).get("cnt") + "\n");
			}
			System.out.println(sb.toString());
										
		} else {
			System.out.println(" 작성된 게시글이 없습니다 ");
		}

	}

	private void weekcnt() {

		System.out.println("------------------[최근 1주일간 일자별 게시글 작성건수]---------------------");

		String result = "전체\t";
		for (int i=0; i<7; i++) {
			result += MyUtil.getDay(i - 6) + "\t";
		}
		
		System.out.println(result);
		
		Map<String, Integer> resultMap = bdao.weekcnt();
		System.out.println(resultMap.get("total") + "\t" + resultMap.get("previous6") + "\t" + resultMap.get("previous5") + "\t" 
				+ resultMap.get("previous4") + "\t" + resultMap.get("previous3") + "\t" + resultMap.get("previous2") + "\t"
				+ resultMap.get("previous1") + "\t" + resultMap.get("today"));
		System.out.println("------------------------------------------------------------------------");
	}
	private int deleteBoard(MemberDTO loginMember, Scanner sc) {
		int result = 0;

		System.out.println("\n>>> 글 삭제 하기 <<<");
		
		System.out.println("> 삭제할 글번호 : ");
		String boardNo = sc.nextLine();
	
		Map<String, String> paraMap =new HashMap<String, String>();
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		if (bdto != null) {
			if (bdto.getFk_userid().equals(loginMember.getUserid())) {
				System.out.println("> 글암호 : ");
				String boardPasswd = sc.nextLine();

				paraMap.put("boardPasswd", boardPasswd);
				
				bdto = bdao.viewContents(paraMap);
				
				System.out.println("글제목 : " + bdto.getSubject());
				System.out.println("글내용 : " + bdto.getContents());
				
				Connection conn = MyDBConnection.getConn();
				int n = bdao.deleteBoard(paraMap);
				if (n == 1) {
					do {
						
						System.out.print(">정말로 삭제하시겠습니까?[Y/N] :");
						String yn = sc.nextLine();
						try {
							if("y".equalsIgnoreCase(yn)) {
								System.out.println("삭제 성공");
								conn.commit();
								break;
							} else if("n".equalsIgnoreCase(yn)) {
								System.out.println("삭제 포기");
								conn.rollback();
								break;
							} else {
								System.out.println("> Y 또는 N만 입력하세요!! ");
							}
						} catch (SQLException e) {
							e.printStackTrace();
							break;
						}
						
					}while (true);
				} else {
					System.out.println("해당 글번호가 존재하지 않습니다");
				}
				
			} 
		} else {
			System.out.println("해당 글번호가 존재하지 않습니다!");
		}

		
		return 0;
	}

	private int updateBoard(MemberDTO loginMember, Scanner sc) {
		
		int result = 0;

		System.out.println("\n>>> 글 수정 하기 <<<");
		
		System.out.println("> 수정할 글번호 : ");
		String boardNo = sc.nextLine();
	
		Map<String, String> paraMap =new HashMap<String, String>();
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if (bdto != null) {
			
			if (bdto.getFk_userid().equals(loginMember.getUserid())) {
				System.out.println("> 글암호 : ");
				String boardPasswd = sc.nextLine();

				paraMap.put("boardPasswd", boardPasswd);
				
				bdto = bdao.viewContents(paraMap);
				
				if (bdto != null) {
					
					System.out.println("-----------------------");
					System.out.println("글제목 : " + bdto.getSubject());
					System.out.println("글내용 : " + bdto.getContents());
					System.out.println("-----------------------");
					
					System.out.println("> 글제목[변경하지 않으려면 엔터] : ");
					String subject = sc.nextLine();
					
					if (subject != null && subject.trim().isEmpty()) {
						subject = bdto.getSubject();
					}
					
					System.out.println("> 글내용[변경하지 않으려면 엔터] : ");
					String contents = sc.nextLine();
					
					if (contents != null && contents.trim().isEmpty()) {
						contents = bdto.getContents();
					}
					
					paraMap.put("subject", subject);
					paraMap.put("contents", contents);
					
					int n = bdao.updateBoard(paraMap);
					if (n == 1) {
						Connection conn = MyDBConnection.getConn();
						do {
							
							System.out.print(">정말로 수정하시겠습니까?[Y/N] :");
							String yn = sc.nextLine();
							try {
								if("y".equalsIgnoreCase(yn)) {
									conn.commit();
									result = 5;
									break;
								} else if("n".equalsIgnoreCase(yn)) {
									conn.rollback();
									result = 4;
									break;
								} else {
									System.out.println("> Y 또는 N만 입력하세요!! ");
								}
							} catch (SQLException e) {
								e.printStackTrace();
								break;
							}
							
						}while (true);
						
					} else {
						result = 3;
					}
					
					
				} else {
					result = 2;
				}
			} else {
				result = 1;
			}
			
		}
		
		return result;
	}

	private int writeComment(MemberDTO loginMember, Scanner sc) {

		int result = 0;
		
		System.out.println("\n>>> 댓글쓰기 <<<");
		System.out.println("1. 작성자명 : " + loginMember.getName());
		System.out.println("2. 원글의 글번호 :");
		String boardno = sc.nextLine();
		
		String contents = null;
		do {
			System.out.println("3. 댓글내용 : ");
			contents = sc.nextLine();
			
			if (contents == null || contents.trim().isEmpty()) {
				System.out.println(">> 댓글내용은 필수로 입력해야 합니다. <<");
			} else {
				break;
			}
		} while (true);
		
		BoardCommentDTO cmdto = new BoardCommentDTO();
		cmdto.setFk_boardno(boardno);
		cmdto.setFk_userid(loginMember.getUserid());
		cmdto.setContents(contents);
		
		result = bdao.writeComment(cmdto);
		
		
		return result;
	}

	private void viewContents(MemberDTO loginMember, Scanner sc) {
		
		System.out.println("\n >>> 글내용 보기 <<<");
		System.out.print("> 글번호 : ");
		String boardNo = sc.nextLine();
		Map<String, String> paraMap =new HashMap<String, String>();
		paraMap.put("boardNo", boardNo);
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if(bdto != null) {
			System.out.println("[글내용]" + bdto.getContents());
			
			if (!bdto.getFk_userid().equals(loginMember.getUserid())) {
				bdao.updateViewCount(boardNo);
				
			}
			
			System.out.println("[댓글]\n--------------------------------");
			
			List<BoardCommentDTO> commentList = bdao.commenList(boardNo);
			
			if (commentList != null) {
				System.out.println("댓글내용\t작성자\t작성일자");
				System.out.println("---------------------------------------");
				StringBuilder sb = new StringBuilder();
				
				for (BoardCommentDTO comment : commentList) {
					sb.append(comment.commentInfo() + "\n");
				}
				
				System.out.println(sb.toString());
			}
			else {
				System.out.println(">> 댓글 내용 없음 <<\n");
			}
			
		} else {
			System.out.println(">> 글번호 " + boardNo + "은 글목록에 존재하지 않습니다 \n");
		}


	}

	private void boardList() {
		List<BoardDTO> boardList = bdao.boardList(); 
		
		StringBuilder sb = new StringBuilder();
		
		if ( boardList.size() > 0) {
			
			for (int i=0; i<boardList.size(); i++) {
				sb.append(boardList.get(i).listInfo() + "\n");
			}
			
			System.out.println("\n---------------------------[게시글 목록]-------------");
			System.out.println("글번호\t글제목\t\t작성자\t작성일자\t조회수");
			System.out.println("----------------------------------------------------");
			System.out.println(sb.toString());
			
		} else {
			System.out.println(">>> 글목록이 없습니다 <<<\n");
		}
	}

	
	private int write(MemberDTO loginMember, Scanner sc) {
		
		int result = 0;
		
		System.out.println("\n>>> 글쓰기 <<<");
		System.out.println("1. 작성자명 : " + loginMember.getName());
		
		System.out.print("2. 글제목 : ");
		String subject = sc.nextLine();
		
		System.out.print("3. 글내용 : ");
		String contents = sc.nextLine();
		
		System.out.print("4. 글암호 : ");
		String boardpasswd = sc.nextLine();
		
		BoardDTO bdto = new BoardDTO();
		bdto.setFk_userid(loginMember.getUserid());
		bdto.setSubject(subject);
		bdto.setContents(contents);
		bdto.setBoardpass(boardpasswd);
		
		int n1 = bdao.write(bdto);
		int n2 = mdao.updateMemberPoint(loginMember);		// 회원테이블에서 글을 작성한 회원의 point를 10 증가하는 update

		Connection conn = MyDBConnection.getConn();
		
		if (n1 == 1 && n2 == 1) {
			do {
				System.out.println(">> 정말로 글쓰기를 하시겠습니까?[Y/N] => ");
				String yn = sc.nextLine();
				
				try {
					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();
						result = 1;
						break;
					} else if ("n".equalsIgnoreCase(yn)) {
						conn.rollback();
						result = -1;		// 사용자가 글쓰기를 취소한 경우
						break;
					} else {
						System.out.println(">> Y 또는 N만 입력하세요!! \n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} while (true);
		} else {
			try {
				conn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;

	}

	// Connection 자원 반납
	private void appExit() {
		MyDBConnection.closeConnection();
	}

}