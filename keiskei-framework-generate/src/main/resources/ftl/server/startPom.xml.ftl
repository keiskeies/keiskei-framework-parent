<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>${name}</artifactId>
        <groupId>top.keiskeiframework</groupId>
        <version>1.0.0-Release</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>${module.name}</artifactId>
    <name>${module.name}</name>
    <version>1.0.0-Release</version>
    <dependencies>
<#list modules as module>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>${module.name}</artifactId>
            <version>1.0.0-Release</version>
        </dependency>
</#list>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>