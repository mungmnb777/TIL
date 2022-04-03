# 0. 개요

이펙티브 자바를 공부하면서 정리한 싱글턴 패턴에 관한 내용입니다. 백기선님의 강의 이펙티브 자바 완벽 공략 1부를 참고했습니다.

# 1. 싱글턴 패턴(Singleton Pattern)이란?

생성 디자인 패턴 중 하나이다. 클래스가 싱글턴 패턴으로 구현되었을 경우 어플리케이션에서 인스턴스를 오직 하나만 생성할 수 있음을 보증한다.

그럼 이 싱글턴 패턴은 어디에 사용할 수 있을까? 

**첫 번째**로 클래스가 인스턴스 내부에서 어떠한 상태가 저장되지 않는 경우 사용하면 좋다(예를 들어 Math 클래스와 같은 함수 클래스). 이럴 경우 여러 사용자가 각각 객체를 확보하지 않아도 일관성있는 작업을 진행할 수 있기 때문에 인스턴스를 하나만 유지하는 경우 메모리 측면에서 이득을 볼 수 있다.

<p align="center">
  <img src=https://user-images.githubusercontent.com/72181693/161428846-1b8edeb7-2636-4300-91b8-5b654e30860b.png><br>
  <em>그림 1) 자바의 API 중 하나인 Math 클래스. 싱글턴 패턴으로 구현하였다.</em>
</p>

두 번째로 어플리케이션 내부에서 데이터 공유가 쉽다. 대부분 싱글턴은 static으로 생성되기 때문에 어플리케이션 전역에서 사용할 수 있게 된다. 하지만 동시에 싱글턴 인스턴스에 접근하게 되면 동시성 문제가 발생할 수 있으니 이 점을 유의해야한다.

# 2. 싱글턴 패턴 생성 방식

이펙티브 자바의 저자 조슈아 블로크는 싱글턴을 만드는 방식으로 세 가지 방법을 제시한다.

1. public static final 필드 방식의 싱글턴
2. 정적 팩터리 방식의 싱글턴
3. 열거 타입 방식의 싱글턴

각각의 생성 방식에 대해 차례대로 설명하겠다.

## 2.1. public static final 필드 방식의 싱글턴

우선 생성 코드는 아래와 같다.

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }
}
```

이때 생성자는 private으로 되어있어 클래스가 JVM에 로드될 때 static 필드가 생성되면서 **딱 한번**만 실행된다. 다른 접근제한자로 이루어진 생성자가 없으므로 인스턴스가 딱 하나뿐이라 보장되는 것처럼 보인다.

## 2.2. 정적 팩터리 방식의 싱글턴

생성 코드는 아래와 같다.

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() { }

    public static Elvis getInstance() { return INSTANCE; }
}
```

싸피인에게 가장 익숙한 방식이다. `getInstance()`라는 정적 팩터리 메서드를 생성해 거기서 인스턴스를 반환하는 방식이다.

이 방식에는 세 가지 장점이 있다.

1. 나중에 싱글턴이 아닌 코드로 변환하기가 편하다. 아래와 같이 `getInstance()`의 반환값만 바꿔주면 클라이언트 코드를 변경하지 않아도 된다는 장점이 있다.

```java
public class Elvis implements Singer{
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() { }

    // 정적 팩터리 메서드의 반환값만 바꿔주면 됨!!
    public static Elvis getInstance() { return new Elvis(); }
}
```

2. 원한다면 제네릭 싱글턴 팩터리로 만들 수 있다는 점이다. 아래와 같이 사용하면 제네릭을 사용할 수 있다는 장점이 있다.

```java
public class MetaElvis<T> {

    private static final MetaElvis<Object> INSTANCE = new MetaElvis<>();

    private MetaElvis() { }

    @SuppressWarnings("unchecked")
    public static <E> MetaElvis<E> getInstance() { return (MetaElvis<E>) INSTANCE; }
}
```

3. 정적 팩터리 메서드 참조를 공급자로 사용할 수 있다는 점이다.  이 부분은 완벽히 이해하지는 못했지만 이해한 부분을 써보겠다,,, 공급자로 불리는 Supplier 인터페이스는 `@FunctionalInterface` 애노테이션을 가지고 있다. 이 애노테이션은 보통 함수형 프로그래밍, 즉 람다식을 사용할 때 주로 쓰이는 애노테이션이다. 즉 클라이언트 코드에서 람다식을 사용하고 싶을 때, Supplier를 사용하는 것이다. 다음 코드는 그 예시이다.

```java
public class Concert {
    public void start(Supplier<Singer> singerSupplier) {
        Singer singer = singerSupplier.get();
        singer.sing();
    }

    public static void main(String[] args) {
        Concert concert = new Concert();
        concert.start(Elvis::getInstance);
    }
}
```

보면 start() 메서드의 파라미터로 Elvis::getInstance를 사용함으로써 singer 객체에 의존성을 주입하는 모습을 확인할 수 있다. 이 방식의 장점은 내 생각일 뿐인데 singer 변수가 참조하는 객체를 추상화하여 의존성을 떨어뜨리기 위한 이유가 아닌가 싶다.

참고로 `Elvis::getInstance`는 `() -> Elvis.getInstance()`와 같다.

## 2.3. Enum 타입 방식의 싱글턴

가장 단순한 방식이다.

```java
 public enum Elvis {
    INSTANCE;
}
```

