FROM bitnamilegacy/wildfly:21.0.1


COPY ./build/libs/ROOT.war /opt/bitnami/wildfly/standalone/deployments/ROOT.war
COPY postgres-ds.xml /opt/bitnami/wildfly/standalone/deployments/
COPY driver/postgresql-42.7.7.jar /opt/bitnami/wildfly/standalone/deployments/

EXPOSE 8080 9990

RUN /opt/bitnami/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
