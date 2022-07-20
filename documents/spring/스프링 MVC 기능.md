# Spring MVC - 기능

# 로깅

로깅은 보통 `@Slf4j`라는 롬복 애노테이션을 사용하여 호출한다.

### 로그 레벨

Trace > Debug > Info > Warn > Error

개발 서버는 보통 Debug를 사용하고 운영 서버는 Info를 사용한다.

### 로그 사용법

```java
log.debug("username = {}", username); // (O)

log.debug("username = " + username); // (X)
```

로그 레벨이 info일 경우 위의 방식은 아무것도 일어나지 않지만 아래 방식은 더하기 연산이 일어나기 때문에 의미 없이 리소스를 사용하게 된다.

### 참고 - 매핑 정보

<aside>
💡 @Controller 애노테이션을 사용할 때 반환값이 String이면 view를 찾고 렌더링한다. 대신에 @RestController를 사용하면 HTTP 메세지 바디 값에 바로 입력된다.

</aside>

# 요청 매핑(@RequestMapping)

```java
@RequestMapping("/hello-basic")
```

URL에서 /hello-basic으로 호출이 오면 이 메소드가 실행하도록 매핑한다.

### HTTP Method

```java
@RequestMapping(value = "/hello-basic", method = RequestMethod.GET)
```

이 경우에는 GET 방식으로만 요청을 받을 수 있다. 다른 메소드로 요청을 하면 스프링 MVC는 405 Method Not Allowed 상태 코드를 반환한다.

위의 명령어는 `@GetMapping("/hello-basic")`으로 줄일 수 있다.

### 경로 변수

```java
@RestController
@GetMapping("/hello-basic/{username}")
public String helloBasic(@Pathvariable String username){
	log.info("username = {}", username);
	return "ok"
}
```

URL Path에서 변수를 받아서 @PathVariable을 이용하여 코드에 사용할 수 있다. 

# HTTP 요청 파라미터

클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 3가지 방법을 이용한다.

- GET - 쿼리 파라미터
    - `/path?username=lee&age=26`
    - 메세지 바디 없이 URL의 쿼리 파라미터에 데이터를 넣어 전달
- POST - HTML Form
    - content-type : application/x-www-form-urlencoded
    - 메세지 바디에 쿼리 파라미터 형식으로 전달 `username=lee&age=20`
- HTTP Message body에 데이터를 직접 담아서 요청
    - HTTP API에서 주로 사용. JSON, XML, TEXT
    - 데이터 형식은 주로 JSON 사용
    - POST, PUT, PATCH, DELETE

### 요청 파라미터 조회 - @RequestParam

스프링이 제공하는 `@RequestParam` 애노테이션을 이용하면 쉽게 파라미터 이름으로 바인딩할 수 있다.

```java
@RequestMapping("/request-param")
public String requestParam(
	@RequestParam String username,
	@RequestParam int ag
){

	log.info("username = {}, age = {}", username, age);
	return "ok";
}
```

클라이언트가 `[http://localhost:8080/request-param?username=lee&age=26](http://localhost:8080/request-param?username=lee&age=26)` 이라는 URL으로 서버에 요청을 보내면 `@RequestParam`에 의해 lee와 26이라는 value가 각각 username, age에 바인딩된다.

### 요청 파라미터 조회 - @ModelAttribute

실제 개발을 하면 요청 파라미터를 받아 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다.

다음과 같은 클래스가 있다.

```java
@Data
public class HelloData {
	private String username;
	private int age;
}
```

`@Data`의 경우 롬복의 `@Getter, @Setter, @ToString` 등 애노테이션들을 자동으로 적용해준다.

이를 이용해 HelloData 객체에 요청 파라미터를 받아볼 수 있다.

```java
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(@ModelAttribute HelloData helloData){
	
	log.info("username = {}, age = {}", helloData.username, helloData.age);
	return "ok";
}
```

스프링 MVC는 `@ModelAttribute`가 있으면 다음과 같이 파라미터를 바인딩한다

1. 우선 객체를 생성한다. 여기서는 HelloData 객체를 생성한다.
2. 요청 받은 파라미터의 이름으로 객체의 프로퍼티를 찾는다.
3. 일치하는 프로퍼티를 찾으면 setter를 이용해 파라미터의 값을 바인딩한다.

### HTTP 응답 - 정적 리소스, 뷰 템플릿

- 정적 리소스
    - ex) 웹 브라우저에 html, css, js를 제공할 때는 정적 리소스 사용
- 뷰 템플릿
    - ex) 동적인 HTML 제공할 떄는 뷰 템플릿 사용
- HTTP 메세지
    - HTTP API를 사용할 때에는 json 데이터를 담은 http 메세지 바디를 제공

### HTTP 응답 - HTTP API, 메세지 바디에 직접 입력

- `@ResponseBody` 를 사용한 방법

```java
@ResponseBody
@GetMapping("/response-body")
public String responseBody() {
	return "ok";
}
```

보통 위와 같은 방식을 사용함. JSON으로 응답이 필요한 경우 아래와 같이 하면 됨

```java
@ResponseStatus(HttpStatus.OK) // 200 상태 코드 응답
@ResponseBody
@GetMapping("/response-json")
public HelloData responseJson(){
	HelloData helloData = new HelloData();
	helloData.setUsername = "userA";
	helloData.setAge = 20;

	return helloData;
}
```

### HTTP 메세지 컨버터

`@ResponseBody` 를 사용하면 viewResolver 대신에 HttpMessageConverter가 동작함

*스프링 MVC는 아래의 경우에 HTTP 메세지 컨버터를 적용한다.*

- HTTP 요청 : `@RequestBody` , `HttpEntity(RequestEntity)`
- HTTP 응답 : `@ResponseBody` , `HttpEntity(ResponseEntity)`

### 요청 매핑 핸들러 어댑터 구조

- Spring MVC 구조
    1. 우선 핸들러를 조회한다.
    2. 핸들러 어댑터 목록에서 매핑되는 어댑터를 조회한다.
    3. 핸들러 어댑터가 핸들러를 호출하고 비즈니스 로직을 실행한 후 값을 반환받는다.
    4. 반환받은 값을 이용해 뷰 리졸버를 실행시킨다.
    5. 뷰를 호출한다.