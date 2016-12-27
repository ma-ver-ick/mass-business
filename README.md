


    docker run -d --name=mass-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mass postgres
    docker run -d --name=mass-glassfish --link mass-postgres:db -p 8080:8080 -p 4848:4848 msei/mass-glassfish 

    docker build --tag=msei/mass-glassfish .

    docker stop mass-glassfish mass-postgres
    docker rm mass-glassfish mass-postgres


