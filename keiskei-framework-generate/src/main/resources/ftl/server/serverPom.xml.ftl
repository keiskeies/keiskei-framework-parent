<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>keiskei-framework-parent</artifactId>
        <groupId>top.keiskeiframework</groupId>
        <version>1.0.0-Release</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <modules>
<#list modules as module>
        <module>${module.name}</module>
</#list>
        <module>${name}-start</module>
    </modules>

    <artifactId>${name}</artifactId>
    <name>${name}</name>
    <version>1.0.0-Release</version>
    <dependencies>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-core</artifactId>
        </dependency>
    </dependencies>
</project>