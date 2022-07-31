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
    1. `UsernamePAsswordAuthenticationFilter`로 요청 정보가 매칭되는지 확인
    2. `AntPathRequestMatcher(/login)`으로 url을 접속했는지 확인한다. (디폴트는 `/login`)
        - URL 매칭 성공 시 실제 인증 처리 과정으로 이동
        - URL 매칭 실패 시 필터에 걸러진다.
    3. 로그인 페이지에서 사용자가 아이디와 비밀번호를 입력하면 `Authentication` 객체에 담아 `AuthenticationManager`에 인증을 요청한다.
    4. `AuthenticationManager`는 인증 정보를 받아 `AuthenticationProvider`에 인증 처리를 위임한다.
        - 인증에 성공하면 `Authentication` 객체에 유저 정보와 권한을 담아 다시 `AuthenticationManager`에게 넘겨준다.
        - 인증에 실패하면 `AuthenticationException` 예외를 발생시킨다.
    5. `AuthenticationManager`는 `Authentication` 객체를 `SecurityContext`에 저장한다.
    6. `SuccessHandler`가 성공 후 처리 로직을 실행한다.