#!/bin/bash

JAVA_OPTS="-Xms128m -Xmx128m"

CONF="--mysql.host=10.1.30.120:3306 --register.url=10.1.30.110:8848"

nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-gateway/sop-gateway.jar $CONF --logging.file.path=/sop/sop-gateway/log > /dev/null 2>&1 &
nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-admin/sop-admin.jar $CONF --logging.file.path=/sop/sop-admin/log > /dev/null 2>&1 &
nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-website/sop-website.jar $CONF --logging.file.path=/sop/sop-website/log > /dev/null 2>&1 &
nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-auth/sop-auth.jar $CONF --logging.file.path=/sop/sop-auth/log > /dev/null 2>&1 &
nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sop/sop-story/sop-story.jar $CONF --logging.file.path=/sop/sop-story/log > /dev/null 2>&1 &

