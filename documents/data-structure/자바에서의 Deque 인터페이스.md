# Java에서의 Deque 인터페이스

자바 1.6에 `Deque`라는 인터페이스가 등장했다.

![Untitled](Java%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A5%E1%84%8B%E1%85%B4%20Deque%20%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%90%E1%85%A5%E1%84%91%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%B3%2098d7a2b26a59446ba9a05bb9cd443e67/Untitled.png)

`Deque`은 Head Element와 Tail Element 모두 추가, 삭제가 가능한 자료구조이다.

특별한 점으로는 알려진 `Deque`은 Capacity Limit이 없지만, 자바의 `Deque`은 제한을 걸 수도, 걸지 않을 수도 있다.

![Untitled](Java%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A5%E1%84%8B%E1%85%B4%20Deque%20%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%90%E1%85%A5%E1%84%91%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%B3%2098d7a2b26a59446ba9a05bb9cd443e67/Untitled%201.png)

`Deque`는 Queue를 상속받고 있다. 따라서 선입선출 기능을 구현해야한다.

![Untitled](Java%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A5%E1%84%8B%E1%85%B4%20Deque%20%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%90%E1%85%A5%E1%84%91%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%B3%2098d7a2b26a59446ba9a05bb9cd443e67/Untitled%202.png)

또한 `Deque`은 스택의 역할도 할 수 있어야 한다. 따라서 후입선출 기능을 구현해야한다.

근데 보통 Stack은 이미 자바 1.0때부터 구현된 `Stack` 클래스를 사용할 수 있다. 근데 위의 문서를 보면 기존의 스택 클래스보다 `Deque`을 써야한다고 쓰여져있다.

그 이유는 `Stack`이 `Vector` 클래스를 상속받고 있기 때문이다.

→ 대부분의 메서드가 동기화되어있기 때문에 실행 성능이 떨어진다.

```java
public synchronized boolean add(E e) {
    modCount++;
    add(e, elementData, elementCount);
    return true;
}
```

```java
public synchronized E remove(int index) {
    modCount++;
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);
    E oldValue = elementData(index);

    int numMoved = elementCount - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--elementCount] = null; // Let gc do its work

    return oldValue;
}
```

`Stack`도 마찬가지로 메서드를 동기화하여 사용하기 때문에 일반적인 환경에서 `Deque`에 비해 성능이 떨어진다.

`Deque`의 구현체는 `LinkedList`와 `ArrayDeque`가 있다.

보통 `LinkedList`를 많이 사용했는데, 성능이 좋은건 `ArrayDeque`가 더 성능이 좋다고 한다. `ArrayDeque`는 배열로 Deque를 구현한 것인데, 수행속도도 참조 지역성의 원리에 의해 캐싱에 유리해서 더 빠르고 메모리 효율도 높다. 즉 `Deque`의 구현체는 `ArrayDeque`를 선택하는 것이 좋아보인다.