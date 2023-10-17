# [이펙티브 자바 다시 시작] 아이템 10. equals는 일반 규약을 지켜 재정의하라

날짜: 2023년 10월 17일
카테고리: java
책: effective-java

## 재정의하지 않아도 되는 경우

- 각 인스턴스가 본질적으로 고유하다.
    - 싱글턴 인스턴스
    - Enum
- 논리적 동치성을 검사할 일이 없는 경우.
- 상위 클래스에서 재정의한 equals가 하위 클래스에도 적절하다.
- 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다.
    - public 클래스는 equals가 호출이 안된다는 것을 보장하기 어렵다.

## equals 규약

- 반사성 : `x.equals(x)` → true
- 대칭성 : `x.equals(y) == y.equals(x)`
- 추이성 : `x.equals(y) == y.equals(z)` → `x.equals(z)`
    
    ```java
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof Point))
    		return false;
    
    	// o가 일반 Point면 색상을 무시하고 비교한다.
    	if (!(o instanceof ColorPoint))
    		return o.equals(this);
    
    	// o가 ColorPoint면 색상까지 비교한다.
    	return super.equals(i) && ((ColorPoint) o).color == color;
    }
    ```
    
    이 방식은 추이성을 깨뜨린다.
    
    ```java
    ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
    Point p2 = new Point(1, 2);
    ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
    
    p1.equals(p2) // true
    p2.equals(p3) // true
    p1.equals(p3) // false
    ```
    
    - (p1, p2), (p2, p3)는 색상을 무시하고 비교했지만, (p1, p3)는 색상을 고려했기 때문이다.
    - 이 방식은 무한 재귀에 빠질 수도 있다.
    - instanceof 검사를 getClass 검사로 바꾼다면?
        
        → `리스코프 치환 원칙` 위배
        
    - 이를 해결하기 위한 방법은 Composition을 사용한다.
- 일관성
    - 불변 객체인 경우, 두 객체가 같다면 영원히 같아야 한다.
- nulll-아님

## equals 구현 규칙

1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
2. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
3. 입력을 올바른 타입으로 형변환한다.
4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치히는지 하나씩 검사한다.