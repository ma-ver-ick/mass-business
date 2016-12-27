# GlassFish on Docker
FROM glassfish/openjdk

# Maintainer
MAINTAINER Bruno Borges <bruno.borges@oracle.com>

# Set environment variables and default password for user 'admin'
ENV GLASSFISH_PKG=glassfish-4.1.1.zip \
    GLASSFISH_URL=http://download.oracle.com/glassfish/4.1.1/release/glassfish-4.1.1.zip \
    POSTGRES_URL=https://jdbc.postgresql.org/download/postgresql-9.4.1209.jar \
    POSTGRES_FILE=/glassfish4/glassfish/domains/domain1/lib/postgresql.jar \
    GLASSFISH_HOME=/glassfish4 \
    PATH=$PATH:/glassfish4/bin \
    PASSWORD=glassfish


# Install packages, download and extract GlassFish
# Setup password file
# Enable DAS
RUN apk add --update wget unzip && \
    wget --no-check-certificate $GLASSFISH_URL && \
    unzip -o $GLASSFISH_PKG && \
    rm -f $GLASSFISH_PKG && \
    echo "--- Setup the password file ---" && \
    echo "AS_ADMIN_PASSWORD=" > /tmp/glassfishpwd && \
    echo "AS_ADMIN_NEWPASSWORD=${PASSWORD}" >> /tmp/glassfishpwd  && \
    echo "--- Enable DAS, change admin password, and secure admin access ---" && \
    asadmin --user=admin --passwordfile=/tmp/glassfishpwd change-admin-password --domain_name domain1 && \
    asadmin start-domain && \
    echo "AS_ADMIN_PASSWORD=${PASSWORD}" > /tmp/glassfishpwd && \
    asadmin --user=admin --passwordfile=/tmp/glassfishpwd enable-secure-admin && \
    echo "--- Configuring myDatasource connection db pools ---" && \
    asadmin --user=admin --passwordfile=/tmp/glassfishpwd create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGSimpleDataSource --restype javax.sql.DataSource --property user=postgres:password=mass:databaseName=postgres:serverName=db myDatasource && \
    asadmin --user=admin --passwordfile=/tmp/glassfishpwd create-jdbc-resource --connectionpoolid myDatasource jdbc/myDatasource && \
    asadmin --user=admin --passwordfile=/tmp/glassfishpwd set domain.resources.jdbc-connection-pool.myDatasource.property.JDBC30DataSource=true && \
    echo "--- Finishing installation, shutting down server --" && \
    rm /tmp/glassfishpwd && \
    asadmin --user=admin stop-domain && \
    wget $POSTGRES_URL -O $POSTGRES_FILE && \
    apk del wget unzip

# Ports being exposed
EXPOSE 4848 8080 8181

# Start asadmin console and the domain
CMD ["asadmin", "start-domain", "-v"]