이 방식이 제일 간결하고 편한 방식의 싱글턴이다. 아래 3번 챕터에 나와있는 리플렉션 공격이나, 직렬화 상황으로 제 2의 인스턴스가 생기는 일을 완벽히 막아주는 방식이다.

이펙티브 자바에서는 싱글턴 생성 시 이 방식으로 구현하는 것을 권장한다.

# 3. 싱글턴임을 보증하라.

지금부터의 내용은 Effective Java 3/E 책을 읽고 참고 지식으로 알아두었으면 하는 내용을 가져와보았다.

우리는 위에서 세 가지 방식의 싱글턴 생성 방식을 보았다. private으로 클라이언트에서 생성자를 접근할 수 없게 만들었기 때문에 인스턴스가 하나임이 보증되는 것처럼 보인다. 하지만 다른 언어에서는 잘 모르겠지만, 자바에서는 두 가지 예외가 존재한다.

## 3.1. 리플렉션 API를 사용하는 경우

리플렉션 API는 인스턴스로 클래스의 정보를 다루기 위한 API이다. 여기에는 `setAccessible()`이라는 메서드가 있는데, 이는 필드나 메서드의 접근제한자에 의한 제어를 변경한다.

의 코드는 `setAccessible()` 메서드를 이용한 싱글턴 보증에 관한 테스트이다.

```java
// 생성자로 여러 인스턴스 만들기
public class ElvisReflection {
    public static void main(String[] args) {
        try {
            Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();
            System.out.println(elvis1);
            System.out.println(elvis2);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
```

우선 Elvis.class라는 Class 타입의 인스턴스를 가져온다. 이 인스턴스에 getDeclaredConstuctor()라는 메서드는 그 클래스의 생성자를 인스턴스로 가져오는데 private 접근제한자의 생성자도 포함한다. 이 부분이

 `Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();`이다.

그 후 defaultConstructor의 접근을 허용하고 `newInstance()` 메서드를 실행하게 되면 private으로 되어있어도 생성자를 사용할 수 있게 된다.

<p align="center">
  <img src=https://user-images.githubusercontent.com/72181693/161429079-89bc71dd-41fe-4261-90a1-3c71170e712d.png><br>
  <em>그림 2) 위의 코드를 실행한 결과.</em>
</p>

위의 사진을 보면 해시 코드가 다른 Elvis 객체가 생성되어 있음을 보여주고, 이는 싱글턴 객체가 하나임을 보증하지 않는다는 뜻이다.

이를 막기 위해서는 필드와 생성자 내부에 다음과 같은 코드를 추가해주어야 한다.

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private static boolean isCreated;

    private Elvis() {
        if (isCreated) {
            throw new UnsupportedOperationException();
        }
        created = true;
    }
}
```

위와 같이 필드에 flag를 생성해서 이를 체크함으로써 어플리케이션 스코프에서 두 번 이상 생성자에 접근하려고 할 떄 예외를 던진다.

## 3.2. 싱글턴 클래스를 직렬화할 경우

예전에 싸피 수업 초반 자바 수업에서 직렬화에 대해서 배웠던 적이 있다. 직렬화는 클래스에 Serializable이라는 인터페이스를 상속받고 객체를 상태 그대로 파일로 저장하고 필요할 때 다시 불러와서 사용하는 방법이다.

하지만 직렬화를 통해 파일을 저장한 뒤에 파일을 불러오고 싶을 때, `readObject()`메서드를 사용하게 되는데 이 메서드를 사용하면 클래스가 초기화될 때 만들어진 인스턴스와는 별개의 인스턴스를 반환한다!

아까 public static 필드 방식 코드에 Serializable 인터페이스를 상속받았다고 가정하고 다음 코드를 보자.

```java
public static void main(String[] args) {
    try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
        out.writeObject(Elvis.INSTANCE);
    } catch (IOException e) {
        e.printStackTrace();
    }

    try (ObjectInput in = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
        Elvis elvis = (Elvis) in.readObject();

        System.out.println(elvis);
        System.out.println(Elvis.INSTANCE);

    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

위의 `try ~ catch` 문은 Elvis 인스턴스를 파일로 저장하는 코드이고, 아래의 `try ~ catch` 문은 Elvis 인스턴스를 불러오는 코드이다. 이 코드를 한번 실행해보자.

<p align="center">
  <img src=https://user-images.githubusercontent.com/72181693/161429127-0bab50be-17f9-4511-b615-2fc2ec0ce0b1.png><br>
  <em>그림 3) 위의 코드를 실행한 결과</em>
</p>

(그림 3)과 같이 다른 객체가 생성된 걸 확인할 수 있다.

역직렬화에서 새로운 객체를 생성하는 것을 막는 방법은 싱글턴 클래스 내부에 `readResolve()`라는 메서드를 구현하는 것이다. 그러면 `readObject()`를 실행하는 시점에 새로 생성된 객체는 GC로 던져버리고 readResolve()의 실행 결과로 받은 객체를 반환하게 된다. 따라서 다음과 같이 구현하면 된다.

```java
public class Elvis {
  public static final Elvis INSTANCE = new Elvis();
  private static boolean isCreated;

  private Elvis() {
    if (isCreated) {
        throw new UnsupportedOperationException();
    }
    created = true;
  }

	private Object readResolve() {
    return INSTANCE;
  }
}
```

위와 같이 구현하면 `readObject()`를 실행할 때 이미 생성된 Elvis 인스턴스를 반환받아 자바에서도 인스턴스가 하나임을 보증할 수 있다.
