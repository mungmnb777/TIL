# 1. 스프링 MVC란?

우선 스프링 MVC로 가기 전 자바로 구현된 MVC1 아키텍처와 MVC2 아키텍처에 대해서 한번 알아보자.

## 1.1. MVC1

MVC1은 뷰와 컨트롤러가 나뉘어지지 않은 아키텍처이다. 웹 개발을 할 때 보통 JSP가 뷰 역할을 하는데 MVC1 아키텍처에서는 JSP가 컨트롤러의 역할까지 맡는 것이다.

<p align="center">
<a href="https://onejuny.tistory.com/entry/JavaJsp-MVC-1-MVC-2-%EC%B0%A8%EC%9D%B4-%EB%B0%8F-%EC%9E%A5%EB%8B%A8%EC%A0%90">
  <img src="../images/Spring MVC_1.png"><br>
  <em>그림 1) MVC1 아키텍처</em>
</a>
</p>

하지만 이렇게 설계할 경우 JSP 내부에 자바 코드의 비율이 높아지면서 코드가 복잡해지고 가독성이 떨어진다는 단점을 가지고 있어서 서비스의 크기가 크고 유지보수가 많은 엔터프라이즈 어플리케이션에서는 사용하기 어렵다.

## 1.2. MVC2

MVC2는 JSP에서 도맡아하던 컨트롤러 역할을 서블릿이 수행하는 아키텍처를 말한다.

<p align="center">
<a href="https://onejuny.tistory.com/entry/JavaJsp-MVC-1-MVC-2-%EC%B0%A8%EC%9D%B4-%EB%B0%8F-%EC%9E%A5%EB%8B%A8%EC%A0%90">
  <img src="../images/Spring MVC_2.png"><br>
  <em>그림 2) MVC2 아키텍처</em>
</a>
</p>

이런 식으로 구현하게 되면 JSP의 코드 중 자바 코드의 비율이 줄어들어 가독성이 높아진다. 그렇기 때문에 유지보수나 디버깅도 편해진다는 장점이 있다.

## 1.3. 스프링 MVC

기존 MVC2 패턴에서 프론트 컨트롤러 패턴을 도입하고, 여러 중복되는 부분을 컨트롤러 코드에서 제거하여 개발자가 구현해야하는 부분을 획기적으로 줄이게 해준 스프링에서 제공하는 웹 프레임워크이다.

`@RequestMapping`이라는 애노테이션 기반의 아주 편리한 URL 및 어댑터 매핑 기술을 선보이면서 많은 개발자의 사랑을 받고 있다.

<p align="center">
  <img src="../images/Spring MVC_3.png"><br>
  <em>그림 3) 스프링 MVC의 구조</em>
</p>

### 1.3.1. 디스패처 서블릿

서블릿 컨테이너의 제일 앞단에 위치해서 클라이언트로부터 들어오는 모든 요청을 받아 이를 처리할 수 있는 컨트롤러를 찾아 전달하는 프론트 컨트롤러의 역할을 하는 클래스이다. 또 컨트롤러로부터 모델과 뷰 이름을 받아서 적절한 뷰 리소스에 모델과 함께 포워딩하는 역할도 해준다.

디스패처 서블릿이 등장하면서 web.xml이 하던 url 매핑을 대신 처리해주면서 개발자는 필요한 컨트롤러를 구현하고 적절한 애노테이션을 사용하기만 하면 장땡이다.

### 1.3.2. 핸들러 매핑

핸들러 매핑은 말 그대로 클라이언트에서 요청이 들어왔을 때 적절한 컨트롤러에 매핑시켜주는 인터페이스이다. 보통 주로 사용하는 구현체는 `RequestMappingHandlerMapping`이고 `@RequestMapping`을 이용하여 매핑시키는 방법이다. 우리가 `@Controller` 애노테이션을 붙여 작성한 클래스는 스프링 컨테이너에 등록된다.

이후에 `@RequestMapping`이 달린 메서드들은 `RequestMappingHandlerMapping` 객체의 `HandlerMethod`라는 객체에 저장이 된다.

<p align="center">
  <img src="../images/Spring MVC_4.png"><br>
  <em>그림 4) Handler Method 클래스</em>
</p>

그 후 요청이 들어오면 요청 받은 URI, 요청 메서드(GET, POST 등), consume, produce 등을 `RequestMappingInfo` 객체를 이용해 핸들러 메서드와 매핑한다.

### 1.3.3 핸들러 어댑터

디스패처 서블릿은 핸들러 어댑터를 통해서 요청을 위임한다. 그 이유는 공통 전후 처리 과정이 필요하기 때문인데 우리가 보통 컨트롤러 메서드에서 파라미터로 사용하는 `@RequestParam`이나 `@ModelAttribute` 등 여러 ArgumentResolver들을 핸들러 어댑터가 적절하게 처리해서 보낸다. 또한 인터셉터들도 여기서 모두 실행한 다음에 메서드를 호출하게 된다. 여기서 주로 사용하는 구현체는 `RequestMappingHandlerAdapter`이다. 위의 핸들러 매핑과 같이 `@RequestMapping` 애노테이션을 사용할 떄 쓰는 구현체로 현재 실무에서는 대부분 이 구현체를 사용한다.

<p align="center">
  <img src="../images/Spring MVC_5.png"><br>
  <em>그림 5) RequestMappingHandlerAdapter 클래스</em>
</p>

### 1.3.4. 뷰 리졸버

추후에 추가 예정
