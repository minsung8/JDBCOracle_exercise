-- sys로 접속

show user;

-- 오라클 일반 사용자 계정(계정명 : myorauser, 암호 : cclass)을 생성
create user myorauser identified by cclass;

-- 생성된 myorauser에게 오라클 서버에 접속해서 작업할 수 있도록 권한 부여 
grant connect, resource to myorauser;

show user;

select * from tab;

------ *** 회원 테이블 생성하기 *** ------


drop table jdbc_member purge;
create table jdbc_member (userseq number not null -- 회원번호
,userid varchar2(30) not null -- 회원아이디 ,passwd varchar2(30) not null -- 회원암호
,passwd varchar2(20)
,name varchar2(20) not null -- 회원명 ,mobile varchar2(20) -- 연락처
,mobile varchar2(20)
,point number(10) default 0 -- 포인트 ,registerday date default sysdate -- 가입일자
,status number(1) default 1 -- status 컬럼의 값이 1 이면 정상, 0 이면 탈퇴
,constraint PK_jdbc_member primary key(userseq)
,constraint UQ_jdbc_member unique(userid)
,constraint CK_jdbc_member check( status in(0,1) ) );

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

