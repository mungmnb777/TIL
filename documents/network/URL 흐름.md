# 1. HTTP 요청과 응답 흐름

크롬 브라우저에서 `[https://www.naver.com](https://www.naver.com이라는)`이라는 URL을 입력했을 때 어떤 일이 벌어지는지 알아보자.

## 1.1. 브라우저가 URL을 파싱한다.

`[https://www.naver.com](https://www.naver.com의)`의 경우

- 프로토콜 : https
- 도메인 : www.naver.com
- 포트 : 443(https 프로토콜의 경우 443번 포트는 URL에서 생략 가능하다.)

즉 URL은 어떤 웹 서버에 무엇을 요청할 것인가에 대한 정보가 담겨있다.

## 1.2. 도메인을 IP 주소로 변환한다.

도메인 네임은 컴퓨터가 해석할 수 없다. 그래서 IP 주소로 변환해주어야 하는데, 이러한 역할을 해주는 것이 바로 DNS(Domain Name Server)이다.

이 때 요청 과정은 다음과 같다.

1. Local DNS에 해당 URL 주소의 IP 주소를 요청한다.
2. 만약 있다면 이를 응답하고, 없다면 root DNS에 요청한다.
3. root DNS에도 없다면 하위 DNS에 요청해서 있다면 응답한다.

## 1.3. 서버와 TCP 소켓 연결

IP 주소에 해당하는 서버와 논리적 연결을 진행한다. 이 때 3-way handshaking을 통해 이루어진다.

## 1.4. TLS

HTTPS 통신이기 때문에 데이터의 암호화 과정이 포함된다. 이 때 TLS handshaking을 통해 이루어진다.