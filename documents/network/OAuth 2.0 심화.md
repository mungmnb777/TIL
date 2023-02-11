# OAuth 2.0 심화

강의: 스프링 시큐리티 OAuth2
날짜: 2023년 2월 5일
카테고리: network

## 1. 개요

- OAuth = Open + Authorization
- The OAuth 2.0 Authorization Framework
    - OAuth 2.0 인가 프레임워크는 애플리케이션이 사용자 대신하여 사용자의 자원에 대한 제한된 액세스를 얻기 위해 승인 상호 작용을 함으로써 애플리케이션이 자체적으로 액세스 권한을 얻도록 한다.
    - 즉 사용자가 속한 사이트의 보호된 자원에 대하여 어플리케이션의 접근을 허용하도록 승인하는 것을 의미한다.
- Delegated Authorization Framework - 위임 인가 프레임워크
    - 어플리케이션이 사용자의 데이터에 접근하도록 권한을 부여한다.

## 2. OAuth 2.0 Roles

- Resource Owner(자원 소유자)
    - 보호된 자원에 대한 접근 권한을 부여할 수 있는 주체, 사용자로서 계정의 일부에 대한 접근 권한을 부여하는 사람
    - 사용자를 대신하여 작동하려는 모든 클라이언트는 먼저 사용자의 허가를 받아야 한다.
- Resource Server(보호 자원 서버)
    - 타사 어플리케이션에서 접근하는 사용자의 자원이 포함된 서버를 의미한다.
    - 액세스 토큰을 수락 및 검증할 수 있어야 하며 권한 체계에 따라 요청을 승인할 수 있어야 한다.
- Authorization Server(인가 서버)
    - 클라이언트가 사용자 꼐정에 대한 동의 및 접근을 요청할 때 상호 작용하는 서버로서 클라이언트의 권한 부여 요청을 승인하거나 거부하는 서버
    - 사용자가 클라이언트에게 권한 부여 요청을 승인한 후 Access Token을 클라이언트에게 부여하는 역할
- Client(클라이언트)
    - 사용자를 대신하여 권한을 부여받아 사용자의 리소스에 접근하려는 어플리케이션
    - 사용자를 권한 부여 서버로 안내하거나 사용자의 상호 작용 없이 권한 부여 서버로부터 직접 권한을 얻을 수 있다.

### 진행 순서

1. Resource Owner가 Authorization Server에 계정을 인증하여 Access Token을 Client에 전달한다. 이 때 Resource Owner는 Client에 제공할 자원에 대한 접근 범위를 설정할 수 있다.
2. Client는 Access Token을 받아 Resource Server에 해당 Resource Owner의 자원을 요청한다.
3. Resource Server에서 Access Token을 받아 정상적으로 인가된 요청인지 확인한 후 Client에 Resource를 제공한다.

## 3. OAuth 2.0 Client Types

