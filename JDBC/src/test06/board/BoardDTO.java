package test06.board;

public class BoardDTO {

	private int boardno;
	private String fk_userid;
	private String subject;
	private String contents;
	private String writeday;
	private int viewcount;
	private String boardpass;
	
	private MemberDTO member;
	
	private int commentcnt;
	
	public int getBoardno() {
		return boardno;
	}
	public void setBoardno(int boardno) {
		this.boardno = boardno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public String getBoardpass() {
		return boardpass;
	}
	public void setBoardpass(String boardpass) {
		this.boardpass = boardpass;
	}
	public MemberDTO getMember() {
		return member;
	}
	public void setMember(MemberDTO member) {
		this.member = member;
	}
	
	public String listInfo() {
		String listInfo;
		if (commentcnt != 0) {
			listInfo = boardno + "\t" + subject + "["+ commentcnt +"]\t" + member.getName() + "\t" + writeday + "\t" + viewcount + "\t";
		} else {
			listInfo = boardno + "\t" + subject + "\t" + member.getName() + "\t" + writeday + "\t" + viewcount + "\t";
		}
		return listInfo;
	}
	public int getCommentcnt() {
		return commentcnt;
	}
	public void setCommentcnt(int commentcnt) {
		this.commentcnt = commentcnt;
	}
	
	
}
