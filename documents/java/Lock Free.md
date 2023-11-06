# Lock Free

날짜: 2023년 11월 6일
카테고리: java

## Lock의 문제점

- 데드락
    - 어플리케이션에 락이 많을수록 데드락이 걸릴 확률이 높아진다.
- 우선순위 역전
    - 리소스 하나와 그 리소스의 락을 공유하는 쓰레드가 두 개일 때 발생
    - 쓰레드 하나가 운영체제에 의해 우선순위가 낮게 평가된다.
    - 낮은 우선순위의 쓰레드가 락을 획득하게 되면, 스케줄링 되지 않아 락이 해제되지 않을 수도 있다.
- 쓰레드가 락을 지닌채 죽거나 인터럽트 되는 경우
    - 모든 쓰레드가 중단된다.
        
        → 임계 영역에 타임 아웃을 거는 등 복잡한 코드가 요구된다.
        
- 성능
    - 락을 얻기 위한 쓰레드 간 다툼이 발생하기 때문에 성능에 오버헤드가 발생함

## 락이 필요한 이유

- 코드를 원자적 연산으로 만들어야 하는 경우
    - count++의 경우 세 번의 연산이 필요함.
        1. count를 읽는다.
        2. 새로운 값을 계산한다.
        3. count에 새로운 값을 저장한다.
        
        → 이 때 다른 쓰레드가 count 값을 수정할 수 있기 때문에 오류가 발생할 수 있음
        

## Lock Free

- 락을 사용하지 않고 단일 하드웨어 명령어로 실행될 수 있게 보증하는 솔루션
    
    → 의미상 원자적이기 때문에 쓰레드 안전을 보장한다.
    

## java.util.concurrent.atomic

- AtomicInteger
    
    ```java
    int initialValue = 0;
    AtomicInteger atomicInteger = new AtomicInteger(initialValue);
    
    atomicInteger.incrementAndGet(); // ++initialValue 의 단일 하드웨어 명령어
    atomicInteger.getAndIncrement(); // initialValue++ 의 단일 하드웨어 명령어
    ```
    
- AtomicReference
    
    ```java
    String old = "old name";
    String recent = "recent name";
    
    AtmoicReference<String> atomicReference = new AtomicReference<>(old);
    
    if (atomicReference.compareAndSet(oldName, newName)) {
    	System.out.println(atomicReference.get()); // 현재 값이 "old name"이면 "recent name"으로 변경된 값이 나올 것이다.
    }
    ```