- 개요
    - [RFC 6749](https://www.rfc-editor.org/rfc/rfc6749)
    - 인증 서버에 클라이언트를 등록할 때 클라이언트 자격 증명인 클라이언트 아이디와 클라이언트 암호를 받는다.
    - 클라이언트 암호는 비밀이고 그대로 유지되어야 하는 반면 클라이언트 아이디는 공개이다.
    - 이 자격 증명은 인증 서버에 대한 클라이언트 ID를 증명한다.
- 기밀 클라이언트
    - 기밀 클라이언트는 `client_secret`의 기밀성을 유지할 수 있는 클라이언트를 의미한다.
    - 일반적으로 사용자가 소스 코드에 액세스할 수 없는 서버에서 실행되는 응용 프로그램으로 .NET, Java, PHP 및 Node.js와 같은 서버 측 언어로 작성된다.
    - 이러한 유형의 애플리케이션은 대부분 웹 서버에서 실행되기 때문에 일반적으로 “웹 앱”이라고 한다.
- 공개 클라이언트
    - 공개 클라이언트는 `client_secret`의 기밀을 유지할 수 없으므로 이러한 앱에는 secret이 사용되지 않는다.
    - 브라우저에서 실행되는 Javascript 애플리케이션, Android 또는 iOS 모바일 앱, 데스크톱에서 실행되는 기본 앱뿐만 아니라 IoT/임베디드 장치에서 실행되는 애플리케이션이 있다.
    - Chrome 개발자 콘솔이나 디스어셈블러와 같은 디버깅 도구를 사용하여 바이너리/실행 코드에서 기밀 정보를 추출할 수 있기 때문에 공개로 간주된다.
    - 서버측이 아닌 리소스 소유자가 사용하는 장치에서 실행되는 모든 클라이언트는 공개 클라이언트로 간주되어야 한다.

## 4. OAuth 2.0 Token Types

- Access Token
    - 클라이언트에서 사용자의 보호된 리소스에 접근하기 위해 사용하는 일종의 자격 증명으로서 역할을 하며 리소스 소유자가 클라이언트에게 부여한 권한 부여의 표현이다.
    - 일반적으로 JWT 형식을 취하지만 사양에 따라 그럴 필요는 없다.
    - 토큰에는 해당 액세스 기간, 범위 및 서버에 필요한 기타 정보가 있다.
    - 타입에는 식별자 타입과 자체 포함 타입이 있다.
- Refresh Token
    - 액세스 토큰이 만료된 후 새 액세스 토큰을 얻기 위해 클라이언트 응용 프로그램에서 사용하는 자격 증명
    - 액세스 토큰이 만료되는 경우 클라이언트는 권한 부여 서버로 인증하고 Refresh Token을 전달한다.
    - 인증 서버는 Refresh hToken의 유효성을 검사하고 새 액세스 토큰을 발급한다.
    - Refresh Token은 액세스 토큰과 달리 권한 서버 토큰 엔드포인트에만 보내지고 리소스 서버에는 보내지 않는다.
- ID Token
- Authorization Code
    - 권한 부여 코드 흐름에서 사용되며 이 코드는 클라이언트가 액세스 토큰과 교환할 임시 코드이다.
    - 사용자가 클라이언트가 요청하는 정보를 확인하고 인가 서버로부터 리다이렉트 되어 받아온다.

## 5. OAuth 2.0 Grant Types

- 권한부여란 클라이언트가 사용자를 대신해서 사용자의 승인하에 인가서버로부터 권한을 부여받는 것을 의미한다.
- OAuth 2.0 메커니즘은 아래와 같은 권한 부여 유형들을 지원하고 있으며 일부는 Deprecated 되었다.
    1. Authorization Code Grant Type
        - 권한 코드 부여 타입, 서버 사이드 어플리케이션(웹 어플리케이션), 보안에 가장 안전한 유형
    2. Implicit Grant Type (Deprecated)
        - 암시적 부여 타입, 공개 클라이언트 어플리케이션 (SPA 기반 자바스크립트 앱, 모바일 앱), 보안에 취약
    3. Resource Owner Password Credentials Grant Type (Deprecated)
        - 리소스 사용자 비밀번호 자격증명 부여 타입, 서버 어플리케이션, 보안에 취약
        - 서버 어플리케이션에 Authorization Server의 ID와 Password가 노출되기 때문에 보안에 취약하다고 하는 듯
    4. Client Credentials Grant Type
        - 클라이언트 자격 증명 권한 부여 타입, UI or 화면이 없는 서버 어플리케이션
    5. Refresh Token Grant Type
        - 새로고침 토큰 부여 타입, Authorization Code, Resource Owner Password Type 에서 지원
    6. PKCE-enhanced Authorization Code Grant Type
        - PKCE 권한 코드 부여 타입,서버 사이드 어플리케이션, 공개 클라이언트 어플리케이션

### 권한 부여 흐름 선택 기준

![Untitled](../images/OAuth%202.0%20%EC%8B%AC%ED%99%94_1.png)

### 매개 변수 용어

1. client_id
    - 인가서버에 등록된 클라이언트에 대해 생성된 고유 키
2. client_secret
    - 인가서버에 등록된 특정 클라이언트의 client id 에 대해 생성된 비밀 값
3. response_type
    - 애플리케이션이 권한 부여 코드 흐름을 시작하고 있음을 인증 서버에 알려준다
    - code, token, id_token 이 있으며 token, id token 은 implicit 권한부여유형에서 지원해야 한다.
    - 서버가 쿼리 문자열에 인증 코드(code), 토큰(token, id_token) 등을 반환
4. grant_type
    - 권한 부여 타입 지정 - authorization code, password, client credentials, refresh_token
5. redirect_uri
    - 사용자가 응용 프로그램을 성공적으로 승인하면 권한 부여 서버가 사용자를 다시 응용 프로그램으로 리디렉션한다
    - redirect_uri 가 초기 권한 부여 요청에 포함된 경우 서비스는 토큰 요청에서도 이를 요구해야 한다.
    - 토큰 요청의 redirect_uri 는 인증 코드를 생성할 때 사용된 redirect_uri 와 정확히 일치해야 한다. 그렇지 않으면 서비스는 요청을 거부해야 한다.
6. scope
    - 어플리케이션이 사용자 데이터에 접근하는 것을 제한하기 위해 사용된다 - email profile read write
    - 사용자에 의해 특정 스코프로 제한된 권한 인가권을 발행함으로써 데이터 접근을 제한한다
7. state
    - 응용 프로그램은 임의의 문자열을 생성하고 요청에 포함하고 사용자가 앱을 승인한 후 서버로부터 동일한 값이 반환되는지 확인해야 한다.
    - 이것은 CSRF 공격을 방지하는 데 사용된다.

### Authorization Code Grant Type - 권한 부여 코드 승인 방식

1. 흐름 및 특징
    1. 사용자가 애플리케이션을 승인하면 인가 서버는 Redirect URI로 임시 코드를 담아서 애플리케이션으로 다시 리다이렉션한다.
    2. 애플리케이션은 해당 임시 코드를 인가 서버로 전달하고 액세스 토큰으로 교환한다.
    3. 애플리케이션이 액세스 토큰을 요청할 때 해당 요청을 클라이언트 암호로 인증할 수 있으므로 공격자가 인증 코드를 가로채서 스스로 사용할 위험이 줄어든다.
    4. 액세스 토큰이 사용자 또는 브라우저에 표시되지 않고 애플리케이션에 다시 전달하는 가장 안전한 방법이므로 토큰이 다른 사람에게 누출될 위험이 줄어든다.
2. 권한 부여 코드 요청 시 매개변수
    - response_type=code (필수)
    - client_id (필수)
    - redirect_uri (선택)
    - scope (선택)
    - state (선택)
3. 액세스 토큰 교환 요청 시 매개변수
    - grant_type=authorization_code (필수)
    - code (필수)
    - redirect_uri (필수 - `리다이렉션 URI가 초기 승인 요청에 포함된 경우`)
    - client_id (필수)
    - client_secret (필수)

### Implicit Grant Type - 암묵적 승인 방식

1. 흐름 및 특징
    1. 클라이언트에서 Javascript 및 HTML 소스 코드를 다운로드한 후 브라우저는 서비스에 직접 API 요청을 한다.
    2. 코드 교환 단계를 건너뛰고 대신 액세스 토큰이 쿼리 문자열 조각으로 클라이언트에 즉시 반환된다.
    3. 이 유형은 back channel이 없으므로 refresh token을 사용하지 못한다.
    4. 토큰 만료 시 어플리케이션이 새로운 access token을 얻으려면 다시 OAuth 승인 과정을 거쳐야 한다.
2. 권한 부여 승인 요청 시 매개변수
    - response_type=token (필수), id_token
    - client_id (필수)
    - redirect_uri (필수)
    - scope (선택)
    - state (선택)

### Resource Owner Password Credentials Grant Type - 패스워드 자격증명 승인 방식

1. 흐름 및 특징
    - 애플리케이션이 사용자 이름과 암호를 액세스 토큰으로 교환할 떄 사용된다.
    - 타사 어플리케이션이 이 권한을 사용하도록 허용해서는 안되고 신뢰하는 자사 어플리케이션에서만 사용해야 한다.
2. 권한 부여 승인 요청 시 매개변수
    - grant_type=password (필수)
    - username (필수)
    - password (필수)
    - client_id (필수)
    - client_secret (필수)
    - scope (선택)

### Client Credentials Grant Type - 클라이언트 자격증명 승인 방식

1. 흐름 및 특징
    - 애플리케이션이 리소스 소유자인 동시에 클라이언트의 역할을 한다.
    - 리소스 소유자에게 권한 위임 받아 리소스에 접근하는 것이 아니라 자기 자신이 애플리케이션을 사용할 목적으로 사용하는 것
    - 서버 대 서버 간의 토오신에서 사용할 수 있으며 IoT와 같은 장비 어플리케이션과의 통신을 위한 인증으로도 사용할 수 있다.
    - Client ID와 Client Secret을 통해 액세스 토큰을 바로 발급 받을 수 있기 때문에 Refresh Token을 제공하지 않는다.
    - Client 정보를 기반으로 하기 때문에 사용자 정보를 제공하지 않는다.
2. 권한 부여 승인 요청 시 매개변수
    - grant_type=client_credentials (필수)
    - client_id (필수)
    - client_secret (필수)
    - scope (선택)

### Refresh Token Grant Type - 리프레시 토큰 승인 방식

1. 흐름 및 특징
    - 액세스 토큰이 발급될 때 함께 제공되는 토큰으로서 액세스 토큰이 만료되더라도 함께 발급받았던 리프레시 토큰이 유효하다면 인증 과정을 처음부터 반복하지 않아도 액세스 토큰을 재발급 받을 수 있다.
    - 한 번 사용된 리프레시 토큰은 폐기되거나 재사용할 수 있다.
2. 권한 부여 승인 요청 시 매개변수
    - grant_type=refresh_token (필수)
    - client_id (필수)
    - client_secret (필수)

## 6. Open ID Connect

- OpenId Connect 1.0은 OAuth 2.0 프로토콜 위에 구축된 ID 계층으로 OAuth 2.0을 확장하여 인증 방식을 표준화한 OAuth 2.0 기반의 인증 프로토콜이다.
- Scope 지정 시 `openid`를 포함하면 OpenID Connect 사용이 가능하며 인증에 대한 정보는 ID 토큰이라고 하는 JWT로 반환된다.
- OpenID Connect는 클라이언트가 사용자 ID를 확인할 수 있게 하는 보안 토큰인 ID Token을 제공한다.

### OpenID Connect Discovery 1.0 Provider Metadata

- OpenID Connect를 사용하기 위해 필요한 모든 엔드 포인트 및 공개 키 위치 정보를 포함하여 OpenID 공급자의 구성에 대한 클레임 집합을 나타낸다.
- 검색 문서 경로 `/.well-known/openid-configuration`

### ID Token

- ID 토큰은 사용자가 인증 되었음을 증명하는 결과물로서 OIDC 요청 시 access token과 함께 클라이언트에게 전달되는 토큰이다.
- ID 토큰은 JWT로 표현되며 헤더, 페이로드 및 서명으로 구성된다.
- ID 토큰은 개인 키로 발급자가 서명하는 것으로서 토큰의 출처를 보장하고 변조되지 않았음을 보장한다.
- 어플리케이션은 공개키로 ID 토큰을 검증 및 유효성을 검사하고 만료여부 등 토큰의 클레임을 확인한다.
- 클라이언트는 클레임 정보에 포함되어 있는 사용자명, 이메일을 활용하여 인증 관리를 할 수 있다.

### ID Token vs Access Token

- ID Token은 API 요청에 사용해서는 안되며 사용자의 신원 확인을 위해 사용되어야 한다.
- Access Token은 인증을 위해 사용해서는 안되며 리소스에 접근하기 위해 사용되어야 한다.

## OIDC 로그인 요청

- OIDC 상호 작용 행위자
    - OpenID Provider
        - 줄여서 OP라고 하며 OpenID 제공자로서 최종 사용자를 인증하고 인증 결과와 사용자에 대한 정보를 신뢰 당사자에게 제공할 수 있는 OAuth 2.0 서버를 의미한다.
    - Relying Party
        - 줄여서 RP라고 하며 신뢰 당사자로서 인증 요청을 처리하기 위해 OP에 의존하는 OAuth 2.0 애플리케이션을 의미한다.
- 흐름
    1. RP는 OP에 권한 부여 요청을 보낸다.
    2. OP는 최종 사용자를 인증하고 권한을 얻는다.
    3. OP는 ID 토큰과 액세스 토큰으로 응답한다.
    4. RP는 Access Token을 사용하여 UserInfo 엔드포인트에 요청을 보낼 수 있다.
    5. UserInfo 엔드포인트는 최종 사용자에 대한 클레임을 반환한다.
- 매개변수 요청 및 응답
    - 요청 시 scope에 `openid`를  포함해야 한다.
    - response_type 매개 변수는 id_token으로 해야한다.
    - 요청은 nonce 매개 변수를 포함해야 한다.