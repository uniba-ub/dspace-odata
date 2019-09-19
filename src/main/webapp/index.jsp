<html>
<body>
<h2>OData API for fis.uni-bamberg.de</h2>

<h4>How to use the API (examples):</h4>

<h6>Overview of which metadata is accessible:</h6>
<p>odata.fis.uni-bamberg.de/$metadata?$format=application/json</p>

<h6>Get Collection of Entities:</h6>
<b>Get all Projects:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects</p>

<b>Get all Researchers:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers</p>

<b>Get all Orgunits:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits</p>

<h6>Get a specific entity:</h6>
<b>Get Project entity with id 5:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects(5)</p>

<b>Get Researcher entity with id 5:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers(5)</p>

<b>Get Orgunit entity with id 5:</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)</p>


<h6>Get entities which are linked to each other</h6>
<b>Get all Publications of OrganisationUnit with id 5</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Publications</p>

<b>Get all Projects of Researcher with id 5</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Researchers(5)/Projects</p>

<b>Get all Researchers of OrganisationUnit with id 5</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Researchers</p>

<h6>Further options to add to request:</h6>
<b>Get Project entity with id 5 only containing metadata about attribute "title":</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Projects(5)?$select=title</p>

<b>Get Organisation Unit entity with id 5 and include all Researchers belonging to this entity</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)?$expand=Researchers</p>

<b>Get Publications of Orgunit with id 5 filtered by type 'book':</b>
<p>odata.fis.uni-bamberg.de/ODataService/ODataService.svc/Orgunits(5)/Publications?$filter=contains(type,'book')</p>

<b>Get Publications of Researcher with id 5 and order the result by completedyear</b>
<p>odata.fis.uni-bamberg.de/ODataService.svc/Researchers(5)/Publications?$orderby= completedyear desc</p>

<h6>How to use Citation Style Language:</h6>
<b>There are two methods which can be used to create a csl style: cslforresearcher(style,id);cslfororgunit(style,id)</b>

<b>Get Publications of researcher with id 1411 in style 'apa'</b>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslforresearcher(style='apa',id=1411)</p>

<b>Get Publications of orgunit with id 11 in style 'ieee'</b>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslfororgunit(style='ieee',id=11)</p>

<b>It is still possible to add filter or order options to the uri:</b>
<p>odata.fis.uni-bamberg.de/ODataService.svc/cslfororgunit(style='ieee',id=11)?$filter=contains(type,'book')</p>

</body>
</html>
