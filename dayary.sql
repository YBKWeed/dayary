create user YBKWeed identified by 11111111;

grant dba to YBKWeed;

--diary
create table diary(
    id number primary key,
    u_id varchar2(15),
    title varchar2(50),
    content clob not null,
    regdate date not null
);
create sequence seq_diary_id;

insert into diary 
values(seq_diary_id.nextval, 'flynn', '첫 번째 일기', 
       '곧 점심시간이다', sysdate);
       
insert into diary 
values(seq_diary_id.nextval, 'flynn', '두 번째 일기', 
       '곧 프로젝트를 시작해야 한다', sysdate);
commit;
delete diary;

select * from diary;

--member
create table member(
    id varchar2(10) primary key,
    password varchar2(10) not null,
    name varchar2(30) not null,
    email varchar2(30) not null unique,
    gender char(1) check(gender in('m', 'f'))
);

alter table diary add constraint fk_diary_u_id 
foreign key(u_id)
references member(id);  

select * from member;

insert into member values('Weed', 1234, '김영범', 'dudqja1003@naver.com', 'm');

create table tag(
    id number primary key,
    d_id number references diary(id),
    name varchar2(30) not null,
    color varchar2(10) default 'default' not null
);

create sequence seq_tag_id;


