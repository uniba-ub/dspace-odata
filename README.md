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


### Environment variable need to be added

The Solr Url must be added as an environment variable to start the api. The Url must point to the DSpace Solr instance.

**variable:** `SOLR_URL`

Example value:

**value:** `http://localhost:8080/solr/search`




### Use OData API

Examples:

* Get all Projects
`http://localhost:8080/ODataService/ODataService.svc/Projects`

* Get all Researchers
`http://localhost:8080/ODataService/ODataService.svc/Researchers`

* Get all Publications
`http://localhost:8080/ODataService/ODataService.svc/Publications`

* Get all Orgunits
`http://localhost:8080/ODataService/ODataService.svc/Orgunits`

* Get Researcher with id 1
`http://localhost:8080/ODataService/ODataService.svc/Researchers(1)`

* Get Publication with id 7
`http://localhost:8080/ODataService/ODataService.svc/Publications(7)`

* Get all Publications of Orgunit with id 5
`http://localhost:8080/ODataService/ODataService.svc/Orgunits(5)/Publications`

* Get all Researchers of Orgunit with id 5
`http://localhost:8080/ODataService/ODataService.svc/Orgunits(5)/Researchers`

* Get all Publications of Researcher with id 5
`http://localhost:8080/ODataService/ODataService.svc/Researchers(5)/Publications`

* Get all Researchers of Orgunit and Orgunit data with id 5 in single JSON Object
`http://localhost:8070/ODataService/ODataService.svc/Orgunits(14)?$expand=Researchers`

### API Architecture
rough draft of the architecture:

![](odata-architecture.bmp)

### More informations and filter options of OData

please have a look at **odata.pdf**
