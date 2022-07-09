# URI

### “URI는 로케이터(locator), 이름(name) 또는 둘 다 추가로 분류될 수 있다.”

- Uniform : 리소스 식별하는 통일된 방식
- Resource : 자원, URI로 식별할 수 있는 모든 것(제한 없음)
- Identifier : 다른 항목과 구분하는데 필요한 정보

# URL

### 전체 문법

- scheme://[userinfo@]host[:port][/path][?query][#fragment]

### scheme

- 주로 프로토콜 사용
- 프로토콜 : 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙
- http는 80 포트, https는 443 포트를 주로 사용, 포트는 생략 가능
- https는 http에 보안을 추가.

### userinfo

- URL에 사용자 정보를 포함에서 인증
- 거의 사용하지 않음

### host

- 호스트명
- 도메인 명이나 IP 주소를 직접 사용 가능

### port

- 접속 포트
- 일반적으로 생략, 생략 시 http는 80, https는 443

### path

- 리소스 경로, 계층적 구조

### query

- key = value 형태
- ?로 시작, &로 추가 가능
- query parameter, query string 등으로 불림, 웹 서버에 제공하는 파라미터

---