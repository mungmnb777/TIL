# [이펙티브 자바 다시 시작] 아이템 11. equals를 재정의하려거든 hashCode도 재정의하라

날짜: 2023년 10월 18일
카테고리: java
책: effective-java

## hashCode 규약

- equals 비교에 사용하는 정보가 변경되지 않았다면 hashCode는 매번 같은 값을 리턴해야한다.
- 두 객체에 대한 equals가 같다면, hashCode의 값도 같아야 한다.
- 두 객체에 대한 equals가 다르더라도, hashCode의 값은 같을 수 있지만 해시 테이블 성능을 고려해 다른 값을 리턴하는 것이 좋다.

## hashCode 구현

```java
@Override 
public int hashCode() {
	int result = Short.hashCode(areaCode);
	result = 31 * result + Short.hashCode(prefix);
	result = 31 * result + Short.hashCode(lineNum);
	return result;
}
```

- 왜 이전의 값에 31을 곱할까?
    - 홀수를 써야한다.
        
        → 짝수를 써서 연산을 하면 오버플로우 발생 시 정보를 잃게 된다.
        
    - 31인 이유?
        
        → (i << 5) - i이기 때문에 최적화된 머신 코드이다.
        

## 해시 충돌이 더 적은 방법

- 구아바의 `com.google.common.hash.Hashing`

## 캐싱

- 클래스가 불변이고 해시코드를 계산하는 비용이 큰 경우 캐싱할 수 있다.