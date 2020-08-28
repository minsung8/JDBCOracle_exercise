show user;

drop table jdbc_member purge;
create table jdbc_member (userseq number not null -- 회원번호
,userid varchar2(30) not null -- 회원아이디 ,passwd varchar2(30) not null -- 회원암호
,passwd varchar2(20)
,name varchar2(20) not null -- 회원명 ,mobile varchar2(20) -- 연락처
,mobile varchar2(20)
,point number(10) default 0 -- 포인트 ,registerday date default sysdate -- 가입일자
,registerday date default sysdate
,status number(1) default 1 -- status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴
,constraint PK_jdbc_member primary key(userseq)
,constraint UQ_jdbc_member unique(userid)
,constraint CK_jdbc_member check( status in(0,1) )
,constraint ck_jdbc_member_point check ( point < 30 )
);

drop sequence userseq;

-- sequence 생성
create sequence userseq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select * 
from jdbc_member
order by userseq desc;

select userseq, userid, passwd, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') as registerday, status
from jdbc_member
where userid = '1234' and passwd = '1234';


------ /// 게시판 테이블 생성하기 /// ------ 
create table jdbc_board (boardno number not null -- 글번호 
,fk_userid varchar2(30) not null -- 작성자아이디
,subject varchar2(100) not null -- 글제목
,contents varchar2(200) not null -- 글내용
,writeday date default sysdate not null -- 작성일자
,viewcount number default 0 not null -- 조회수
,boardpasswd varchar2(20) not null -- 글암호
,constraint PK_jdbc_board primary key(boardno)
,constraint FK_jdbc_board foreign key(fk_userid) references jdbc_member(userid)
);

create sequence board_seq start with 1 increment by 1 nomaxvalue nominvalue nocycle nocache;

create table jdbc_comment (commentno number not null -- 댓글번호
,fk_boardno number not null -- 원글의 글번호
,fk_userid varchar2(30) not null -- 사용자ID
,contents varchar2(200) not null -- 댓글내용
,writeday date default sysdate -- 작성일자
,constraint PK_jdbc_comment primary key(commentno)
,constraint FK_jdbc_comment_fk_boardno foreign key(fk_boardno) references jdbc_board(boardno) on delete cascade
,constraint FK_jdbc_comment_fk_userid foreign key(fk_userid) references jdbc_member(userid)
);

create sequence seq_comment start with 1 increment by 1 nomaxvalue nominvalue nocycle nocache;

delete from jdbc_member;