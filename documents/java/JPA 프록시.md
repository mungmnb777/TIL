# 프록시

- 프록시 객체는 처음 사용할 때 한 번만 초기화된다.
- 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아니다. 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근할 수 있다.

  - 프록시 자체가 실제 엔티티의 인스턴스를 참조하고 있다. 그렇기 때문에 프록시의 메서드를 호출하면 실제 타겟 인스턴스를 초기화한 후 그 메서드를 다시 호출하는 방식으로 작동한다.

    <p align="center">
        <img src="../images/JPA proxy_1.png"><br>
        <em>그림 1) 프록시 객체</em>
    </p>


- 프록시 객체는 실제 엔티티를 상속받는다. 그래서 단순하게 `Entity` 객체의 클래스 정보(`getClass()`)를 `==` 비교할 경우 원하는 값이 나오지 않을 수도 있다. 그렇기 때문에 `instance of`를 사용한다.
- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 `em.getReference()`를 호출해도 실제 엔티티가 반환된다.
  - 여기에는 재밌는 특징이 있다. JPA는 기본적으로 같은 엔티티의 PK까지 같은 인스턴스를 `==` 비교를 할 경우 항상 `true`가 나와야 한다. 그래서 `em.getReference()`를 통해 프록시 객체를 먼저 호출 한 다음에 같은 PK를 가지는 실제 객체를 `em.find()`를 통해 호출하려해도 프록시 객체가 반환된다.
  ```java
  Member refMember = em.getReference(Entity.class, 1L);
  Member realMember = em.find(Entity.class, 1L);
  ```
  → 이 경우 `realMember`도 프록시 객체를 참조하게 된다.
- 프록시가 준영속 상태이거나 영속성 컨텍스트에서 제거되었을 경우 프록시를 초기화할 때 문제가 발생한다.

## 즉시 로딩과 지연 로딩

단순하게 `Member` 객체만을 이용할 때 객체의 필드 중 하나인 `Team`도 조회할 필요가 있을까? `Team`을 조회하기 위해서는 조인을 이용해야 하는데, 필요도 없는 필드를 위해 조인까지 하는 것은 리소스가 너무 아깝다. 이를 해결하기 위해 JPA는 지연 로딩이라는 방법을 제공한다.

<p align="center">
    <img src="../images/JPA proxy_2.png"><br>
    <em>그림 2) 프록시 객체를 이용한 지연 로딩</em>
</p>

지연 로딩은 `fetch = FetchType.LAZY` 옵션을 통해서 사용할 수 있다.

```java
@Entity
public class Member {
	@Id @GeneratedValue
	private Long id;

	@Column(name = "USERNAME")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
	@JoinColumn(name = "TEAM_ID")
	private Team team;
	..
}
```

위와 같이 `@ManyToOne`과 같은 애노테이션의 값으로 넣어주면 된다.

즉시 로딩은 지연 로딩의 반대로 `Member` 객체를 가져올 때 `Team` 객체도 조인하여 함께 가져오는 것이다. 이 경우에는 `fetch = FetchType.EAGER` 옵션을 통해서 가져올 수 있다.

## 영속성 전이(CASCADE)

특정 엔티티를 영속 상태로 만들 때, 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때 사용하는 옵션이다.

### CASCADE의 종류

- **ALL : 모두 적용**
- **PERSIST : 영속**
- **REMOVE : 삭제**
- MERGE : 병합
- REFRESH : REFRESH
- DETACH : DETACH

CASCADE의 경우 사용하는 타겟의 엔티티가 여러 군데에서 참조되는 엔티티라면 이 옵션을 사용해서는 안된다.

## 고아 객체

고아 객체는 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 뜻하는데, JPA에서는 이러한 고아 객체를 `orphanRemoval`이라는 옵션을 통해서 자동으로 제거해준다.

참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 판단하고 자동으로 삭제하는 것이다. 이 옵션도 참조하는 곳이 하나일 때만 사용해야한다. (즉, 특정 엔티티가 개인적으로 소유하는 경우!)

## 영속성 전이 + 고아 객체

- `CascadeType.ALL` + `orphanRemoval = true`

두 옵션을 모두 활성화하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있다.
