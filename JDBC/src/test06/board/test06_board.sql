show user;
select * from jdbc_member;
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

delete from jdbc_board;

select * from jdbc_board;
select * from jdbc_member;

select B.boardno
    , case when length(B.subject) > 10 then substr(B.subject, 1, 10) || '..' else B.subject end
    , M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday, B.viewcount
from jdbc_board B join jdbc_member M
on B.fk_userid = M.userid
order by 1 desc;


String sql = "select B.boardno\n"+
"    , case when length(B.subject) > 10 then substr(B.subject, 1, 10) || '..' else B.subject end\n"+
"    , M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday, B.viewcount\n"+
"from jdbc_board B join jdbc_member M\n"+
"on B.fk_userid = M.userid\n"+
"order by 1 desc";

select *
from jdbc_board
where boardno = 12;

String sql = "select contents, fk_userid\n"+
"from jdbc_board\n"+
"where boardno = ?;";

String sql = "select contents, fk_user  id from jdbc_board where boardno = 12;";

select * from jdbc_comment;


select *
from jdbc_comment C right join jdbc_board B
on C.fk_boardno = B.boardno;


select B.boardno, 
 B.subject, 
 M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday, B.viewcount
from jdbc_board B join jdbc_member M
on B.fk_userid = M.userid
order by 1 desc;

String sql = "select B.boardno, B.subject, \n"+
"M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
", B.viewcount, nvl(C.commentcount, 0)\n"+
"from jdbc_board B join jdbc_member M\n"+
"on B.fk_userid = M.userid\n"+
"left join\n"+
"(\n"+
"select fk_boardno, count(*) as commentcount\n"+
"from jdbc_comment\n"+
"group by fk_boardno\n"+
") C\n"+
"on B.boardno = C.fk_boardno";

select * 
from jdbc_comment
where fk_boardno = 7;

String sql = "select C.contents, M.name, to_char(C.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
"from jdbc_comment C join jdbc_member M\n"+
"on C.fk_userid = M.userid\n"+
"where C.fk_boardno = 1";

select * from jdbc_board
order by boardno desc;

update jdbc_board set writeday = writeday - 1
where boardno = 15;

update jdbc_board set writeday = writeday - 3
where boardno in (14, 13);

update jdbc_board set writeday = writeday - 10
where boardno = 12;



select boardno as 게시글번호
    , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as 작성일자시분초
    ,to_char(to_date( to_char(writeday, 'yyyy-mm-dd'), 'yyyy-mm-dd'), 'yyyy-mm-dd hh24:mi:ss') as 작성일자자정
from jdbc_board
order by boardno desc;

create or replace function func_midnight
(p_date in date)
return date
is
begin
   return to_date( to_char(p_date, 'yyyy-mm-dd'), 'yyyy-mm-dd');
end func_midnight;
    
select boardno as 게시글번호 
    , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as 작성일자시분초
    ,to_char(func_midnight(writeday), 'yyyy-mm-dd hh24:mi:ss' ) as 작성일자자정
from jdbc_board
order by boardno desc;

commit;

select text
from user_source
where type = 'function' and name = 'func_midnight';

select boardno as 게시글번호 
    , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as 작성일자시분초
    , to_char(func_midnight(writeday), 'yyyy-mm-dd hh24:mi:ss' ) as 작성일자자정
    , sysdate - writeday
    , func_midnight(sysdate) - func_midnight(writeday)
from jdbc_board
order by boardno desc;

String sql = "select count(*) as total\n"+
"    ,sum(decode(func_midnight(sysdate) - func_midnight(writeday), 6, 1, 0))as previous6\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 5, 1, 0)) as previous5\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 4, 1, 0)) as previous4\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 3, 1, 0)) as previous3\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 2, 1, 0)) as previous2\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 1, 1, 0)) as previous1\n"+
"    , sum(decode(func_midnight(sysdate) - func_midnight(writeday), 0, 1, 0)) as today\n"+
"from jdbc_board\n"+
"where func_midnight(sysdate) - func_midnight(writeday) < 7";

select * from jdbc_board
where to_char(writeday, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm')

update jdbc_board set writeday = add_months(writeday, -1)
where boardno = 12

    