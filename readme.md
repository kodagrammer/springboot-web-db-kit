# 🔄 Multi-Database Transaction Management
Spring Boot 환경에서 멀티 데이터베이스를 하나의 트랜잭션으로 관리하는 예제 프로젝트.

<br/>

## 🎯 주요 내용

- 멀티 데이터베이스 설정 : 복수의 데이터베이스 연결 구성
- 트랜잭션 관리 : 여러 DB에서 단일 트랜잭션으로 데이터 일관성 유지
- MyBatis 통합 : MyBatis를 활용한 데이터베이스 연동

<br/>

## 🛠️ 기술 스택

- Java 17 or higher
- Maven
- Spring Boot 3.x.x version
  - HikariCP
  - log4jdbc
  - Mybatis 3.x.x

<br/>

## ⚙️ 설정 방법

### 1. 데이터베이스 설정

Create `db-config.properties`:

```properties
# Database 1 (MySQL)
database1.url=jdbc:log4jdbc:mysql://localhost:3306/mydb
database1.username=my-user
database1.password=my-password

# Database 2 (SQL Server)
database2.url=jdbc:log4jdbc:sqlserver://localhost:1433
database2.username=ms-user
database2.password=ms-password
```

### 2. 애플리케이션 설정

`application.yml` 파일 설정:

```yaml
spring:
  config:
    import: classpath:db-config.properties
  datasource:
    hikari:
      database1:
        pool-name: hikari-default
        jdbc-url: ${database1.url}
        username: ${database1.username}
        password: ${database1.password}
        driver-class-name: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
        connection-test-query: SELECT 'database1'
        mapper-locations: classpath:mapper/**/*.xml
      database2:
        pool-name: hikari-second
        jdbc-url: ${database2.url}
        username: ${database2.username}
        password: ${database2.password}
        driver-class-name: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
        connection-test-query: SELECT 'database2'
        mapper-locations: classpath:mapper/database2/*.xml

# MyBatis Configuration
mybatis:
  type-aliases-package: com.koda.multitranx
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    multiple-result-sets-enabled: true
    call-setters-on-nulls: true
```

<br/>

## 🚀 실행 방법

1. `db-config.properties` 작성 및 테스트용 테이블 생성
```sql
-- MySQL
CREATE TABLE transaction_test(
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    reg_date DATETIME,
    reg_database_pool VARCHAR(20)
);
```
2. `MultiTranxApplicationTests` 테스트 클래스 실행

<br/>

## 📝 추가 설정

- **SQL 문 수정**  
    필요하다면, Mapper 안의 SELECT나 INSERT 문을 데이터베이스 환경에 맞게 수정

   For Example:
    - `Db1Mapper.xml`: MySQL에서 사용하는 쿼리
    - `Db2Mapper.xml`: SQL Server에서 사용하는 쿼리

- **버전 호환성 확인**  
  Spring Boot, MyBatis 등 주요 의존성의 버전을 변경 시, 호환성을 반드시 확인 필요
    * Spring Boot와 MyBatis 간의 호환성 정보는 공식 문서 또는 릴리스 노트를 참고
    * HikariCP 및 Log4jdbc와 같은 추가 라이브러리의 버전 충돌 가능성에 특히 주의 필요
    * Gradle 또는 Maven과 같은 의존성 관리 도구를 사용하여 의존성 트리와 중복 버전을 확인

```bash
mvn dependency:tree
```
