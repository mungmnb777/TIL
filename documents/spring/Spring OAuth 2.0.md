# Spring OAuth 2.0

## 1. 클라이언트 권한 부여 요청 시작

1. 클라이언트가 인가 서버로 권한 부여 요청을 하거나 토큰 요청을 할 경우 클라이언트 정보 및 엔드포인트 정보를 참조해서 전달한다.
2. application.properties 환경설정 파일에 클라이언트 설정과 인가서버 엔드포인트 설정을 한다.
3. 초기화가 진행되면 application.properties에 있는 클라이언트 및 엔드포인트 정보가 OAuth2ClientProperties의 각 속성에 바인딩된다.
4. OAuth2ClientProperties에 바인딩되어있는 속성의 값은 인가 서버로 권한 부여 요청을 하기 위한 ClientRegistration 클래스의 필드에 저장된다.
5. OAuth2Client는 ClientRegistration을 참조해서 권한 부여 요청을 위한 매개 변수를 구성하고 인가 서버와 통신한다.

## 2. ClientRegistration

- OAuth 2.0 또는 OpenID Connect 1.0 Provider에서 클라이언트의 등록 정보를 나타낸다.
- ClientRegistration은 OpenID Connect Provider의 설정 엔드포인트나 인가 서버의 메타데이터 엔드포인트를 찾아 초기화할 수 있다.
- ClientRegistration의 메서드를 사용하면 아래 예제처럼 편리하게 ClientRegistration을 설정할 수 있다.
    
    ```java
    ClientRegistration clientRegistration = 
    	ClientRegistrations.fromIssuerLocation("https://idp.example.com/issuer").build();
    ```
    

## 3. ClientRegistrationRepository

- ClientRegistrationRepository는 OAuth 2.0 & OpenID Connect 1.0의 ClientRegistration 저장소 역할을 한다.
- 클라이언트 등록 정보는 궁극적으로 인가 서버가 저장하고 관리하는데 이 리포지토리는 인가 서버에 일차적으로 저장된 클라이언트 등록 정보의 일부를 검색하는 기능을 제공한다.
- 스프링 부트 2.X 자동 설정은 spring.security.oauth2.client.registration.{registrationId} 하위 프로퍼티를 ClientRegistration 인스턴스에 바인딩하며, 각 ClientRegistration 객체를 ClientRegistrationRepository 안에 구성한다.
- ClientRegistrationRepository의 디폴트 구현체는 InMemoryClientRegistrationRepository다.
- 자동 설정을 사용하면 ClientRegistrationRepository도 ApplicationContext 내 @Bean으로 등록하므로 필요하다면 원하는 곳에 의존성을 주입할 수 있다.

## 4. oauth2Login()

### Authorization Code 요청

- OAuth2AuthorizationRequestRedirectFilter
    - 클라이언트는 사용자의 브라우저를 통해 인가 서버의 권한 부여 엔드포인트로 리다이렉션하여 권한 코드 부여 플로우를 시작한다.
- 요청 매핑 URI
    - AuthorizationRequestMatcher : `/oauthh2/authorization/{registrationId}`
    - AuthorizationEndpointConfig : authorizationRequestBaseUri를 통해 재정의될 수 있다.
- DefaultOAuth2AuthorizationRequestResolver
    - 웹 요청에 대하여 OAuth2AuthorizationRequest 객체를 최종 완성한다.
    - `/oauth2/authorization/[registrationId}`와 일치하는지 확인해서 일치하면 registrationId를 추출하고 이를 사용해서 ClientRegistration을 가져와 OAuth2AuthorizationRequest를 빌드한다.
- OAuth2AuthorizationRequest
    - 토큰 엔드포인트 요청 파라미터를 담은 객체로 인가 응답을 연계하고 검증할 때 사용한다.
- OAuth2AuthorizationRequestRepository
    - 인가 요청을 시작한 시점부터 인가 요청을 받는 시점까지 OAuth2AuthorizationRequest를 유지해준다.

### Access Token 교환하기

- OAuth2LoginAuthenticationFilter
    - 인가서버로부터 리다이렉트되면서 전달된 code를 인가서버의 Access Token으로 교환하고 Access Token이 저장된 OAuth2LoginAuthenticationToken을 AuthentcationManager에 위임하여 UserInfo 정보를 요청해서 최종 사용자에 로그인한다.
    - OAuth2AuthorizedClientRepository를 사용하여 OAuthh2AuthorizedClient를 저장한다.
    - 인증에 성공하면 OAuth2AuthenticationToken이 생성되고 SecurityContext에 저장되어 인증 처리를 완료한다.
- 요청 매핑 URI
    - RequestMatcher : `/login/oauth2/code/*`
- OAuth2LoginAuthenticationProvider
    - 인가서버로부터 리다이렉트된 이후 프로세스를 처리하며 Access Token으로 교환하고 이 토큰을 사용하여 UserInfo 처리를 담당한다.
    - Scope에 openid가 포함되어 있으면 OidcAuthorizationCodeAuthenticationProvider를 호출하고 아니면 OAuth2AuthorizationCodeAuthenticationProvider를 호출하도록 제어한다.
- OAuth2AuthorizationCodeAuthenticationProvider
    - 권한 코드 부여 흐름을 처리하는 authenticationProvider
    - 인가서버에 Authorization Code와 AccessToken의 교환을 담당하는 클래스
- OidcAuthorizationCodeAuthenticationProvider
    - OpenID Connect 1.0 권한 코드 부여 흐름을 처리하는 AuthenticationProvider이며 요청 Scope에 openid가 존재할 경우 실행된다.
- DefaultAuthorizationCodeTokenResponseClient
    - 인가서버의 token 엔드 포인트로 통신을 담당하며 AccessToken을 받은 후 OAuth2AccessTokenResponse에 저장하고 반환한다.

## 5. User

### OAuth2UserService

- 액세스 토큰을 사용해서 UserInfo 엔드포인트 요청으로 최종 사용자의 속성을 가져오며 OAuth2User 타입의 객체를 리턴한다.
- 구현체로 DefaultOAuth2UserService와 OidcUserService가 제공된다.
- `DefaultOAuth2UserService`
    - 표준 OAuth 2.0 Provider를 지원하는 OAuth2UserService 구현체다.
    - OAuth2UserRequest에 Access Token을 담아 인가서버와 통신 후 사용자의 속성을 가지고 온다.
    - 최종 OAuth2User 타입의 객체를 반환한다.
- `OidcUserService`
    - OpenID Connect 1.0 Provider를 지원하는 OAuth2UserService 구현체다.
    - OidcUserRequest에 있는 ID Token을 통해 인증 처리를 하며 필요 시 DefaultOAuth2UserService를 사용해서 UserInfo 엔드포인트의 사용자 속성을 요청한다.
    - 최종 OidcUser 타입의 객체를 반환한다.