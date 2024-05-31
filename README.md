### About
The configuration for these odata entities is aligned to the structure which is currently defined in the   
[DSpace-Cris](https://github.com/4Science/DSpace/tree/dspace-cris-2023_02_x) `dspace-cris-2023_02_x` branch which is itself aligned to the [OpenAIRE Guidelines for Cris Manager](https://github.com/openaire/guidelines-cris-managers).   

It is currently `Work in Progress` and will maintain some updates in near future.

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


### Adjustments regarding data model of dspace instance

Already existing Entitys (Publication, Project, Project, Orgunit) can be used as default.

Probably need to be changed for entitys (regarding your database model):

- `Resource_TYPE_FILTER` need to be changed, if you are using a different resource type filters at your dspace installation. You can find your Resource Type Filters in solr: `localhost:8080/solr/#/search/schema-browser?field=resourcetype_filter`
- The Items are adresses using the uuid of the item in search.
- Metadata field names and mapping of names in all Entities regarding your database model   

All new Entities with metadata need to be definied in `src/main/java/entitys/` and added to `src/main/java/entitys/EntityRegister`.
Relations between those entities and ComplexProperties (which represents NestedObjects from DspaceCris) need to be registered in the `EntitiyRegister` as well. 


### Use OData API

Examples:

* Get all Projects
`http://localhost:8080/ODataService/ODataService.svc/Projects`

* Get all Persons
`http://localhost:8080/ODataService/ODataService.svc/Persons`

* Get all Publications
`http://localhost:8080/ODataService/ODataService.svc/Publications`

* Get all Orgunits
`http://localhost:8080/ODataService/ODataService.svc/Orgunits`

* Get Persons with id 1
`http://localhost:8080/ODataService/ODataService.svc/Persons('1')`

* Get Publication with id 7
`http://localhost:8080/ODataService/ODataService.svc/Publications('7')`

* Get all Publications of Orgunit with id 5
`http://localhost:8080/ODataService/ODataService.svc/Orgunits('5')/Publications`

* Get all Persons of Orgunit with id 5
`http://localhost:8080/ODataService/ODataService.svc/Orgunits('5')/Persons`

* Get all Publications of Person with id 5
`http://localhost:8080/ODataService/ODataService.svc/Persons('5')/Publications`

* Get all Persons of Orgunit and Orgunit data with id 5 in single JSON Object
`http://localhost:8070/ODataService/ODataService.svc/Orgunits('14')?$expand=Persons`

* Filter Persons Name with String 'Test'
`http://localhost:8080/ODataService/ODataService.svc/Persons?$filter=contains(name,'Test')`

* Filter Publications of a Person with String 'Book'
`http://localhost:8080/ODataService/ODataService.svc/Persons('5')/Publications?$filter=contains(type,'Book')`

### Use OData API with CSL

* Get all Publications of Person with id 5 in csl style 'apa'
`http://localhost:8080/ODataService/ODataService.svc/cslforresearcher(style='apa',id='5')`

* Get Publications of orgunit with id 11 in style 'ieee'
`http://localhost:8080/ODataService/ODataService.svc/cslfororgunit(style='ieee',id='11')`

* It is still possible to add filter or order options to the uri:
`.../cslfororgunit(style='ieee',id='11')?$filter=contains(type,'book')`


### API Architecture
rough draft of the architecture:

![](odata-architecture.jpg)

### More informations and filter options of OData

please have a look at:
![Apache Olingo](https://olingo.apache.org/doc/odata4/index.html)
![Olingo OData 4.0 Javadoc](https://olingo.apache.org/javadoc/odata4/index.html)


## possible usage
This plugin can be used together in Typo3, e.g. with the plugin provided here https://gitlab.rz.uni-bamberg.de/rz/rz/dspace-typo3-plugin
- 
