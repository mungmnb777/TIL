## ArrayList의 필드

![Untitled](../images/Java%20ArrayList_1.png)

- `DEFAULT_CAPACITY` → 배열 초기 사이즈(Default)
- `EMPTY_ELEMENTDATA` → 초기 Capacity가 0일 경우 `elementData`에 할당
- `DEFAULTCAPACITY_EMPTY_ELEMENTDATA` → 기본 생성자일 경우 `elementData`에 할당
- `elementData` → 실제 데이터가 저장됨

## 기본 생성자

```java
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

## ArrayList는 배열인데 어떻게 리스트처럼 작동하지?

```java
private Object[] grow(int minCapacity) {
    int oldCapacity = elementData.length;
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        int newCapacity = ArraysSupport.newLength(oldCapacity,
                minCapacity - oldCapacity, /* minimum growth */
                oldCapacity >> 1           /* preferred growth */);
        return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
        return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
    }
}
```

## Capacity는 초기 생성자에서나 ensureCapacity() 메서드로 커스텀 가능하다.

```java
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
```

```java
public void ensureCapacity(int minCapacity) {
    if (minCapacity > elementData.length
        && !(elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
             && minCapacity <= DEFAULT_CAPACITY)) {
        modCount++;
        grow(minCapacity);
    }
}
```