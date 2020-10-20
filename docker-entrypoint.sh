#!/bin/bash

JAVA_OPTS="-Xms128m -Xmx128m"

# mysql, nacos配置
CONF="--mysql.host=10.1.30.110:3306 --register.url=10.1.30.110:8848"

java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-gateway/sop-gateway.jar $CONF --logging.file.path=/sop/sop-gateway/log &
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-admin/sop-admin.jar $CONF --logging.file.path=/sop/sop-admin/log &
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-website/sop-website.jar $CONF --logging.file.path=/sop/sop-website/log &
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-auth/sop-auth.jar $CONF --logging.file.path=/sop/sop-auth/log &
# 最后一条没有&
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-story/sop-story.jar $CONF --logging.file.path=/sop/sop-story/log