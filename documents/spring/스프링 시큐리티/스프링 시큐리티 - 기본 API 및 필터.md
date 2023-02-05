# 기본 API 및 필터

## 스프링 시큐리티 의존성 추가 시 일어나는 일

- 서버가 기동되면 스프링 시큐리티의 초기화 작업 및 보안 설정이 이루어진다.
- 별도의 설정이나 구현을 하지 않아도 기본적인 웹 보안 기능이 현재 시스템에 연동되어 작동한다.
    - 모든 요청은 인증이 되어야 자원에 접근이 가능하다.
    - 인증 방식은 폼 로그인 방식과 httpBasic 로그인 방식을 제공한다.
    - 기본 로그인 페이지 제공한다.
    - 기본 계정 한 개 제공한다.

## 인증 API

### Form Login

```java
protected void configure(HttpSecurity http) throws Exception {
	http.formLogin()
			// 사용자 정의 로그인 페이지
			.loginPage("/login.html")
			// 로그인 성공 후 이동 페이지
			.defaultSuccessUrl("/home")
			// 로그인 실패 후 이동 페이지
			.failureUrl("/login.html?error=true")
			// 아이디 파라미터명 설정
			.usernameParameter("username")
			// 패스워드 파라미터명 설정
			.passwordParameter("password")
			// 로그인 Form Action Url
			.loginProcessingUrl("/login")
			// 로그인 성공 후 핸들러
			.successHandler(loginSuccessHandler())
			// 로그인 실패 후 핸들러
			.failureHandler(loginFailureHandler())
}
```

### Form Login 인증 필터 : `UsernamePasswordAuthenticationFilter`

- **인증 과정**
    1. `UsernamePasswordAuthenticationFilter`로 요청 정보가 매칭되는지 확인
    2. `AntPathRequestMatcher(/login)`으로 url을 접속했는지 확인한다. (디폴트는 `/login`)
        - URL 매칭 성공 시 실제 인증 처리 과정으로 이동
        - URL 매칭 실패 시 필터에 걸러진다.
    3. 로그인 페이지에서 사용자가 아이디와 비밀번호를 입력하면 `Authentication` 객체에 담아 `AuthenticationManager`에 인증을 요청한다.
    4. `AuthenticationManager`는 인증 정보를 받아 `AuthenticationProvider`에 인증 처리를 위임한다.
        - 인증에 성공하면 `Authentication` 객체에 유저 정보와 권한을 담아 다시 `AuthenticationManager`에게 넘겨준다.
        - 인증에 실패하면 `AuthenticationException` 예외를 발생시킨다.
    5. `AuthenticationManager`는 `Authentication` 객체를 `SecurityContext`에 저장한다.
    6. `SuccessHandler`가 성공 후 처리 로직을 실행한다.

## Logout

- 클라이언트의 로그아웃 요청 시 서버에서 일어나는 작업
    - 세션 무효화
    - 인증 토큰 삭제
    - `SecurityContext`의 객체 삭제
    - 쿠키 정보 삭제
    - 로그인 페이지로 리다이렉트
- `http.logout()`을 사용하면 로그아웃 기능이 작동한다.
- 스프링 시큐리티에서는 기본적으로 로그아웃을 `POST` 메서드로 작동한다.
- **로그아웃 과정**
    1. 클라이언트 로그아웃 요청을 햐면 `LogoutFilter`가 요청을 받는다.
    2. `AntPathRequestMatcher(/logout)`을 통해 Resource가 매칭되는지 확인한다.
    3. `SecurityContext`로부터 `Authentication` 객체를 가져온다.
    4. `SecurityContextLogoutHandler`가 세션 무효화, 쿠키 삭제, 시큐리티 컨텍스트 초기화를 한다.
    5. `SimpleUrlLogoutSuccessHandler`를 통해 로그인 페이지로 리다이렉트한다.

## Remember Me

