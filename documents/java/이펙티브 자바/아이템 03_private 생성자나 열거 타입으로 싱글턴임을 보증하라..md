# 아이템 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라.

# 싱글턴(Singleton)

### 정의

- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.

### 특징

- 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기 어렵다.

### 만드는 방식

- 우선 두 가지 방법이 있다. 두 가지 방법 모두 생성자는 private으로 감추고 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 마련해둔다.
    1. public static 멤버가 final 필드인 방식
    
    ```java
    public class Elvis {
    	public static final Elvis INSTANCE = new Elvis();
    	private Elvis() { ... }
    } 
    ```
    
    - private 생성자는 컴파일 시 클래스가 초기화되는 시점에 INSTANCE에 대입할 때 딱 한번만 호출된다.
    - 해당 클래스가 싱글턴임이 API에 명백히 드러난다.
    - 간결하다.
    
     2. 정적 팩터리 메서드를 public static 멤버로 제공한다.
    
    ```java
    public class Elvis {
    	private static final Elvis INSTANCE = new Elvis();
    	private Elvis() { ... }
    	public static Elvis getInstance() { return INSTANCE; }
    }
    ```
    
    - API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다.
    - 정적 팩터리를 제니릭 싱글턴 팩터리로 만들 수 있다.
    - 정적 팩터리의 메서드 참조를 공급자로 사용할 수 있다.

1. 원소가 하나인 열거 타입을 선언한다.

```java
public enum Elvis {
	INSTANCE;

	public void leaveTheBulding() { ... }
}
```