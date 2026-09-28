FROM quay.io/wildfly/wildfly-ee-10:40.0.0.Final-jdk17
WORKDIR /opt/jboss/wildfly

COPY --chown=jboss:0 app/build/libs/labworks.war app/build/docker/postgresql.jar standalone/deployments/
COPY --chown=jboss:0 docker/datasource.xml standalone/configuration/labworks-datasource.xml
COPY --chown=jboss:0 app/build/docker/eclipselink.jar modules/system/layers/base/org/eclipse/persistence/main/
RUN grep -q '<datasources>' standalone/configuration/standalone.xml && \
    sed -i '/<datasources>/r standalone/configuration/labworks-datasource.xml' standalone/configuration/standalone.xml
RUN sed -i \
    -e '/<resources>/a\        <resource-root path="eclipselink.jar"><filter><exclude path="jakarta/**"/></filter></resource-root>' \
    -e '/<dependencies>/a\        <module name="java.se"/>' \
    modules/system/layers/base/org/eclipse/persistence/main/module.xml

EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
