# 1. DTO (Data Transfer Object)

데이터를 전달하기 위해 사용하는 객체이다.

그렇다면 DTO를 왜 사용하는가? 리팩토링의 저자 마틴 파울러는 그의 다른 저서 ([Patterns of Enterprise Application Architecture - Martin Fowler](https://martinfowler.com/eaaCatalog/dataTransferObject.html))에서 이렇게 말했다.

> Remote Facade(388)와 같은 원격 인터페이스로 작업할 때 호출할 때마다 비용이 많이 듭니다. 결과적으로 호출 수를 줄여야 하며, 이는 호출할 때마다 더 많은 데이터를 전송해야 함을 의미합니다. 이를 수행하는 한 가지 방법은 많은 매개변수를 사용하는 것입니다. 그러나 이것은 종종 프로그래밍하기가 어색합니다. 실제로 단일 값만 반환하는 Java와 같은 언어에서는 불가능한 경우가 많습니다.<br><br>
> When you're working with a remote interface, such as Remote Facade (388), each call to it is expensive. As a result you need to reduce the number of calls, and that means that you need to transfer more data with each call. One way to do this is to use lots of parameters. However, this is often awkward to program - indeed, it's often impossible with languages such as Java that return only a single value.

우선 자바를 이용하니 자바를 기준으로 설명한다. 자바는 기본적으로 하나의 메서드에서는 하나의 기본 타입(primitive type)이나 참조 타입(reference type)만을 반환한다. 그렇다면 하나의 트랜잭션에서 모든 필드를 각각 메서드로 구현하여 다 따로따로 받게되면 필드 A 호출 및 반환, 필드 B 호출 및 반환,,, 언뜻 봐도 좋아보이지 않는 설계이다. 그래서 마틴 파울러는 반환해야 하는 필드들을 하나로 합쳐 보내는 방법을 고안하였고, 그게 바로 DTO이다.

# 2. VO (Value Object)

값 그 자체만을 표현하는 객체이다. 그렇기 때문에 객체의 내부 필드가 전부 다 같은 두 객체는 같은 객체로 인정해야하기 때문에 VO는 equals()와 hashCode()를 오버라이딩하여 구현해주어야 한다.

좀 더 자세하게 알아보기 위해 이번에도 마틴 파울러의 말을 인용해왔다.

> 프로그래밍할 때 나는 종종 사물을 합성물로 표현하는 것이 유용하다는 것을 알게 됩니다. 2D 좌표는 x 값과 y 값으로 구성됩니다. 금액은 숫자와 통화로 구성됩니다. 날짜 범위는 시작 날짜와 종료 날짜로 구성되며 자체적으로 연도, 월, 일의 조합이 될 수 있습니다.<br><br>
> When programming, I often find it's useful to represent things as a compound. A 2D coordinate consists of an x value and y value.

즉, 마틴 파울러의 말은 x, y로 표현하는 것이 아니라 ‘2차원 좌표’ 그 자체로서 표현하는 것을 말한다. 이는 더 직관적이고 알아보기 쉬운 표현법이라는 생각이 든다.

그런데 자바에서 객체를 생각해보면 어떤지 생각해보자.

```java
// 좌표 객체
class Point {
	double x;
	double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
```

이 때 JUnit5의 `assertThat(new Point(2, 3)).isEqualTo(new Point(2, 3))`이 테스트를 통과할 수 있을까? 답은 `아니오`이다. 기본적으로 참조 객체는 생성할 때마다 각각 다른 정보로 표현된다. 그렇기 때문에 (2, 3)이라는 값 자체는 같은데 다르다고 표현되는 것이다. 이를 막기 위해서 Value Object는 equals()와 hashCode()를 오버라이딩 해야한다.

그리고 Value Object를 사용함으로써 가져올 수 있는 결과 중 하나는 객체가 같은 객체를 참조하는지, 다른 객체를 참조하는 지 신경 쓸 필요가 없다는 점이다. 마틴 파울러가 예시로 든 Aliasing Bug를 보자.

```java
// 은퇴 날짜
Date retirementDate = new Date(Date.parse("Tue 1 Nov 2016"));

// 은퇴하는 날에 송별회를 하자!
Date partyDate = retirementDate;

// 그런데 그 날이 평일이네? 날짜는 주말로 옮기는게 좋겠어! 6일 토요일로 바꾸자!
partyDate.setDate(5);

// 근데 은퇴 날짜도 6일로 변해버림,,
assertEquals(new Date(Date.parse("Sat 5 Nov 2016")), retirementDate);
```

은퇴 날짜와 파티 날짜 둘 다 같은 객체를 참조하고 있기 때문에 발생한 버그이다. 이를 막기 위해서는 `new`를 활용해서 새로운 객체를 참조시켜주면 된다.

그리고 Value Object가 변경이 불가능한 경우 Value Object를 실제고 변경할 수 없는 객체로 생성하면 훨씬 더 쉽다고 말한다. → 지금 이 부분이 우리가 알고 있는 VO가 되는 것 같다.

# 3. DAO(Data Access Object)

DAO는 실제 비즈니스 로직이 구현되는 부분과 DB 접근 기술 부분을 분리하기 위해서 J2EE에서 등장한 개념이다.

오라클의 [문서](https://www.oracle.com/java/technologies/dataaccessobject.html)에는 다음과 같은 문제를 제시한다.

> 응용 프로그램은 JDBC API를 사용하여 관계형 데이터베이스 관리 시스템(RDBMS)에 있는 데이터에 액세스할 수 있습니다. JDBC API는 관계형 데이터베이스와 같은 영구 저장소의 데이터에 대한 표준 액세스 및 조작을 가능하게 합니다. JDBC API를 사용하면 J2EE 애플리케이션이 RDBMS 테이블에 액세스하기 위한 표준 수단인 SQL 문을 사용할 수 있습니다. **_그러나 RDBMS 환경에서도 특정 데이터베이스 제품에 따라 SQL 문의 실제 구문과 형식이 다를 수 있습니다._**<br><br>
> Applications can use the JDBC API to access data residing in a relational database management system (RDBMS). The JDBC API enables standard access and manipulation of data in persistent storage, such as a relational database. The JDBC API enables J2EE applications to use SQL statements, which are the standard means for accessing RDBMS tables. However, even within an RDBMS environment, the actual syntax and format of the SQL statements may vary depending on the particular database product.

>

간단하게 말하면 MySQL로 구현한 JDBC 구문은 Oracle DBMS에서는 사용할 수 없다는 말이다. Oracle에서 사용하기 위해서는 비즈니스 로직에 있는 MySQL JDBC 구문을 싹 다 오라클 구문으로 변경해주어야 하는데 이러한 작업은 너무 어렵고 지루하다. 그로 인해 **DAO 패턴**이라는 개념이 등장하게 되었다.

DAO와 연관되어 있는 클래스 다이어그램은 다음과 같다.

<p align="center">
	<img src="../images/DAO, DTO, VO_1.png"><br>
	<em>그림 1) DAO 클래스 다이어그램</em>
</p>

-   BusinessObject : 싸피에서 배운 Service와 비슷한 개념. 데이터 저장 및 활용을 위해 데이터 소스에 엑세스하고자 하는 객체이다.
-   DataAccessObject : DAO 패턴의 기본 객체이다. 데이터 불러오기 및 저장 작업을 BusinessObject로 부터 위임받는다.
-   Data Source : 보통 영구 저장소(RDMBS, XML 등의 DB)를 뜻하는데 다른 시스템을 말하는 경우도 있다.
-   Transfer Object : 위에서 설명한 DTO와 같은 개념이다.

이런 식으로 DAO를 추가함으로써 비즈니스 로직과 데이터 접근 로직을 분리하게 되었고 이로 얻게 되는 장점이 정말 많다.

우선 **투명성**이다. 비즈니스 객체에서는 데이터 소스 관련 세부 로직이 DAO 내부에 숨겨져 있으므로 투명하게 접근 가능하다.

그리고 **마이그레이션**이 쉽게 가능하다. MySQL에서 Oracle로 DBMS를 변경하고 싶을 경우, 비즈니스 객체에서 DAO 구현체를 갈아 끼워주기만 하면 바로 변경할 수 있다.

또 비즈니스 객체의 코드가 **간결**해진다. JDBC에 관한 코드를 봤다면 생각보다 되게 길다는 것을 알 수 있을 것이다. 그러한 JDBC 코드와 더불어 비즈니스 로직이 추가된다면 비즈니스 객체는 복잡하고 지저분한 코드로 범벅이 될 것이다. 하지만 이를 분리함으로써 로직을 쉽게 파악할 수 있다는 장점이 있다.
