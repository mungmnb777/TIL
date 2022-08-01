# Let’s Encrypt 인증서 발급

우리 프로젝트에서는 인증서 발급 방식 중 `standalone` 방식을 이용하여 인증서를 발급받았습니다.

이 방식은 80번 포트로 가상 standalone 웹 서버를 띄워 인증서를 발급받는 방식으로 동시에 여러 도메인에 대해 인증서를 발급받을 수 있다는 장점이 있지만 인증서 발급 전에 nginx 서버를 중단해야 한다는 단점이 있습니다.

```
sudo apt update

// letsencrypt 패키지 설치
sudo apt-get install letsencrypt -y

// 실행중인 nginx 종료 
service nginx stop

// SSL 인증
certbot certonly --standalone -d uknowme.mooo.com
```

certbot 명령을 실행시켰을 때 만약 인증서가 없다면 인증서를 발급받는 과정을 거칩니다.

```
Saving debug log to /var/log/letsencrypt/letsencrypt.log
Plugins selected: Authenticator standalone, Installer None
Enter email address (used for urgent renewal and security notices) (Enter 'c' to cancel)
:
```

다음은 서비스 약관에 동의하는지 묻습니다. 동의 해줍니다.

```
Please read the Terms of Service at
https://letsencrypt.org/documents/LE-SA-v1.2-November-15-2017.pdf. You must agree in order to register with the ACME server at  https://acme-v02.api.letsencrypt.org/directory

(A)gree/(C)ancel:
```

다음은 이메일 주소를 공유할 것인지를 묻습니다. 공유한다면 Y, 아니라면 N을 입력하면 됩니다.

```
Would you be willing to share your email address with the Electronic Frontier  Foundation, a founding partner of the Let's Encrypt project and the non-profit  organization that develops Certbot? We'd like to send you email about our work  encrypting the web, EFF news, campaigns, and ways to support digital freedom.
 
(Y)es/(N)o:
```

인증 완료 후 제대로 발급이 되었는지 확인해줍니다.

```
certbot certificates
```