FROM java:8
VOLUME /tmp
VOLUME /sop

ADD sop-gateway/target/*.jar sop/sop-gateway/sop-gateway.jar
ADD sop-admin/sop-admin-server/target/*.jar sop/sop-admin/sop-admin.jar
ADD sop-website/target/*.jar sop/sop-website/sop-website.jar
ADD sop-auth/target/*.jar sop/sop-website/sop-auth.jar
ADD sop-example/sop-story/target/*.jar sop/sop-story/sop-story.jar

RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["docker-entrypoint.sh"]
