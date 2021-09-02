<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>${name}</artifactId>
        <groupId>top.keiskeiframework</groupId>
        <version>1.1.0-jpa-Release</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>${name}-start</artifactId>
    <name>${name}-start</name>
    <version>1.1.0-jpa-Release</version>
    <dependencies>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-core</artifactId>
        </dependency>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-system</artifactId>
        </dependency>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>${item.fileJar.value}</artifactId>
        </dependency>
<#if item.sqlLog>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-system-log</artifactId>
        </dependency>
</#if>
<#if item.workflow>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-system-workflow</artifactId>
        </dependency>
</#if>
<#list modules as module>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>${module.name}</artifactId>
            <version>1.1.0-jpa-Release</version>
        </dependency>
</#list>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <profiles>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <environment>dev</environment>
            </properties>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-devtools</artifactId>
                    <optional>true</optional>
                    <scope>true</scope>
                </dependency>
                <!--监控sql日志-->
                <dependency>
                    <groupId>org.bgee.log4jdbc-log4j2</groupId>
                    <artifactId>log4jdbc-log4j2-jdbc4.1</artifactId>
                    <version>1.16</version>
                </dependency>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-web</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-actuator</artifactId>
                </dependency>
                <dependency>
                    <groupId>io.micrometer</groupId>
                    <artifactId>micrometer-registry-prometheus</artifactId>
                </dependency>
                <dependency>
                    <groupId>com.github.xiaoymin</groupId>
                    <artifactId>knife4j-micro-spring-boot-starter</artifactId>
                    <version>2.0.2</version>
                </dependency>
            </dependencies>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <environment>prod</environment>
            </properties>
        </profile>
    </profiles>
</project>