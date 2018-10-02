#####################
# build environment #
#####################

FROM maven:3.5.4-jdk-8 AS maven-builder

COPY . /app
WORKDIR /app
RUN mvn clean install


#######################
# runtime environment #
#######################
FROM tomcat:8.5.32-jre8

ENV PATH=$CATALINA_HOME/bin:$PATH

# Build info
RUN . /etc/os-release; echo "$PRETTY_NAME (`uname -rsv`)" >> /root/.built && \
echo "- with `java -version 2>&1 | awk 'NR == 2'`" >> /root/.built && \
echo "- with Tomcat $TOMCAT_VERSION"  >> /root/.built && \
echo "cat ~/.built" >> /root/.bashrc

COPY tomcat/tomcat-users.xml ${CATALINA_HOME}/conf/tomcat-users.xml
COPY tomcat/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY tomcat/context.xml /usr/local/tomcat/webapps/host-manager/META-INF/context.xml

WORKDIR ${CATALINA_HOME}
EXPOSE 8080
ENV SOLR_URL="http://localhost:8080/solr/search"

COPY --from=maven-builder /app/target/*.war ${CATALINA_HOME}/webapps/
