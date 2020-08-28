package test06.board;

import java.util.Map;
import java.util.Scanner;

public interface InterMemberDAO {

	// 회원가입 메소드
	int memberRegister(MemberDTO member, Scanner sc);
	
	// 로그인 처리 메소드
	MemberDTO login(Map<String, String> paraMap);
}