- 세션이 만료되고 웹 브라우저가 종료되어도 어플리케이션이 사용자를 기억하는 기능
- Remember-Me 쿠키에 대한 http 요청을 확인한 후 토큰 기반 인증을 사용해 유효성을 검사하고 토큰이 검증되면 사용자는 로그인된다.
- 사용자 라이프 사이클
    - 인증 성공 → Remember-Me 쿠키 설정
    - 인증 실패 → 쿠키 존재 시 쿠키 무효화
    - 로그아웃 → 쿠키 존재 시 쿠키 무효화

```java
protected void configure(HttpSecurity http) throws Exception {
		http.rememberMe()
				.rememberMeParameter("remember") // 기본 파라미터 명은 remember-me
				.tokenValiditySeconds(3600) // default는 14일
				.alwaysRemember(true) // 리멤버 미 기능이 활성화되지 않아도 항상 실행
				.userDetailService(userDetailsService)
}
```

## AnonymousAuthenticationFilter

- 익명 사용자 인증 처리 필터
- 익명 사용자와 인증 사용자를 구분해서 처리하기 위한 용도로 사용
- 화면에서 인증 여부를 구현할 떄 `isAnonymouse()`와 `isAuthenticated()`로 구분해서 사용
- 인증 객체를 세션에 저장하지 않는다.

## 동시 세션 제어

### 최대 세션 허용 개수(1개)가 초과하는 경우

- 이전 사용자 세션 만료
    1. 사용자 1이 로그인을 한다.
    2. 서버는 사용자 1에 대한 로그인 세션을 생성한다.
    3. 사용자 2가 로그인을 한다.
    4. 서버는 사용자 2에 대한 로그인 세션을 생성하고 사용자 1의 세션을 만료 설정한다.
    5. 사용자 1이 링크를 접속하면 세션이 만료되었다는 메세지를 보낸다.
- 현재 사용자 인증 실패
    1. 사용자 1이 로그인을 한다.
    2. 서버는 사용자 1에 대한 로그인 세션을 생성한다.
    3. 사용자 2가 로그인을 하면 최대 세션을 넘어서므로 인증 예외를 발생시킨다.

```java
protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.maximumSessions(1) // 최대 세션 허용 개수 (-1일 경우 무제한)
				.maxSessionsPreventsLogin(true) // true : 현 사용자 인증 실패, false : 기존 세션 만료
				.invalidSessionUrl("/invalid") // 세션이 유효하지 않을 때 이동 할 페이지
				.expiredUrl("/expired") // 세션이 만료된 경우 이동할 페이지
}
```

## 세션 고정 보호

### 세션 고정 공격이란?

1. 공격자가 웹 어플리케이션에 접속한다.
2. 공격자는 서버로부터 받은 JSESSIONID를 사용자의 브라우저 쿠키로 강제 변경한다.
3. 사용자가 공격자로부터 받은 세션 쿠키로 로그인을 시도 한다.
4. 로그인에 성공하면 공격자는 해당 JSESSIONID로 웹 어플리케이션에 접속하면 사용자 정보를 들여다 볼 수 있다.

### 해결방법

```java
protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionFixation().changeSessionId() // 기본값
																						 // none, migrateSession, newSession
}
```

- `changeSessionId()` : 로그인할 때 세션 ID를 변경한다. (서블릿 3.1 이상에서 사용)
- `migrateSession()` : 로그인할 때 세션 ID를 변경한다. (서블릿 3.1 이하에서 사용)
- `newSession()` : 세션이 새로 생성되고 JSESSIONID, 세션의 속성도 새로 설정된다.
- `none()` : 세션이 새로 생성되지 않는다.

## 세션 정책

```java
protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.If_Required)
}
```

- `SessionCreationPolicy.Always` : 스프링 시큐리티가 항상 세션 생성
- `SessionCreationPolicy.If_Required` : 스프링 시큐리티가 필요 시 생성(기본값)
- `SessionCreationPolicy.Never` : 스프링 시큐리티가 생성하지 않지만 이미 존재하면 사용
- `SessionCreationPolicy.Stateless` : 스프링 시큐리티가 생성하지 않고 존재해도 사용하지 않음

