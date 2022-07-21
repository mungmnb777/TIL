# JPQL

## 특징

- JPQL은 객체지향 쿼리 언어이다. 따라서 테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.
- JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
- JPQL은 결국 SQL로 변환된다.

---

## JPQL 기본 문법

```
# SELECT문
	select_절
	from_절
	[where_절]
	[groupby_절]
	[having_절]
	[orderby_절]

# UPDATE문
	update_절
	[where_절]

# DELETE문
	delete_절
	[where_절]
```

- 엔티티와 속성은 대소문자 구분 O `(Member, Age)`
- JPQL 키워드는 대소문자 구분 X `(SELECT, FROM, WHERE)`
- 엔티티 이름 사용, 테이블 이름이 아님!!
- 별칭은 필수(m) (as는 생략 가능)

### 집합과 정렬

```
select
	COUNT(m),   // 회원수
	SUM(m.age), // 나이 합
	AVG(m.age), // 평균 나이
	MAX(m.age), // 최대 나이
	MIN(m.age), // 최소 나이
from Member m
```

- ANSI 표준 집합 함수를 다 사용할 수 있다!! `(GROUP BY, HAVING, ORDER BY)`

### TypeQuery, Query

- TypeQuery : 반환 타입이 명확할 때 사용
- Query : 반환 타입이 명확하지 않을 때 사용

### 결과 조회

- getResultList() : 결과가 하나 이상일 때 사용, 리스트 반환
  - 결과가 없으면 빈 리스트 반환
- getSingleResult() : 결과가 정확히 하나, 단일 객체 반환
  - 결과가 없으면 `javax.persistence.NoResultException`
  - 둘 이상이면 `javax.persistence.NonUniqueResultException`

### 파라미터 바인딩

```java
TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class)

// 파라미터 바인딩
query.setParameter("username", "member1");
```

- `:username` 부분에 setParameter() 메서드를 통해 바인딩할 수 있다.

---

## 프로젝션

- SELECT 절에 조회할 대상을 지정하는 것을 말한다.
- 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입
  - SELECT **m** FROM Member m → 엔티티 프로젝션
  - SELECT **m.team** FROM Member m → 엔티티 프로젝션
  - SELECT **m.address** FROM Member m → 임베디드 타입 프로젝션
  - SELECT **m.username, m.age** FROM member m → 스칼라 타입 프로젝션

### 프로젝션 - 여러 값 조회

프로젝션을 하는 경우 여러 필드의 값을 가져오는 경우가 있다. 그럴 때 자바에서는 어떻게 값을 가져와야 하는가?

1. Query 타입으로 조회
2. Object[] 타입으로 조회

   ```java
   List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
   ```

   - Object[]의 0번 인덱스부터 필드의 값이 차례로 들어간다.

3. new 명령어로 조회
   - DTO를 생성해서 바로 조회하는 방법
     ```java
     List<MemberDto> resultList = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class).getResultList();
     ```
   - 패키지명을 포함한 전체 클래스명을 입력해야 한다.

---

## 페이징

- JPA는 페이징을 다음 두 API로 추상화
- `setFirstResult(int startPosition)` : 조회 시작 위치
- `setMaxResults(int maxResult)` : 조회할 데이터 수

---

## 조인

- 내부 조인 :
  ```java
  SELECT m FROM Member m [INNER] JOIN m.team t
  ```
- 외부 조인 :
  ```java
  SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
  ```
- 세타 조인 :
  ```java
  select count(m) from Member m, Team t where m.username = t.name
  ```

### ON 절

- ON절을 활용해 조인할 수 있다.
  - 조인 대상 필터링 가능
  - 연관관계 없는 엔티티를 외부 조인할 수 있다.

---

## 서브쿼리

- JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
- 하이버네이트에서는 SELECT 절에서도 가능
- **FROM절의 서브 쿼리는 현재 JPQL에서 불가능!!**
