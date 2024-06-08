# 添加 Java 8 镜像来源
FROM openjdk:8
#WORKDIR指令用于指定容器的一个目录， 容器启动时执行的命令会在该目录下执行。
WORKDIR /opt/docker/images/calyeedatabases/

# 添加 Spring Boot 包
ADD chat-server-1.0-SNAPSHOT.jar chat-server-1.0-SNAPSHOT.jar

EXPOSE 8080
EXPOSE 8090
# 执行启动命令
CMD nohup java -Xmx700m -Xms700m -Xmn512m -jar /chat-server-1.0-SNAPSHOT.jar > /dev/null2>&1 &
