# 템플릿 메서드 패턴

## 1. 정의

GoF Design Patterns에서는 템플릿 메서드 패턴을 다음과 같이 정의한다.

> \*Defines the skeleton of an algorithm in a method, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithms structure.

메소드에서 알고리즘의 골격을 정의하고 일부 단계를 하위 클래스로 연기합니다. 템플릿 메서드를 사용하면 하위 클래스가 알고리즘 구조를 변경하지 않고 알고리즘의 특정 단계를 재정의할 수 있습니다.\*

>

이름 그대로 템플릿이라는 거대한 틀을 만들어두고 그 곳에 변하지 않으면서 여러 곳에서 재사용되는 코드들을 몰아둔다. 그 후 일부 변하는 부분을 별도로 호출해서 해결하는 디자인 패턴이다.

<p align="center">
    <a href="https://ko.wikipedia.org/wiki/%ED%85%9C%ED%94%8C%EB%A6%BF_%EB%A9%94%EC%86%8C%EB%93%9C_%ED%8C%A8%ED%84%B4">
        <img src="../images/Template Method Pattern.png"><br>
        <em>그림 1) UML 클래스 다이어그램</em>
    </a>
</p>

```java
public abstract class AbstractTemplate<T> {
    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            T result = call();

            trace.end(status);

            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
```

`AbstractTemplate`이라는 추상 클래스를 만든 후, 자식 클래스에서 call()이라는 메서드를 오버라이딩하여 사용할 수 있다. 이 때, try ~ catch문이나 LogTrace 객체는 위에서 정의된 알고리즘의 골격이고 변하지 않는 부분이다. call() 메서드는 핵심 로직이 들어가는 부분으로 자주 변경되는 로직이다.

## 2. 장점

중복되는 부분을 줄이기 때문에 변경이 일어날 때 바꿔야하는 부분을 최소화할 수 있다. 만약에 저 템플릿 부분의 코드에 무언가 변경이 일어날 때, AbstractTemplate 내부만 변경해주면 사용하는 모든 코드에서 동시에 다 변경할 수 있게 된다.

## 3. 단점

템플릿 메서드 패턴은 상속을 사용하기 때문에 기능마다 새로운 클래스를 만들거나 익명 내부 클래스를 생성해야 한다. 이러한 부분은 복잡하고 코드도 지저분해보인다.

또한 상속은 자식 클래스와 부모 클래스가 컴파일 시점에 강하게 결합되기 때문에 유연성이 떨어진다.
