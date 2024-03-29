# 2. JVM 이야기

## 2.1. 인터프리팅과 클래스로딩

JVM은 스택 기반의 해석 머신이다. 레지스터는 없지만 일부 결과를 실행 스택에 보관하면서 이 스택의 맨 위에 쌓인 값들을 가져와 계산한다.

- **`java HelloWorld` 명령을 통해 자바 애플리케이션을 실행한 경우 발생하는 작업**
    1. OS가 가상 머신 프로세스(자바 바이너리)를 구동한다.
    2. 자바 가상 환경이 구성되고 스택 머신을 초기화한다.
    3. `HelloWorld.class`가 실행된다. 이 때 제어권을 이 클래스로 넘기려면 가상 머신이 실행 되기전에 이 클래스를 로드(메모리에 적재하는 것)해야 한다. → 이것을 클래스 로더가 관여한다.
- **클래스로더가 하는 작업**
    1. 자바 프로세스가 초기화된다.
    2. `부트스트랩 클래스로더`가 런타임 코어 클래스를 로드한다.
        - 부트스트랩 클래스로더는 다른 클래스로더가 나머지 시스템에 필요한 클래스를 로드할 수 있게 하기 위해 최소한의 필수 클래스(`java.lang.Object`, `Class`, `Classloader`)를 로드하는 역할을 한다.
        - 런타임 코어 클래스는 자바 8 이전까지는 `rt.jar` 파일에서 가져오지만, 자바 9 이후부터는 런타임이 모듈화되고 클래스로딩 개념 자체가 달라짐
    3. 다음은 `확장 클래스로더`가 부트스트랩 클래스로더를 자기 부모로 설정하고 필요할 때 클래스로딩 작업을 부모에게 넘긴다.
        - 확장 클래스로더는 특정 OS나 플랫폼에 네이티브 코드를 제공하고 기본 환경을 오버라이드할 수 있다.
        - 자바 8에서 탑재된 자바스크립트 런타임 `Nashorn`을 로드한다.
    4. `애플리케이션 클래스로더`가 지정된 클래스패스에 위치한 유저 클래스를 로드한다.

## 2.2. 바이트코드 실행

자바 소스 코드의 실행의 첫 단계는 자바 컴파일러 `javac`를 이용해 컴파일하는 것이다. 해당 프로세스를 거치면 자바 소스 코드가 바이트코드로 가득 찬 `.class` 파일로 바뀐다.

바이트코드는 특정 컴퓨터 아키텍처에 특정하지 않은 `중간 표현형(Intermediate Representation)`이다. 그렇기 때문에 컴파일된 소프트웨어는 JVM 지원 플랫폼 어디서건 실행할 수 있고 자바 언어에 대해서도 추상화되어 있다.

컴파일러가 생성한 클래스 파일은 VM 명세서에 정의된 구조를 갖추고 있다. JVM은 클래스를 로드할 때 올바른 형식을 준수하고 있는지 검사한다.

| 컴포넌트 | 설명 |
| --- | --- |
| 매직 넘버(Magic Number) | 0xCAFEBABE : 클래스 파일임을 나타내는 4 Bytes 16진수 숫자 |
| 클래스 파일 포맷 버전 | 클래스 파일의 메이저/마이너 버전|
| 상수 풀(Constant Pool) | 클래스 상수들이 모여있는 위치|
| 액세스 플래그(Access Flag) | 추상 클래스, 정적 클래스 등 클래스 종류를 표시|
| this 클래스 | 현재 클래스명 |
| 슈퍼클래스(Superclass) | 슈퍼클래스(부모클래스)명 |
| 인터페이스(Interface) | 클래스가 구현한 모든 인터페이스 |
| 필드(Field) | 클래스에 들어 있는 모든 필드 |
| 메서드(Method) | 클래스에 들어 있는 모든 메서드 |
| 속성(Attribute) | 클래스가 지닌 모든 속성(예: 속성 파일명) |

### 2.2.1. 클래스 파일 포맷 버전
클래스를 실행하는 대상 JVM이 컴파일한 JVM보다 버전을 비교하고 호환되지 않는 버전의 클래스 파일을 만나면 런타임에 UnsupportedClassVersionError 예외를 일으킨다. 

### 2.2.2. 상수 풀
JVM은 코드를 실행할 때 런타임에 배치된 메모리 대신 이 상수 풀 테이블을 먼저 찾아보고 필요한 값을 참조한다. 

### 2.2.3. 엑세스 플래그
    
현재 명세서에는 8개의 클래스 접근 플래그가 있다.

- ACC_PUBLIC (0x0001)
- ACC_FINAL (0x0010)
- ACC_SUPER (0x0020)
- ACC_INTERFACE (0x0200)
- ACC_ABSTRACT (0x0400)
- ACC_SYNTHETIC (0x1000)
- ACC_ANNOTATION (0x2000)
- ACC_ENUM (0x4000) 

## 2.3. 핫스팟 입문

핫스팟 JVM이 나오면서 자바는 C/C++에 필적한 성능을 자랑하며 진화했다. C/C++는 제로-오버헤드 원칙을 준수하기 때문에 성능 자체는 되게 좋지만 컴퓨터와 OS가 실제로 어떻게 작동해야 하는지 언어 유저가 아주 세세한 저수준까지 일러주어야 한다.

