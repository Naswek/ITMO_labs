#!/bin/bash
set -euo pipefail

: "${DB_HOST:?DB_HOST is required}"
: "${DB_NAME:?DB_NAME is required}"
: "${DB_USER:?DB_USER is required}"
: "${DB_PASSWORD:?DB_PASSWORD is required}"

asadmin start-domain
if ! asadmin list-jdbc-connection-pools | grep -Fxq LabworksPool; then
    asadmin create-jdbc-connection-pool \
        --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
        --restype javax.sql.DataSource \
        --property "serverName=${DB_HOST}:portNumber=5432:databaseName=${DB_NAME}:user=${DB_USER}:password=${DB_PASSWORD}" \
        LabworksPool
fi
if ! asadmin list-jdbc-resources | grep -Fxq jdbc/LabworksDS; then
    asadmin create-jdbc-resource --connectionpoolid LabworksPool jdbc/LabworksDS
fi
asadmin ping-connection-pool LabworksPool
asadmin stop-domain

if [ -f "${PATH_GF_HOME}/labworks.war" ]; then
    mv "${PATH_GF_HOME}/labworks.war" /deploy/labworks.war
fi
