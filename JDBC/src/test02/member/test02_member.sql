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
,registerday date default sysdate
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

select userseq, userid, passwd, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') as registerday, status
from jdbc_member
where userid = '1234' and passwd = '1234';


-- 도서대여 프로그램 테이블 생성하기 (제약조건 모두 만들어야 한다 + index 까지)
-- 테이블명 hw_member

create table hw_librarianDTO 
(  id       varchar2(20)
,   pwd     varchar2(20)
, constraint pk_hw_librarianDTO_id primary key(id)
);

create table hw_bookDTO
( isbn          varchar2(20)
, category      varchar2(20)
, bookname      varchar2(20)
, author        varchar2(20)
, publisher     varchar2(20)
, price         varchar2(20)
, constraint pk_hw_bookDTO_isbn primary key(isbn)
);

create table hw_seperateBookDTO
( bookid    varchar2(20) 
, isrent    varchar2(1) default '0'
, fk_isbn   varchar(20)
, constraint pk_hw_seperateBookDTO_bookid primary key (bookid)
, constraint fk_hw_seperateBookDTO_fk_isbn foreign key(fk_isbn) references hw_bookDTO(isbn) on delete cascade
, constraint ck_hw_seperateBookDTO_isrent check (isrent in ('0', '1'))
);

create table hw_userdto
(id         varchar2(20)   
, pwd       varchar2(20)
, name      varchar2(20)
, phone     varchar2(20)
, fk_bookid     varchar2(20)
, constraint pk_hw_userdto_id primary key (id)
, constraint fk_hw_userdto_fk_bookid foreign key(fk_bookid) references hw_seperateBookDTO(bookid) on delete cascade
);


create table RentalTaskDTO
(userid         varchar2(20)
, rentalday     date     default sysdate
, returnday     date     default sysdate + 2
, arrears       varchar2(20)
, fk_id     varchar2(20)       
, fk_bookid   varchar2(20)  
, constraint pk_RentalTaskDTO_bookid primary key (fk_bookid) on delete cascade
, constraint fk_RentalTaskDTO_fk_bookid foreign key(fk_bookid) references hw_seperateBookDTO(bookid) on delete cascade
, constraint fk_RentalTaskDTO_fk_id foreign key(fk_id) references hw_userdto(id)
);

select * from tab;

comment on table HW_BOOKDTO
is 'ISBN 번호를 포함하는 BOOKDTO';

comment on table HW_SEPERATEBOOKDTO
is 'ISBN 번호를 외래키로 받고 Bookid를 기본키로 받는 개별 BOOKDTO';

comment on table HW_LIBRARIANDTO
is 'id를 기본키로 갖는 도서관리자DTO';

comment on table HW_USERDTO
is 'id를 기본키로 갖고 개별 BOOKDTO의 bookid를 외래키로 갖는 도서관 이용자DTO';

comment on table RENTALTASKDTO
is '개별책 bookid를 기본키이자 외래키로 갖고 HW_USERDTO의 userid를 외래키로 갖는 대여책DTO';

select * from user_tab_comments;


comment on column HW_BOOKDTO.isbn          
is 'ISBN 번호(기본키)';
comment on column HW_BOOKDTO.category               
is '책 카테고리';
comment on column HW_BOOKDTO.bookname                
is '책이름';
comment on column HW_BOOKDTO.author                  
is '작가이름';
comment on column HW_BOOKDTO.publisher              
is '출판사';
comment on column HW_BOOKDTO.price              
is '가격';

comment on column hw_seperateBookDTO.bookid                  
is '책ID (기본키)';
comment on column hw_seperateBookDTO.isrent                      
is '책 렌트여부 0 : 대여가능, 1: 대여중';
comment on column hw_seperateBookDTO.fk_isbn                     
is 'ISBN번호 (HW_BOOKDTO의 ISBN을 참조하는 외래키)';

comment on column hw_userdto.id                              
is '사용자 아이디 (기본키)';
comment on column hw_userdto.pwd                                     
is '사용자 비밀번호';
comment on column hw_userdto.name                                    
is '사용자 이름';
comment on column hw_userdto.phone                                   
is '사용자 핸드폰 번호';
comment on column hw_userdto.fk_bookid                                   
is '책ID (hw_seperateBookDTO의 bookid를 참조하는 외래키)';

comment on column RentalTaskDTO.userid                                       
is 'hw_userdto의 id를 참조하는 외래키';
comment on column RentalTaskDTO.rentalday                                            
is '빌린날짜';
comment on column RentalTaskDTO.returnday                                            
is '반납날짜';
comment on column RentalTaskDTO.arrears                                              
is '연체료';
comment on column RentalTaskDTO.fk_id                                            
is 'hw_userdto의 id를 참조하는 외래키';
comment on column RentalTaskDTO.fk_bookid                                          
is 'hw_seperateBookDTO의 bookid를 참조하는 외래키';

select * from user_tab_comments;
