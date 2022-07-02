# 상속관계 매핑

관계형 DB는 상속 관계가 없지만 슈퍼타입 서브타입 관계라는 모델링 기법이 상속과 유사하다. 즉, 상속관계 매핑이란 객체의 상속과 DB의 슈퍼타입 서브타입 관계를 매핑시키는 것을 말한다.

이 논리 모델을 실제 물리 모델로 구현하는 방법은 총 세 가지가 있다.

- 각각 테이블로 변환 → 조인 전략
  - @Inheritance(strategy=InheritanceType.**JOINED**)
- 통합 테이블로 변환 → 단일 테이블 전략
  - @Inheritance(strategy=InheritanceType.**SINGLE_TABLE**)
- 서브타입 테이블로 변환 → 구현 클래스마다 테이블 전략
  - @Inheritance(strategy=InheritanceType.**TABLE_PER_CLASS**)

## 모델 구현 전략 특징

<p align="center">
	<img src="../images/상속관계 매핑_1.png"><br>
	<em>그림 1) 클래스 다이어 그램</em>
</p>

### 1. 조인 전략

<p align="center">
	<img src="../images/상속관계 매핑_2.png"><br>
	<em>그림 2) 조인 전략 ERD</em>
</p>

- **장점**
  - 테이블이 정규화가 되어있다.
  - 외래키 참조 무결성 제약조건을 활용가능하다. (ITEM 테이블만 확인하면 ALBUM, MOVIE, BOOK 테이블 중 어떤 테이블의 데이터인지 확인할 수 있다.)
  - 저장공간 효율화
- **단**점
  - 조회 시 조인을 많이 사용하면서 성능이 저하된다.
  - 조회 쿼리가 복잡해진다.
  - 데이터 저장 시 INSERT SQL을 2번 호출한다.

### 2. 단일 테이블 전략

<p align="center">
	<img src="../images/상속관계 매핑_3.png"><br>
	<em>그림 3) 단일 테이블 전략 ERD</em>
</p>

- **장점**
  - 조인이 필요없다.
  - 조회 쿼리가 단순하다.
- **단점**
  - 자식 엔티티가 매핑한 컬럼은 모두 null을 허용한다. (null 값이 들어가게 되면서 저장 공간도 많이 차지하기 때문에 비효율적이다.)

### 3. 구현 클래스마다 테이블 전략

<p align="center">
	<img src="../images/상속관계 매핑_4.png"><br>
	<em>그림 4) 구현 클래스마다 테이블 전략 ERD</em>
</p>

- **장점**
  - 서브 타입을 명확하게 구분해서 처리할 때 효과적이다.
  - not null 제약 조건을 사용할 수 있다.
- 단점
  - 여러 자식 테이블을 함꼐 조회할 때 성능이 느리다. (어플리케이션에서 ITEM 객체를 조회할 경우에 UNION SQL을 사용한다.)
  - 자식 테이블을 통합해서 쿼리하기 어렵다.

## @MappedSuperclass

상속관계와 비슷하지만 상속관계 매핑이 아니라 부모 클래스를 상속 받는 자식 클래스에 **매핑 정보**만 제공한다.

그렇기 때문에 조회, 검색도 불가능하다. (`em.find(BaseEntity)`가 불가능)

직접 생성해서 사용할 일이 없기 때문에 추상 클래스를 권장한다.

# 참고자료

[자바 ORM 표준 JPA 프로그래밍 - 김영한](https://www.inflearn.com/course/ORM-JPA-Basic)
