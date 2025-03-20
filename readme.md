### Multi-Database Transaction Management Project
This project is designed to learn how to manage data across multiple databases within a single transaction using Spring
Boot.

Spring Boot를 사용하여 멀티 데이터베이스 환경에서 하나의 트랜잭션으로 데이터를 관리하는 방법을 학습하기 위한 프로젝트입니다.

---

### 주요 목표 / Key Goals

- **멀티 데이터베이스 설정**  
  Spring Boot 환경에서 복수의 데이터베이스 연결 설정을 구성합니다.  
  **Setting up multiple database connections** in a Spring Boot application.


- **트랜잭션 관리**  
  여러 데이터베이스에서 단일 트랜잭션으로 데이터 일관성을 유지하는 방법을 학습합니다.  
  **Transaction management** across multiple databases to maintain data consistency within a single transaction.


- **Mybatis 및 플랫폼 통합**  
  Mybatis 및 Spring 데이터 관련 기능을 활용하여 데이터베이스와 상호작용합니다.  
  **Integration with Mybatis and platforms** to interact with databases using Spring's data-related capabilities.

---

### 필요 조건 / Prerequisites

- Java 17 or higher
- Maven
- Spring Boot 3.x.x version
  - HikariCP
  - log4jdbc
  - Mybatis 3.x.x

---

### 예제 설정 및 코드 / Example Setup and Code

#### db-config.properties 설정 / db-config.properties Configuration:

```properties
database1.url=jdbc:log4jdbc:mysql://localhost:3306/mydb
database1.username=my-user
database1.password=my-password

database2.url=jdbc:log4jdbc:sqlserver://localhost:1433
database2.username=ms-user
database2.password=ms-password
```

#### application.yml 설정 / application.yml Configuration:

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

## mybatis config
mybatis:
  type-aliases-package: com.koda.multitranx
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    multiple-result-sets-enabled: true
    call-setters-on-nulls: true
```

### 테스트용 DB TABLE 생성 / Create Database Table for test
```sql
-- mysql
create table transaction_test(
    id varchar(36) not null primary key,
    reg_date datetime,
    reg_database_pool varchar(20)
);
```

---

### 테스트 방법 / How to Test

1. **DB 접속 정보와 테이블 생성 / Provide DB connection info and create tables**  
   먼저, 제공된 `db-config.properties` 파일을 작성하고, 위에 작성되어 있는 SQL 스크립트를 사용해 데이터베이스에 테이블을 생성하세요.  
   First, create the `db-config.properties` file with the provided template, and use the SQL script above to create
   tables in the databases.


2. **테스트 클래스 실행 / Execute the test class**  
   `MultiTranxApplicationTests` 테스트 클래스를 실행하여 트랜잭션이 제대로 동작하는지 확인해보세요.  
   Execute the `MultiTranxApplicationTests` test class to verify if the transactions work as expected.

이 단계를 따라하면 멀티 데이터베이스 트랜잭션 환경을 테스트해볼 수 있습니다.  
By following these steps, you can test the multi-database transaction environment.

---

### 추가 사항

- **SQL 문 수정 / Modify the SQL statements**  
    필요하다면, Mapper 안의 SELECT나 INSERT 문을 데이터베이스 환경에 맞게 수정하세요.  
    Modify the SQL statements in the Mapper if necessary to match your database environment.

   예를 들어:  
   For example:
    - `Db1Mapper.xml`: MySQL에서 사용하는 쿼리 / Queries designed for MySQL.
    - `Db2Mapper.xml`: SQL Server에서 사용하는 쿼리 / Queries designed for SQL Server.


- **버전 호환성 확인 / Check Version Compatibility**  
  Spring Boot, MyBatis 등 주요 의존성의 버전을 변경할 때는 호환성을 반드시 확인하세요.  
  Be sure to check compatibility when updating the versions of key dependencies such as Spring Boot and MyBatis.

    - Refer to the official documentation or release notes for compatibility information between Spring Boot and
      MyBatis.
    - Pay special attention to potential version conflicts with additional libraries such as HikariCP and Log4jdbc.
    - Use dependency management tools like Gradle or Maven to check the dependency tree and for duplicate versions.

---

이 프로젝트를 통해 멀티 데이터베이스 트랜잭션 관리를 성공적으로 구현할 수 있는 방법을 학습할 수 있습니다!  
Through this project, you will learn how to implement multi-database transaction management successfully!