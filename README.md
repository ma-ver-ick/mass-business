


    docker run -d --name=mass-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword postgres
    docker run -d --name=mass-wildfly --link mass-postgres:db -p 8888:8080 -p 9990:9990 jboss/wildfly-admin /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

    docker build --tag=jboss/wildfly-admin .

    docker stop mass-wildfly mass-postgres
    docker rm mass-wildfly mass-postgres


