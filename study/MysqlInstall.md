설치 방법 생략
- linux 버전 확인 후 mysql.com에서 다운로드 링크 확인해 yum localinstall {링크} 진행

설정

```linux
$ vi /etc/my.cnf
```
```linux
[client]
default-character-set = utf8mb4

[mysql]
default-character-set = utf8mb4

[mysqldump]
default-character-set = utf8mb4

[mysqld]
skip-character-set-client-handshake
init_connect="SET collation_connection = utf8mb4_unicode_ci"
init_connect="SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci"

character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
```
추가 : utf8mb4 설정

권한 요구하면 웬만하면 sudo 로 실행

> ps -ef | grep mysql
- mysql 실행 중인 지 확인
> systemctl start mysqld
- mysql 실행

/var/log 에 시스템 로그들이 다 모여있음
- 여기서 mysqld.log 에서 'temporary password' 찾아서 변경

> mysql -u root -p
로 비밀번호 친 후 접속하면 설치 완료!