## 세션 제어 필터 - SessionManagementFilter

- 세션 관리 : 인증 시 사용자의 세션 정보를 등록, 조회, 삭제 등의 세션 이력을 관리
- 동시적 세션 제어 : 동일 계정으로 접속이 허용되는 최대 세션 수를 제한
- 세션 고정 보호 : 인증할 떄마다 세션 쿠키를 새로 발급하여 공격자의 쿠키 조작을 방지
- 세션 생성 정책

## 동시적 세션 처리 도우미 - ConcurrentSessionFilter

- 매 요청마다 현재 사용자의 세션 만료 여부 체크
- 세션이 만료되었을 경우 즉시 만료 처리
- `session.isExpired() == true`인 경우
    - 로그아웃 처리
    - 즉시 오류 페이지 응답

## 권한 설정 및 표현식

### 권한 설정

- 선언적 방식
    - URL
        
        ```java
        http.antMatchers("/users/**").hasRole("USER");
        ```
        
        | 메소드 | 동작 |
        | --- | --- |
        | authenticated() | 인증된 사용자의 접근을 허용 |
        | fullyAuthenticated() | 인증된 사용자의 접근을 허용, rememberMe 인증 제외 |
        | permitAll() | 무조건 접근을 허용 |
        | denyAll() | 무조건 접근을 허용하지 않음 |
        | anonymouse() | 익명 사용자의 접근을 허용 |
        | rememberMe() | 기억하기를 통해 인증된 사용자의 접근을 허용 |
        | access(String) | 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용 |
        | hasRole(String) | 사용자가 주어진 역할이 있다면 접근을 허용(ROLE prefix 없음) |
        | hasAuthority(String) | 사용자가 주어진 권한이 있다면 접근을 허용(ROLE prefix 있음) |
        | hasAnyRole(String…) | 사용자가 주어진 권한이 있다면 접근을 허용 |
        | hasAnyAuthority(String…) | 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용 |
        | hasIpAddress(String) | 주어진 IP로부터 요청이 왔다면 접근을 허용 |
    - METHOD
        
        ```java
        @PreAuthorize("hasRole('USER')")
        ```
        

- 동적 방식
    - URL
    - Method

## ExcecptionTranslationFilter

이 필터는 `FilterSecurityInterceptor`에 의해 발생한 인증 예외와 인가 예외를 처리한다. 

### AuthenticationException

- 인증 예외 처리
    1. AuthenticationEntryPoint 호출
        - 로그인 페이지 이동, 401 오류 코드 전달 등의 역할을 한다.
    2. 인증 예외가 발생하기 전의 요청 정보를 저장
        - RequestCache - 사용자의 이전 요청 정보를 세션에 저장하고 이를 꺼내오는 캐시 매커니즘
        - SavedRequest - 사용자가 요청했던 request 파라미터 값들, 그 당시의 헤더 값들 등이 저장

### AccessDeniedException

- 인가 예외 처리
    - AccessDeniedHandler에서 예외 처리하도록 제공

```java
protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler())
}
```

## CSRF, CsrfFilter

### CSRF(사이트 간 요청 위조) 공격

1. 사용자가 로그인 후 웹 어플리케이션(쇼핑몰)에서 쿠키를 발급 받는다.
2. 공격자가 임의의 링크(`http://www.~~~.com/attacker`)를 사용자에게 전달한다.
3. 사용자가 링크를 클릭하여 공격용 웹페이지에 접속한다.
4. 사용자가 공격용 페이지를 열면 브라우저는 이미지 파일을 받아오기 위해 공격용 URL을 연다.
    
    `<img src=http://shop.com/address=공격자주소">`
    
5. 사용자의 승인이나 인지 없이 배송지가 등록됨으로써 공격이 완료된다.

### CsrfFilter

- 모든 요청에 랜덤하게 생성된 토큰을 HTTP 파라미터로 요구
- 요청 시 전달되는 토큰 값과 서버에 저장된 실제 값과 비교한 후 만약 일치하지 않으면 요청은 실패한다.