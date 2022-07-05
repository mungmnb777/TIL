# HTTP 헤더

# 표현 → 리소스를 어떠한 방식으로 표현한다!

- Content-Type : 표현 데이터의 형식
- Content-Encoding: 표현 데이터의 압축 방식
- Content-Language : 표현 데이터의 자연 언어
- Content-Length: 표현 데이터의 길이

### Content-Type → 표현 데이터의 형식 설명

- 미디어 타입, 문자 인코딩
- content body에 무슨 내용이 들어가는가??

### Content-Encoding → 표현 데이터 인코딩

- 표현 데이터를 압축하기 위해 사용

### Content-Language → 표현 데이터의 자연 언어

- 표현 데이터의 자연 언어

### Content-Length → 표현데이터의 길이

- 바이트 단위

---

# 협상(Content Negotiation)

- Accept : 클라이언트가 선호하는 미디어 타입 전달
- Accept-Charset: 클라이언트가 선호하는 문자 인코딩
- Accept-Encoding: 클라이언트가 선호하는 압축 인코딩
- Accept-Language: 클라이언트가 선호하는 자연 언어

### 협상과 우선순위 1 - Quality Values(q)

- Quality Values(q) 값 사용
- 0~1, 클수록 높은 우선순위
- 생략하면 1

### 협상과 우선순위 2

- 구체적인 것이 우선한다.
- Accept: **text/_, text/plain, text/plain;format=flowed,_/\***
  1. text/plain;format=flowed
  2. text/plain
  3. text/\*
  4. **/**

### 협상과 우선순위 3

- 구체적인 것을 기준으로 미디어 타입을 맞춘다.

---

# 전송 방식

- 단순 전송
- 압축 전송
- 분할 전송
- 범위 전송

### 단순 전송

- Content-Length의 길이를 알 수 있을 때 사용
- 한 번에 요청하고 한 번에 다 받는 전송 방식

### 압축 전송

- gzip같은 압축 방식으로 content body를 압축한다. (Content-Encoding 사용)
- 그리고 압축된 body를 전송하는 방식

### 분할 전송

- Transfer-Encoding: chunked를 사용한다.
- 용량이 되게 큰 컨텐츠를 분할해서 전송한다.
- Content-Length를 사용하지 않음(예측이 힘듦)

### 범위 전송

- 클라이언트가 헤더에 Ranges를 넣어 요청한다.
- 그럼 서버가 해당하는 Ranges만큼의 content body를 전송한다.

---

# 일반 정보

### From → 유저 에이전트의 이메일 정보

- 일반적으로 잘 사용되지 않음
- 검색 엔진 같은 곳에서 주로 사용
- 요청에서 사용

### Referer → 이전 웹 페이지 주소

- 현재 요청된 페이지의 이전 웹 페이지 주소
- A → B로 이동하는 경우 B를 요청할 때 Referer:A를 포함해서 요청
- Referer를 사용해서 유입 경로 분석 가능
- 요청에서 사용

### User-Agent → 유저 에이전트 애플리케이션 정보

- 클라이언트의 애플리케이션 정보
- 통계 정보
- 어떤 종류의 브라우저에서 장애가 발생하는지 파악 가능
- 요청에서 사용

### Server → 요청을 처리하는 ORIGIN 서버의 소프트웨어 정보

- Origin 서버 - 진짜 나의 요청을 받고 응답을 내주는 서버
- 응답에서 사용

### Date → 메시지가 발생한 날짜와 시간

- 응답에서 사용

---

# 특별한 정보

### Host → 요청한 호스트 정보(도메인)

- 요청에서 사용
- 필수
- 하나의 서버가 여러 도메인을 처리해야 할 때
- 하나의 IP 주소에 여러 도메인이 적용되어 있을 때

### Location → 페이지 리다이렉션

- 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면 Location 위치로 자동 이동

### Allow → 허용 가능한 HTTP 메서드

- 405(Method Not Allowed) 에서 응답에 포함해야 함

### Retry-After → 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간

- 503 (Service Unavailable): 서비스가 언제까지 불능인지 알려줄 수 있음

---

# 인증

### Authorization → 클라이언트 인증 정보를 서버에 전달

- Authorization: Basic ****\_\_****(Oauth 인증 등 여러 인증 방법)

### WWW-Authenticate → 리소스 접근 시 필요한 인증 방법 정의

- 리소스 접근 시 필요한 인증 방법 정의

---

# 쿠키

- Set-Cookie: 서버에서 클라이언트로 쿠키 전달(응답)
- Cookie: 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청 시 서버로 전달

### Statless

- HTTP는 무상태 프로토콜이다.
- 클라이언트와 서버가 요청과 응답을 주고 받으면 연결이 끊어진다.
- 클라이언트가 다시 요청하면 서버는 이전 요청을 기억하지 못한다.
- 클라이언트와 서버는 서로 상태를 유지하지 않는다.

→ 이러한 특성 때문에 원래는 로그인 상태를 유지하지 못한다!

### 쿠키

- 사용처
  - 사용자 로그인 세션 관리
  - 광고 정보 트래킹
- 쿠키 정보는 항상 서버에 전송됨
  - 네트워크 트래픽 추가 유발
  - 최소한의 정보만 사용(세션 id, 인증 토큰)
  - 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리지 참고

### 쿠키 생명주기

- Set-Cookie : expires = ~~~ GMT
  - 만료일이 되면 쿠키 삭제
- Set-Cookie: max-age=3600 (3600초)
  - 0이나 음수를 지정하면 쿠키 삭제
- 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료 시까지만 유지
- 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지

### 쿠키 도메인

- 명시 : 명시한 문서 기준 도메인 + 서브 도메인 포함
  - domain=example.org를 지정해서 쿠키 생성
  - example.org는 물론이고
    - dev.example.org도 쿠키 접근
- 생략 : 현재 문서 기준 도메인만 적용
  - example.org에서 쿠키를 생성하고 domain 지정을 생략
    - example.org에서만 쿠키 접근 가능

### 쿠키 경로

- 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
- 일반적으로 path=/ 루트로 지정

### 쿠키 보안

- Secure
  - 쿠키는 http, https를 구분하지 않고 전송
  - Secure를 적용하면 https인 경우에만 전송
- HttpOnly
  - XSS 공격 방지
  - 자바스크립트에서 접근 불가
  - HTTP 전송에만 사용
- SameSite
  - XSRF 공격 방지
  - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키 전송
