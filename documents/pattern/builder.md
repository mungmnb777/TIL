# 1. Builder 패턴이란?

빌더 패턴은 여러 디자인 패턴 중 생성 패턴에 해당하는 패턴이다. 빌더 패턴을 사용할 때, 클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자를 호출한다. 이 때 우리는 원하는 매개변수만을 생성자로 넣기 위해 빌더 객체라는 것을 이용한다. 빌더 클래스는 어떤 클래스 내부에서 정적 멤버 클래스로 보통 존재하며 그 멤버 클래스의 생성자로 객체를 얻을 수 있다. 그 다음 빌더 객체가 제공하는 `setter`같은 메서드로 매개변수들을 설정하고, 마지막으로 `build()`를 호출해 객체를 얻는게 일반적인 순서이다.

```java
public class BoardDto {
	// 공지사항 ID
	private int id;
	// 공지사항 제목
	private String title;
	// 공지사항 내용
	private String content;
	// 작성자
	private MemberDto member;
	// 글 작성 날짜
	private LocalDateTime cdate;
	// 글 수정 날짜
	private LocalDateTime udate;

	private BoardDto(int id, String title, String content, MemberDto member, LocalDateTime cdate, LocalDateTime udate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.member = member;
		this.cdate = cdate;
		this.udate = udate;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		// 공지사항 ID
		private int id;
		// 공지사항 제목
		private String title;
		// 공지사항 내용
		private String content;
		// 작성자
		private MemberDto member;
		// 글 작성 날짜
		private LocalDateTime cdate;
		// 글 수정 날짜
		private LocalDateTime udate;

		public Builder id(int id){
			this.id = id;
			return this;
		}

		public Builder title(String title){
			this.title = title;
			return this;
		}

		public Builder content(String content){
			this.content = content;
			return this;
		}

		public Builder member(MemberDto member){
			this.member = member;
			return this;
		}

		public Builder cdate(LocalDateTime cdate) {
			this.cdate = cdate;
			return this;
		}

		public Builder udate(LocalDateTime udate) {
			this.udate = udate;
			return this;
		}

		public BoardDto build() {
			return new BoardDto(id, title, content, member, cdate, udate);
		}
	}
}
```

# 2. Builder 패턴의 장점

## 2.1. 원하는 매개 변수만 입력할 수 있다.

우선 일반적으로 생성자를 이용해서 객체를 생성하는 경우 원치 않는 필드도 항상 지정해주거나, 아니면 생성자를 입맛대로 더 생성해주어야 한다. 하지만 위 패턴을 사용하는 경우 원치 않는 매개변수는 입력하지 않아도 된다.

```java
public static void main(String[] args){
	BoardDto dto = BoardDto.builder()
												 .title("제목")
												 .content("내용")
												 .build();
}
```

이런 식으로 title과 content만을 필요로 한다면, 다른 매개변수는 건들 필요 없이 딱 title과 content만 입력해주면 된다.

## 2.2. 불변성을 가질 수 있다.

2.1장은 `setter`를 이용하면 되지 않느냐는 반박이 나올 수 있다. 물론 가능하지만 생성자와 수정자는 엄연히 차이가 있다. 특히 내가 생각하는 가장 큰 차이는 필드에 final을 붙일 수 있냐 아니냐이다. 필드에 final을 붙일 경우에는 필드에 직접 값을 지정하거나, 아니면 생성자로 지정해주어야 한다. 수정자로는 final이 붙은 필드에 값을 넣을 수 없다. 그렇기 때문에 final은 변경할 수 없는 성질인 불변성을 가질 수 있다.

Builder 패턴의 경우에는 엄연히 생성자를 이용해서 객체의 필드를 지정하기 때문에 필드의 값을 불변성을 가지도록 만들 수 있다. 위의 코드 1번에서 필드에 final을 붙여주고, default값만 정해주면 된다.

## 2.3. 가독성 및 사용성이 높아진다.

빌더 패턴을 이용하게 되면 어떤 필드에 값을 넣고 있는지 헷갈리지 않게 된다. 생성자를 사용하는 경우 우리는 보통 다음과 같이 사용한다.

```java
public static void main(String[] args){
	BoardDto dto = new BoardDto("1", "2", "3", ... );
}
```

이 때, 저 “1”이 어떤 필드에 들어가고 “2”는 어떤 필드에 들어가는지 한 눈에 알기 쉽지 않다. 또한 생성자를 직접 사용할 때도 데이터를 적절한 필드에 넣기 위해서 순서를 정확히 지켜줘야 하기 때문에 `ctrl + p`를 이용해서 제목은 몇 번째 위치인지, 내용은 몇 번째 위치인지 계속 확인하면서 넣어주어야 했다. 하지만 빌더 패턴은 메서드 이름에 필드의 정보가 담겨져 있기 때문에 가독성도 높아지고, 개발자가 사용하기에도 편리하다.