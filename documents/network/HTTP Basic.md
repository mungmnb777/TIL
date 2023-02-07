# HTTP Basic 인증

강의: 스프링 시큐리티 OAuth2
날짜: 2023년 2월 2일
카테고리: spring

- HTTP는 액세스 제어와 인증을 위한 프레임워크를 제공하며 가장 일반적인 인증 방식은 `Basic` 인증 방식이다.
- RFC 7235 표준이며 인증 프로토콜은 HTTP 인증 헤더에 기술되어 있다.

## 인증 순서

1. 클라이언트는 인증 정보 없이 서버로 접속을 시도한다.
2. 서버가 클라이언트에게 인증 요구를 보낼 때 `401 Unauthorized` 응답과 함께 `WWW-Authenticate` 헤더를 기술해서 realm(보안 영역)과 Basic 인증 방법을 보냄.
3. 클라이언트가 서버로 접속할 때 Base64로 username과 password를 인코딩하고 Authorization 헤더에 담아서 요청한다.
4. 성공적으로 완료되면 정상적인 상태 코드를 반환한다.

## 주의사항

- base64 인코딩된 값은 쉽게 디코딩할 수 있기 때문에 인증 정보가 노출된다.
- 따라서 HTTP Basic 인증은 반드시 TLS와 함께 사용해야한다.

## HttpBasicConfigurer

- HTTP Basic 인증에 대한 초기화를 진행하며 속성들에 대한 기본값들을 설정한다.
- 기본 AuthentictaionEntryPoint는 BasicAuthenticationEntryPoint이다.
- 필터는 BasicAuthenticationFilter를 사용한다.

## BasicAuthenticationFilter

- 이 필터는 기본 인증 서비스를 제공하는 데 사용된다.
- BasicAuthenticationConverter를 사용해서 요청 헤더에 기술된 인증 정보의 유효성을 체크하며 Base64 인코딩된 username과 password를 추출한다.
- 인증이 성공하면 Authentication이 SecurityContext에 저장되고 인증이 실패하면 Basic 인증을 통해 다시 인증하라는 메세지를 표시하는 BasicAuthenticationEntryPoint가 호출된다.
- 인증 이후 세션을 사용하는 경우 매 요청마다 인증 과정을 거치지 않지만 사용하지 않는 경우에는 인증 과정을 거쳐야 한다.