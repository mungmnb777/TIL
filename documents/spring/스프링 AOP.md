# 스프링 AOP

## 1. AOP란?

[위키백과](https://ko.wikipedia.org/wiki/%EA%B4%80%EC%A0%90_%EC%A7%80%ED%96%A5_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D)에서는 AOP를 다음과 같이 정의한다.

> [컴퓨팅](https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%93%A8%ED%8C%85)에서 **관점 지향 프로그래밍**(aspect-oriented programming, AOP)은 [횡단 관심사](https://ko.wikipedia.org/wiki/%ED%9A%A1%EB%8B%A8_%EA%B4%80%EC%8B%AC%EC%82%AC)(cross-cutting concern)의 [분리](https://ko.wikipedia.org/wiki/%EA%B4%80%EC%8B%AC%EC%82%AC%EC%9D%98_%EB%B6%84%EB%A6%AC)를 허용함으로써 [모듈성](https://ko.wikipedia.org/wiki/%EB%AA%A8%EB%93%88%EC%84%B1)을 증가시키는 것이 목적인 [프로그래밍 패러다임](https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%ED%8C%A8%EB%9F%AC%EB%8B%A4%EC%9E%84)이다. 코드 그 자체를 수정하지 않는 대신 기존의 코드에 추가 동작(어드바이스)을 추가함으로써 수행하며, "함수의 이름이 'set'으로 시작하면 모든 함수 호출을 기록한다"와 같이 어느 코드가 [포인트컷](https://ko.wikipedia.org/w/index.php?title=%ED%8F%AC%EC%9D%B8%ED%8A%B8%EC%BB%B7&action=edit&redlink=1)(pointcut) 사양을 통해 수정되는지를 따로 지정한다. 이를 통해 기능의 코드 핵심부를 어수선하게 채우지 않고도 [비즈니스 로직](https://ko.wikipedia.org/wiki/%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4_%EB%A1%9C%EC%A7%81)에 핵심적이지 않은 동작들을 프로그램에 추가할 수 있게 한다.

비즈니스 로직을 짜다보면 각 도메인마다 공통적으로 처리되는 부분들이 있다. 각 도메인의 비즈니스 로직을 보통 종단 관심이라고 생각하면, 로깅이나 보안과 같이 대부분의 도메인에서 공통적으로 사용되는 부분을 횡단 관심이라고 한다.

이러한 횡단 관심사를 그 기능과 이 기능을 어디에 적용할 지 선택하는 기능을 합해 모듈로 구현하였는데 이를 바로 관점(aspect)라고 한다. 관점은 핵심 비즈니스 로직에서 분리하여 재사용하는 것을 목적으로 한다.

<p align="center">
    <img src="../images/스프링 AOP_1.png"><br>
    <em>그림 1) 횡단 관심사 모듈화</em>
</p>

## 2. @Aspect 프록시

gradle 혹은 maven을 통해 `org.springframework.boot:spring-boot-starter-aop`라는 dependency를 추가하면 스프링은 `AnnotationAwareAsjpectJAutoProxyCreator`라는 자동 프록시 생성기를 사용할 수 있다. 자동 프록시 생성기는 스프링 컨테이너에 `Advisor`라는 빈이 등록되면 이를 자동으로 찾아와서 필요한 곳에 프록시를 생성하고 적용해준다. 그리고 `@Aspect`라는 애노테이션이 등록된 클래스를 찾아 `Advisor`로 만들어주는 역할도 한다.

요약하면 다음과 같다.

1. `@Aspect`를 보고 Advisor로 변환해서 저장한다.
2. Advisor를 기반으로 프록시를 생성한다.

### 2.1. @Aspect를 Advisor로 변환해서 저장하는 과정

<p align="center">
    <a href="https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8">
        <img src="../images/스프링 AOP_2.png"><br>
        <em>그림 2) Advisor 생성</em>
    </a>
</p>

1. 실행: 스프링 애플리케이션 로딩 시점에 자동 프록시 생성기를 호출한다.
2. @Aspect 빈 조회: 자동 프록시 생성기가 `@Aspect` 애노테이션이 붙은 스프링 빈을 모두 조회한다.
3. 어드바이저 생성: @Aspect 어드바이저 빌더를 통해 `@Aspect` 애노테이션 정보를 기반으로 어드바이저를 생성한다.
4. @Aspect 기반 어드바이저 저장: 생성한 어드바이저를 @Aspect 어드바이저 빌더 내부에 저장한다.

**@Aspect 어드바이저 빌더**

`BeanFactoryAspectJAdvisorsBuilder` 클래스이다. `@Aspect` 애노테이션 정보를 기반으로 포인트컷, 어드바이스, 어드바이저를 생성하고 보관하는 것을 담당한다.

### 2.2 어드바이저를 기반으로 프록시 생성

<p align="center">
    <a href="https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8">
        <img src="../images/스프링 AOP_3.png"><br>
        <em>그림 3) 프록시 생성</em>
    </a>
</p>

1. 생성: 스프링 빈 대상이 되는 객체를 생성한다. ( @Bean , 컴포넌트 스캔 모두 포함)
2. 전달: 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달한다.
3. Advisor 빈 조회: 스프링 컨테이너에서 Advisor 빈을 모두 조회한다.
4. @Aspect Advisor 조회: @Aspect 어드바이저 빌더 내부에 저장된 Advisor를 모두 조회한다.
5. 프록시 적용 대상 체크: 앞서 3, 4번 과정에서 조회한 Advisor 에 포함되어 있는 포인트컷을 사용해서 해당 객체가 프록시를 적용할 대상인지 아닌지 판단한다. 이때 객체의 클래스 정보는 물론이고, 해당 객체의 모든 메서드를 포인트컷에 하나하나 모두 매칭해본다. 그래서 조건이 하나라도 만족하면 프록시 적용 대상이 된다. 예를 들어서 메서드 하나만 포인트컷 조건에 만족해도 프록시 적용 대상이 된다.
6. 프록시 생성: 프록시 적용 대상이면 프록시를 생성하고 프록시를 스프링 빈으로 등록하고 만약 프록시 적용 대상이 아니라면 원본 객체를 스프링 빈으로 등록한다.
7. 빈 등록: 반환된 객체는 스프링 빈으로 등록된다
