FROM openjdk:8-jre
COPY keiskei-start-demo/target/keiskei-start-demo-3.0.0-Release.jar /home/keiskei-start-demo.jar
ENTRYPOINT ["java", "-Xmx4g", "-Xms4g", "-Xmn256m", "-Xss256k","-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=/tmp/heapDumps","-jar","/home/keiskei-start-demo.jar"]
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone