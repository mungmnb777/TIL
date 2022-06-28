# 엔티티 매핑

# @Entity

- @Entity가 붙은 클래스는 JPA과 관리, 엔티티라 한다.
- JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
- 주의사항
    - 기본 생성자 필수(리플렉션을 사용하기 때문)
    - final 클래스, enum, interface, inner 클래스 사용 안됨
    - 저장할 필드에 final 사용 안됨

# @Table

- @Table은 엔티티와 매핑할 테이블 지정

| 속성 | 기능 | 기본값 |
| --- | --- | --- |
| name | 매핑할 테이블 이름 | 엔티티 이름 사용 |
| catalog | 데이터베이스 catalog 매핑 |  |
| schema | 데이터베이스 schema 매핑 |  |
| uniqueConstraints | DDL 생성 시에 유니크 제약 조건 생성 |  |

# 데이터베이스 스키마 자동 생성

- DDL을 애플리케이션 실행 시점에 자동 생성
- 테이블 중싱 → 객체 중심
- 데이터베이스 방언을 사용해서 데이터베이스에 맞는 적절한 DDL 생성
- 이렇게 생성한 DDL은 개발 장비에서만 사용

## hibernate.hbm2ddl.auto

JPA가 DDL 생성에 대해 취할 전략에 대한 옵션

# 매핑 애노테이션

| 애노테이션 | 설명 |
| --- | --- |
| @Column | 컬럼 매핑 |
| @Temporal | 날짜 타입 매핑 |
| @Enumerated | enum 타입 매핑 |
| @Lob | BLOB, CLOB 매핑 |
| @Transient | 특정 필드를 매핑에서 제외 |