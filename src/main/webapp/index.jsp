<html>
<body>
<h2>OData API for fis.uni-bamberg.de</h2>

<h4>How to use the API (examples):</h4>

<h6>Overview of which metadata is accessible:</h6>
<a href="/ODataService.svc/$metadata?$format=application/json"><p>odata.fis.uni-bamberg.de/ODataService.svc/$metadata?$format=application/json</p></a>

<h4>Get Collection of Entities:</h4>
<h6>Get all Projects:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects</p>

<h6>Get all Researchers:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers</p>

<h6>Get all Orgunits:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits</p>

<h4>Get a specific entity:</h4>
<h6>Get Project entity with id 5:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects(5)</p>

<h6>Get Researcher entity with id 5:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers(5)</p>

<h6>Get Orgunit entity with id 5:</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)</p>


<h4>Get entities which are linked to each other</h4>
<h6>Get all Publications of OrganisationUnit with id 5</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Publications</p>

<h6>Get all Projects of Researcher with id 5</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers(5)/Projects</p>

<h6>Get all Researchers of OrganisationUnit with id 5</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Researchers</p>

<h4>Further options to add to request:</h4>
<h6>Get Project entity with id 5 only containing metadata about attribute "title":</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects(5)?$select=title</p>

<h6>Get Organisation Unit entity with id 5 and include all Researchers belonging to this entity</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)?$expand=Researchers</p>

<h6>Get Publications of Orgunit with id 5 filtered by type 'book':</h6>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Publications?$filter=contains(type,'book')</p>

<h6>Get Publications of Researcher with id 5 and order the result by completedyear</h6>
<p>odata.fis.uni-bamberg.de/ODataService.svc/Researchers(5)/Publications?$orderby= completedyear desc</p>

<h4>How to use Citation Style Language:</h4>
<h6>There are two methods which can be used to create a csl style: cslforresearcher(style,id);cslfororgunit(style,id)</h6>

<h6>Get Publications of researcher with id 1411 in style 'apa'</h6>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslforresearcher(style='apa',id=1411)</p>

<h6>Get Publications of orgunit with id 11 in style 'ieee'</h6>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslfororgunit(style='ieee',id=11)</p>

<h6>It is still possible to add filter or order options to the uri:</h6>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslfororgunit(style='ieee',id=11)?$filter=contains(type,'book')</p>

</body>
</html>
