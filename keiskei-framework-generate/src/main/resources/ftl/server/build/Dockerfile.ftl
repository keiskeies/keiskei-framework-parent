FROM openjdk:8-jre
COPY ${name}-start/target/${name}-start-1.1.0-jpa-Release.jar /home/${name}-start.jar
<#noparse>
ENTRYPOINT ["java", "-Xmx4g", "-Xms4g", "-Xmn256m", "-Xss256k","-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=/tmp/heapDumps","-jar","/home/${name}-start.jar"]
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
</#noparse>