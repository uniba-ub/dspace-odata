#####################
# build environment #
#####################

FROM maven:3.9-eclipse-temurin-17 AS maven-builder

COPY . /app
WORKDIR /app
RUN mvn clean install

RUN ls -lAh /app/target/

#######################
# runtime environment #
#######################
FROM tomcat:10-jdk17

ENV PATH=$CATALINA_HOME/bin:$PATH

# Build info
RUN . /etc/os-release; echo "$PRETTY_NAME (`uname -rsv`)" >> /root/.built && \
echo "- with `java -version 2>&1 | awk 'NR == 2'`" >> /root/.built && \
echo "- with Tomcat $TOMCAT_VERSION"  >> /root/.built && \
echo "cat ~/.built" >> /root/.bashrc

COPY tomcat/tomcat-users.xml ${CATALINA_HOME}/conf/tomcat-users.xml
COPY tomcat/context.xml ${CATALINA_HOME}/webapps/manager/META-INF/context.xml
COPY tomcat/context.xml ${CATALINA_HOME}/webapps/host-manager/META-INF/context.xml

RUN rm -rf ${CATALINA_HOME}/webapps/*

WORKDIR ${CATALINA_HOME}
EXPOSE 8080
ENV SOLR_URL="http://localhost:8080/solr/search"
COPY --from=maven-builder /app/target/ODataService.war ${CATALINA_HOME}/webapps/ROOT.war
