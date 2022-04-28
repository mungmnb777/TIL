# 1. POJO란?

2000년대 초반, EJB와 같은 무겁고 어려운 Java EE 프레임워크를 사용하는데 여기서 사용되는 객체들이 해당 프레임워크을 의존하는 무거운 객체를 만들게 된 것에 반발하여, 기존의 순수한 옛날 자바로 돌아가자!라는 의미에서 Plain Old Java Object라는 용어가 만들어지게 되었다.

# 2. 조건

위키백과에 의하면 프레임워크에서 제공하는 클래스를 상속받거나 인터페이스를 구현한다면 POJO로 취급하지 않는다.

다음 예시가 있다.

1. 프레임워크 클래스의 상속

```java
public class Foo extends javax.servlet.http.HttpServlet { ... }
```

이 경우에는 J2EE의 HttpServlet 클래스를 가져와서 상속한다. 순수한 자바 클래스를 상속하는 것이 아니기 때문에 Foo 클래스는 POJO라고 볼 수 없다.

2. 인터페이스 구현

```java
public class Bar implements javax.ejb.EntityBean { ... }
```

이 경우에도 EJB의 EntityBean을 구현하는 클래스이다. EJB 역시 엔터프라이즈 애플리케이션을 쉽게 구현하기 위해 만들어진 프레임워크로 순수한 자바 클래스가 아니기 때문에 EJB의 인터페이스를 구현한 Bar 클래스는 POJO라고 볼 수 없다.

3. 애너테이션을 포함한 클래스

```java
@javax.persistence.Entity
public class Baz { ... }
```

애너테이션을 포함하는 것도 마찬가지이다. 여기서는 `@javax.persistence.Entity`를 예시로 들었는데 이건 JPA라는 ORM 기술 표준에서 사용되는 애노테이션이다. 마찬가지로 순수한 자바 클래스가 아니기 떄문에 Baz 클래스는 POJO라고 원래는 볼 수 없지만!!!

> The idea is that if the object (actually class) were a POJO before any annotations were added, and would return to POJO status if the annotations are removed then it can still be considered a POJO.

위키백과에는 이런 문구가 있는데 애노테이션이 없다고 간주했을 때 POJO라면, 애노테이션이 있어도 POJO로 간주될 수 있다는 아이디어이다.

기술적인 어려움으로 인해 완전한 POJO를 구현하기 어렵기 때문에 실제로 영속적인 기능을 제대로 작동하게 만들기 위해 사전 지정된 애노테이션을 사용해야만 하기 때문에 만들어진 조건인 것 같다.
