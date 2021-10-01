1. 현재 사용자 확인

SELECT USER();



2. 현재 데이터베이스 확인

SELECT DATABASE();


show batabases;<br>
create database mydb;<br>
select user, host from user;<br>

CREATE USER 'myuser'@'%' IDENTIFIED WITH mysql_native_password BY 'xxx';
- '%'은 'myuser'에 접속할 수 있는 출발지 IP, '%'로 돼 있으면 모든 곳에 열려 있음<br>
ALTER USER myuser IDENTIFIED WITH mysql_native_password BY 'yyyy';
- passwd 변경<br>
GRANT ALL PRIVILEGES ON \*.\* TO 'myuser'@'%' WITH GRANT OPTION;
- . 앞의 *은 DB, 뒤는 테이블, 에게 권한을 준다는 뜻
