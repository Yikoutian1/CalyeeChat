# 添加 Java 8 镜像来源
FROM openjdk:8u312
LABEL authors="Calyee"
# 添加参数
ARG JAR_FILE

# 添加 Spring Boot 包
ADD /chat-server/${JAR_FILE} calyeechat.jar

EXPOSE 8080
EXPOSE 8090
# 执行启动命令
CMD java -jar calyeechat.jar
