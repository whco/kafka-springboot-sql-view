## git
[git](https://git-scm.com/book/ko/v2) 참조
### 1. 시작하기
- git은 DVCS
- 저장소를 히스토리와 더불어 전부 복제
* 빠른 속도 
* 단순한 구조 
* 비선형적인 개발(수천 개의 동시 다발적인 브랜치) 
* 완벽한 분산 
* Linux 커널 같은 대형 프로젝트에도 유용할 것(속도나 데이터 크기 면에서) 

- 무결성
	- 데이터를 저장하기 전에 항상 체크섬을 구하고 그 체크섬으로 데이터를 관리
	- Git은 파일을 이름으로 저장하지 않고 해당 파일의 해시로 저장 :  SHA-1 해시

Committed, Modified, Staged
* Committed란 데이터가 로컬 데이터베이스에 안전하게 저장됐다는 것을 의미한다. 
* Modified는 수정한 파일을 아직 로컬 데이터베이스에 커밋하지 않은 것을 말한다. 
* Staged란 현재 수정한 파일을 곧 커밋할 것이라고 표시한 상태를 의미한다. 


- 워킹 트리, Staging Area, Git 디렉토리
	- 워킹 트리
	  - Git이 프로젝트의 메타데이터와 객체 데이터베이스를 저장하는 곳
	  - 다른 컴퓨터에 있는 저장소를 Clone 할 때 Git 디렉토리가 만들어진다.
    
- git 최초 설정
  1. ```/etc/gitconfig``` : 시스템의 모든 사용자와 모든 저장소에 적용
    - ```git config --system```
  2. ```~/.gitconfig, ~/.config/git/config``` : 특정 사용자(즉 현재 사용자)에게만 적용되는 설정
    - ```git config --global```
  3. ```.git/config``` : 이 파일은 Git 디렉토리에 있고 특정 저장소(혹은 현재 작업 중인 프로젝트)에만 적용
    - ```--local```
    - default로 적용돼 있음.
  3 > 2 > 1 순으로 적용.
  
  - Git을 설치하고 나서 가장 먼저 해야 하는 것은 사용자이름과 이메일 주소를 설정
  ```mac
  git config --global user.name "John Doe"
  git config --global user.email johndoe@example.com
  ```
  - 확인 : 
  ```linux
  git config --list
  git config user.name
  git config --show-origin user.name #설정 파일 경로 확인
  git help config #도움말(매우 긺)
  git add -h #명령어 옵션
  ```

### 2. Git의 기초


주로 다음 두 가지 중 한 가지 방법으로 Git 저장소를 쓰기 시작한다.

1. 아직 버전관리를 하지 않는 로컬 디렉토리 하나를 선택해서 Git 저장소를 적용하는 방법

2. 다른 어딘가에서 Git 저장소를 Clone 하는 방법

본인은 2번의 경우에 해당

```git clone https://oss.navercorp.com/order-internship/2021-internship-first```

