# JOB SCRAPPER

### INSTALL
To successfully run application you need to up locally postgres container using Docker 
```
docker run --name postgres-container -e POSTGRES_PASSWORD=1234 -d -p 5433:5432 postgres
```
If you used custom password or username, you need to configure next options in ```application.properties```:

```spring.datasource.username```
```spring.datasource.password```

### CONFIGURE
By default, application can parse only first page in job list, if you want to parse all number of job advertisement you 
need to switch ```LOAD_FULL_PAGE``` environment variable to ```true``` in ```application.properties```. But this behaviour
cause totally increase page scrapping time 
