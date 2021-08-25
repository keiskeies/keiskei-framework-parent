<#noparse>
server:
  port: 10037
  servlet:
    session:
      timeout: P60D
      cookie:
        http-only: false
logging:
  level:
    top.keiskeiframework: debug
    org.springframework: error
  pattern:
    file: "%d [%X{mdcTraceId}] [%thread] %-5level - %msg%n"
    console: "%d [%X{mdcTraceId}] [%thread] %-5level %logger{36} %line - %msg%n"
spring:
  session:
    store-type: redis
  jackson:
    default-property-inclusion: non_null
  messages:
    basename: i18n/messages
  servlet:
    multipart:
      max-file-size: 500MB
      max-request-size: 500MB
  datasource:
    name: news_stand
    driver-class-name: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
    url: jdbc:log4jdbc:mysql://${MYSQL_HOST:127.0.0.1}:${MYSQL_PORT:3306}/${MYSQL_DB:test_demo}?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true
  redis:
    timeout: 6000
    password: ${REDIS_PASSWORD}
    database: ${REDIS_DB:0}
    port: ${REDIS_PORT:6379}
    host: ${REDIS_HOST:localhost}
  jpa:
    database: mysql
    show-sql: false
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    generate-ddl: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        jdbc:
          batch_size: 500
          batch_versioned_data: true
          order_inserts: true
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
      exclude: WEB-INF/**
  freemarker:
    cache: false
keiskei:
  cache-expired:
    FILE_MINUTE:
  system:
    permit-uri: /swagger-ui.html,/images/**,/webjars/**,/v2/**,/configuration/**,/csrf,/doc.html,/webjars/**,/swagger-resources/**,/favicon.ico
    cross: true
    maximum-sessions: 1
</#noparse>


