# 프록시 패턴

[위키백과](https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9D%EC%8B%9C_%ED%8C%A8%ED%84%B4)에서는 프록시 패턴을 다음과 같이 정의한다.

> **프록시 패턴**(proxy pattern)은 [컴퓨터 프로그래밍](https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%93%A8%ED%84%B0_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D)에서 [소프트웨어 디자인 패턴](https://ko.wikipedia.org/wiki/%EB%94%94%EC%9E%90%EC%9D%B8_%ED%8C%A8%ED%84%B4)의 하나이다.
> <br>일반적으로 프록시는 다른 무언가와 이어지는 인터페이스의 역할을 하는 클래스이다. 프록시는 어떠한 것(이를테면 네트워크 연결, 메모리 안의 커다란 객체, 파일, 또 복제할 수 없거나 수요가 많은 리소스)과도 인터페이스의 역할을 수행할 수 있다.

<p align="center">
  <img src="../images/proxy pattern_1.png"><br>
  <em>그림 1) UML의 프록시</em>
</p>

위의 다이어그램에서 클라이언트는 RealSubject 클래스에게 요청을 한 것인지, 프록시에게 요청을 한 것인지 알 수 없다. 즉, 객체에서 프록시는 진짜 객체와 같은 인터페이스를 구현하여, 그 역할을 대신 수행하는 것을 말한다.

클라이언트 서버 모델에서도 마찬가지이다.

<p align="center">
  <img src="../images/proxy pattern_2.png"><br>
  <em>그림 2) 클라이언트 서버 모델에서의 프록시</em>
</p>

서버와 프록시는 같은 인터페이스를 사용한다. 이로 인해 클라이언트가 사용하는 서버 객체 대신 프록시 객체가 작업을 수행해도 클라이언트 코드는 이를 모르게 된다.

## 프록시의 주요 기능

프록시는 크게 2가지 일을 한다.

- 접근 제어
  - 권한에 따른 접근 차단
  - 캐싱
  - 지연 로딩
- 부가 기능 추가
  - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행
  - 예) 요청 값이나, 응답 값을 중간에 변경
  - 예) 실행 시간을 측정해서 추가 로그를 남긴다.

# 참고자료

[위키백과](https://ko.wikipedia.org/wiki/%ED%94%84%EB%A1%9D%EC%8B%9C_%ED%8C%A8%ED%84%B4)

[스프링 핵심 원리 - 고급편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8)