자바는 이러한 제로-오버헤드 추상화 철학을 동조하지 않는다. 핫스팟 VM은 프로그램의 런타임 동작을 분석하고 성능에 유리한 방향으로 최적화를 적용하는 가상 머신이다.

### 2.3.1. JIT 컴파일이란?

자바 프로그램은 인터프리터가 가상화된 스택 머신에서 명령어를 실행하며 시작된다. 프로그램이 성능을 최대로 내려면 네이티브 기능을 활용해 CPU에서 직접 프로그램을 실행시켜야 한다.

이를 위해 핫스팟은 자주 사용되는 프로그램 단위(메서드와 루프)를 인터프리티드 바이트코드에서 네이티브 코드로 컴파일한다. 이것이 JIT 컴파일이다.

핫스팟은 인터프리티드 모드로 실행하는 동안 애플리케이션을 모니터링하면서 가장 자주 실행되는 코드 파트를 발견해 JIT 컴파일을 수행한다.

다음은 JIT 컴파일러가 코드 최적화하는 방법이다.

1. **인라이닝**
    
    인라이닝은 소형 메소드 트리가 해당 호출자 트리로 병합되거나 "인라인되는" 프로세스입니다. 이를 통해 자주 실행되는 메소드 호출의 속도가 향상됩니다. 현재 최적화 레벨에 따라 강도 레벨이 다른 두 개의 인라이닝 알고리즘이 사용됩니다. 이 단계에서 수행되는 최적화에는 다음이 포함됩니다.
    
    - 단순 인라이닝
    - 호출 그래프 인라이닝
    - 후미 순환 제거
    - 가상 호출 보호 최적화

2. **로컬 최적화**
    
    로컬 최적화에서는 한 번에 하나의 작은 코드 섹션을 분석하고 개선합니다. 여러 로컬 최적화 구현 시 일반 정적 컴파일러에서 사용되는 기술을 시도하고 테스트했습니다. 최적화에는 다음이 포함됩니다.
    
    - 로컬 데이터 플로우 분석 및 최적화
    - 레지스터 사용 최적화
    - Java 관용 표현 단순화
    
    이러한 기술은 특히 많은 개선 기회가 제시될 수 있는 글로벌 최적화 후에 반복적으로 적용됩니다.
    
3. **제어 플로우 최적화**
    
    제어 플로우 최적화는 메소드(또는 메소드의 특정 섹션) 내부의 제어 플로우를 분석하고 코드 경로를 재배열하여 효율성을 향상시킵니다. 최적화의 내용은 다음과 같습니다.
    
    - 코드 다시 정렬, 분할 및 제거
    - 루프 감소 및 반전
    - 루프 진행 속도 조정 및 루프 고정 코드 동작
    - 루프 롤 해제 및 분리
    - 루프 버전화 및 특수화
    - 예외 지시 최적화
    - 전환 분석

4. **글로벌 최적화**
    
    글로벌 최적화는 전체 메소드에서 동시에 작동합니다. 글로벌 최적화는 더 많은 컴파일 시간이 필요한 "비용이 많이 드는" 작업이지만 성능을 크게 향상시킬 수 있습니다. 최적화의 내용은 다음과 같습니다.
    
    - 글로벌 데이터 플로우 분석 및 최적화
    - 부분 중복 제거
    - 이스케이프 분석
    - GC 및 메모리 할당 최적화
    - 동기화 최적화
    
5. **원시 코드 생성**
    
    원시 코드 생성 프로세스는 플랫폼 아키텍처에 따라 다양합니다. 일반적으로 이 컴파일 단계 동안에는 메소드 트리가 시스템 코드 명령어로 변환되고 아키텍처 특성에 따라 일부 소규모 최적화가 수행됩니다. 컴파일된 코드는 `코드 캐시`라는 JVM 프로세스 공간의 파트에 배치됩니다. 향후 호출 시 컴파일된 코드가 호출될 수 있도록 코드 캐시에 있는 메소드의 위치가 기록됩니다. 임의의 지정된 시간에 JVM 프로세스는 JVM 실행 파일과 JVM의 바이트 코드 인터프리터에 동적으로 링크된 JIT 컴파일 코드 세트로 구성되어 있습니다.
    

## 2.4. JVM 메모리 관리

자바는 가비지 컬렉터라는 프로세스를 이용해 힙 메모리를 자동 관리하는 방식을 사용한다. 가비지 컬렉터는 JVM이 더 많은 메모리를 할당해야 할 때 불필요한 메모리를 회수하거나 재사용하는 불확정적 프로세스이다.

## 2.5. 쓰레딩과 자바 메모리 모델

자바의 멀티쓰레드 방식은 다음 세 가지 기본 설계 원칙에 기반한다.

- 자바 프로세스의 모든 쓰레드는 가비지가 수집되는 하나의 공용 힙을 가진다.
- 한 쓰레드가 생성한 객체는 그 객체를 참조하는 다른 쓰레드가 액세스할 수 있다.
- 기본적으로 객체는 변경 가능하다. 즉, 객체 필드에 할당된 값으 늪로그래머가 애써 final 키워드로 불변 표시하지 않는 한 바뀔 수 있다.

## 2.6. JVM 구현체 종류

오라클이 제작한 핫스팟 이외에도 제각기 다른 방법으로 구현한 다양한 구현체가 있다.

- OpenJDK
- 오라클 자바
- Zulu
- IcedTea
- Zing
- J9
- Avian
- Android