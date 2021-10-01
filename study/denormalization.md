## 정규화 vs 반정규화

### 정규화 데이터베이스
특징
정규화 DB는 중복을 최소화하도록 설계되어 insert 시 시간이 적게 걸리고 무결성을 보장한다. (Anomaly 예방)<br>
같은 데이터는 데이터베이스 내에 하나 정도만 놓으려고 노력한다.<br>
하지만 상당수의 일상적 질의를 처리하기 위해 JOIN을 많이 하게 되는 단점이 있다.<br>

### 비정규화 데이터베이스
의도적으로 정규화 원칙을 위배하는 행위<br>
RDB를 사용하는 경우 비정규화 장점
- JOIN 연산의 비용을 줄일 수 있음
- 높은 규모 확장성을 실현하기 위해 자주 사용되는 기법
- 빠른 데이터 조회
  - JOIN 비용 감소
- 조회 쿼리 간단해짐
  - 버그 발생 가능성 줄음
  
단점
- update, insert 비용 높아짐
- update, insert 쿼리 어려워짐
- 일관성 꺠질 수 있음
- 더 많은 저장 공간 필요

비정규화 대상
- 대량 데이터가 있고, 대량의 범위를 자주 처리하는 경우 성능 이슈 있을 시
- 지나치게 조인이 많아 데이터 조회하는 것이 기술적으로 어려운 경우

### 프로젝트 상 이슈
쿼리가 복잡하고 매일 2천만 건의 레코드가 들어올 것에 대비해 
```sql
select product_order_no, product_order_status_code, product_order_amount, modify_ymdt from order_message
where product_order_no IN
      (select product_order_no from member where member_no = 200258617)
order by product_order_no, product_order_status_code, modify_ymdt desc;
```
와 같은 복잡한 쿼리를 단순화하고<br>
컬럼 2개만 있는 테이블을 만드는 비효율을 줄이기 위해 member 테이블의 member_no 컬럼을 order_message의 컬럼에 추가하고 쿼리를 다음과 같이 바꾸었다.
```sql
select product_order_no, product_order_status_code, product_order_amount, modify_ymdt from order_message
group by product_order_status_code
where member_no = 200246482
order by product_order_no, product_order_status_code, modify_ymdt desc;
```
