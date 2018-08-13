### Build WAR File
Build a war file form manual deployment with `./buildService.sh` the result is in the target
folder.


### Build Docker image
Create an image with tomcat and OData webservice with

`docker image build --tag docker-registry.rz.uni-bamberg.de/itfl-service/hydra/odata:latest .`

or uncomment the build section in `docker-compose.yml` and use `docker-compose build`
instead.


### Start Tomcat
Start a tomcat server with port 8080 mapped to the
[host](http://localhost:8080)

`docker-compose up -d`


**note:** Missing database connection as environment variables
-> Work in progress
