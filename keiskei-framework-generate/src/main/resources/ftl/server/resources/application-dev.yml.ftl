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
  data:
    mongodb:
      database: ${MONGO_DATABASE:test_demo}
      port: ${MONGO_PORT:27017}
      host: ${MONGO_HOST:127.0.0.1}
      field-naming-strategy: org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy
  datasource:
    name: test_demo
  redis:
    timeout: 6000
    password: ${REDIS_PASSWORD}
    database: ${REDIS_DB:0}
    port: ${REDIS_PORT:6379}
    host: ${REDIS_HOST:localhost}
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


