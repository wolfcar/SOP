FROM java:8
VOLUME /tmp
VOLUME /sop

ADD sop-gateway/target/*.jar sop/sop-gateway/sop-gateway.jar
ADD sop-admin/sop-admin-server/target/*.jar sop/sop-admin/sop-admin.jar
ADD sop-website/target/*.jar sop/sop-website/sop-website.jar
ADD sop-auth/target/*.jar sop/sop-website/sop-auth.jar
ADD sop-example/sop-story/target/*.jar sop/sop-story/sop-story.jar


# JVM设置
ENV JAVA_OPTS="-Xms128m -Xmx128m"

# springboot配置文件
#  --logging.file：日志
#  --mysql.host：mysql地址
#  --register.url：nacos地址
ENV CONF="--logging.file.path=log --mysql.host=10.1.30.120:3306 --register.url=10.1.30.110:8848"

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-gateway/sop-gateway.jar ${CONF}
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-admin/sop-admin.jar ${CONF}
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-website/sop-website.jar ${CONF}
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-auth/sop-auth.jar ${CONF}
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-story/sop-story.jar ${CONF}
