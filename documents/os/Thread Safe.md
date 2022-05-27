# 1. Thread-Safe란?

위키백과에서는 Thread-Safe를 다음과 같이 정의한다.

> **스레드 안전**(thread safety)은 [멀티 스레드](https://ko.wikipedia.org/wiki/%EC%8A%A4%EB%A0%88%EB%93%9C) 프로그래밍에서 일반적으로 어떤 [함수](https://ko.wikipedia.org/wiki/%ED%95%A8%EC%88%98_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D))나 [변수](https://ko.wikipedia.org/wiki/%EB%B3%80%EC%88%98_(%EC%BB%B4%ED%93%A8%ED%84%B0_%EA%B3%BC%ED%95%99)), 혹은 [객체](https://ko.wikipedia.org/wiki/%EA%B0%9D%EC%B2%B4_(%EC%BB%B4%ED%93%A8%ED%84%B0_%EA%B3%BC%ED%95%99))가 여러 [스레드](https://ko.wikipedia.org/wiki/%EC%8A%A4%EB%A0%88%EB%93%9C)로부터 동시에 접근이 이루어져도 [프로그램](https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%93%A8%ED%84%B0_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8)의 실행에 문제가 없음을 뜻한다. 보다 엄밀하게는 하나의 함수가 한 스레드로부터 호출되어 실행 중일 때, 다른 스레드가 그 함수를 호출하여 동시에 함께 실행되더라도 각 스레드에서의 함수의 수행 결과가 올바로 나오는 것으로 정의한다.
> 

자바에서는 멀티 쓰레드 환경을 `Thread`클래스나 `Runnable` 인터페이스로 간편하게 이용할 수 있다. 대신에 동시성 문제에 노출될 수 있다. 동시성 문제는 여러 쓰레드가 같은 리소스에 접근해서 조작함으로써 그 리소스가 원치 않은 값으로 변하는 문제로 이로 인해 생기는 결함이 치명적인 결과로 이어질 수 있기 때문에 이에 대해 자세히 알고 쓰는 것이 중요하다.

# 2. Thread-Safe를 지키기 위한 방법

## 2.1. Synchronized

쓰레드가 동시 접근해서 교착 상태나 경쟁 상태를 일으킬 수 있는 변수나 메서드에 `synchronized` 키워드를 붙여주면 항상 하나의 쓰레드만 이용할 수 있다. 이를 비관적 잠금이라고 부르는데, 현재 자바의 버전이 올라갔음에도 비관적 락의 비용은 조금 비싼 편이다.

## 2.2. ****ReentrantLock****

`synchronized`와 비슷한데 시작과 끝을 `lock()` 메서드와 `unlock()` 메서드로 확실하게 명시할 수 있다는 점이 다르다. 이런 점으로 인해서 세부적으로 동기화가 가능하다는 장점이 있다.

## 2.3. java.util.concurrent API

synchronized와 ReentrantLock을 포함해 자바에서 동시성을 제어하는 대부분의 API들은 concurrent API에서 나온다. 보통 실무에 가면 HashMap도 ConcurrentHashMap으로 사용하고 동시성을 제어하기 위해서 필요한 부분이니 나중에 시간이 나면 공부하면 좋을 것 같다.