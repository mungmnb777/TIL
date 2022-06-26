# 영속성 관리

`EntityManager` 인스턴스는 영속성 컨텍스트와 접근한다. 영속성 컨텍스트는 Entity ID를 통해 고유한 `Entity` 인스턴스와 생명 주기를 관리한다.

## 엔티티 생명 주기

<p align="center">
	<img src="../images/JPA 영속성 관리.png"><br>
	<em>그림 1) 엔티티 생명 주기</em>
</p>

- 비영속 (new/transient)
  - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
- 영속 (managed)
  - 영속성 컨텍스트에 관리되는 상태
- 준영속 (deetached)
  - 영속성 컨텍스트에 저장되었다가 분리된 상태
- 삭제 (removed)
  - 삭제된 상태

```java
// 비영속
Member member = new Member();
member.setId(100L);
member.setName("HelloJPA");

// 영속 (엔티티 매니저에 의해 관리되는 시점)
em.persist(member);

// 준영속
em.detach(member);

// 삭제
em.remove(member);
```

## 쓰기 지연(transactional write-behind)

JPA는 영속성 컨텍스트 내에 쓰기 지연 기능을 지원한다. 쓰기 지연은 영속성 컨텍스트 내에 변경이 발생했을 때 그 쿼리를 바로 DB에 날리는 것이 아니라 `쓰기 지연 SQL 저장소`에 따로 넣어둔 다음, 영속성 컨텍스트가 플러시할 때 모아둔 쿼리를 한번에 데이터베이스에 실행시키는 기능이다.

## 변경 감지(Dirty Checking)

영속성 컨텍스트는 내부에 엔티티 매니저가 관리하고 있는 인스턴스의 초기 스냅샷을 가지고 있다. 이 스냅샷과 영속 상태의 인스턴스의 값이 달라지면 업데이트 쿼리를 만들어 `쓰기 지연 SQL 저장소`에 넣어둔 후 플러시할 때 수정 사항을 적용한다.

## 플러시(flush)

영속성 컨텍스트와 데이터베이스의 데이터를 동기화시키는 작업이다.

플러시가 발생할 때 **세 가지 작업**이 이루어진다.

- 변경 감지
- 수정된 엔티티가 쓰기 지연 SQL 저장소에 등록된다.
- 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송한다.

## 준영속 상태(detached)

- 영속 상태의 엔티티가 영속성 컨텍스트에서 분리되고 `EntityManager`의 기능을 사용할 수 없다.
