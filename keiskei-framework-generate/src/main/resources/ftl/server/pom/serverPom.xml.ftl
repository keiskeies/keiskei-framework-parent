<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>keiskei-framework-parent</artifactId>
        <groupId>top.keiskeiframework</groupId>
        <version>2.0.0-mp-Release</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <modules>
<#list modules as module>
        <module>${module.name}</module>
</#list>
        <module>${name}-start</module>
        <module>${name}-doc</module>
    </modules>

    <packaging>pom</packaging>
    <artifactId>${name}</artifactId>
    <name>${name}</name>
    <version>2.0.0-mp-Release</version>
    <dependencies>
        <dependency>
            <groupId>top.keiskeiframework</groupId>
            <artifactId>keiskei-framework-data</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>top.keiskeiframework</groupId>
                <artifactId>${name}-start</artifactId>
                <version>2.0.0-mp-Release</version>
            </dependency>
            <dependency>
                <groupId>top.keiskeiframework</groupId>
                <artifactId>${name}-doc</artifactId>
                <version>2.0.0-mp-Release</version>
            </dependency>
<#list modules as module>
            <dependency>
                <groupId>top.keiskeiframework</groupId>
                <artifactId>${module.name}</artifactId>
                <version>2.0.0-mp-Release</version>
            </dependency>
</#list>
        </dependencies>
    </dependencyManagement>
    <repositories>
        <repository>
            <id>cjm-nexus-central</id>
            <name>cjm-nexus-central</name>
            <url>http://nexus.cjm.jx.cn/repository/maven-central/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
        <repository>
            <id>cjm-nexus</id>
            <name>cjm-nexus</name>
            <url>http://nexus.cjm.jx.cn/repository/cjm-repository/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>
</project>