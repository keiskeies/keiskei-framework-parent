<#noparse>
server:
  port: ${APP_PORT:10037}
  servlet:
    session:
      timeout: P60D
      cookie:
        http-only: false
logging:
  level:
    top.keiskeiframework: info
  pattern:
    file: "%d [%X{mdcTraceId}] [%thread] %-5level - %msg%n"
    console: "%d [%X{mdcTraceId}] [%thread] %-5level %logger{36} %line - %msg%n"
  file:
    max-size: 100MB
    max-history: 90
    path: /opt/project/log/
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
    sentinel:
      master: ${REDIS_MASTER:mymaster}
      nodes:
        - ${REDIS_HOST_0}:${REDIS_PORT_0}
        - ${REDIS_HOST_1}:${REDIS_PORT_1}
        - ${REDIS_HOST_2}:${REDIS_PORT_2}

  freemarker:
    cache: false
management:
  endpoints:
    web:
      exposure:
        include: 'prometheus'
  metrics:
    tags:
      application: ${spring.application.name}
keiskei:
  cache-expired:
    FILE_MINUTE:
  system:
    cross: true
    maximum-sessions: 1
</#noparse>