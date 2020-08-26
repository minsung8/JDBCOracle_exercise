show user;

create table jdbc_tbl_memo
( no        number(4)
, name      varchar2(20) not null
, msg       varchar2(200) not null
, writeday  date default sysdate
, constraint PK_jdbc_tbl_memo_no primary key(no)
);

create sequence jdbc_seq_memo
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


