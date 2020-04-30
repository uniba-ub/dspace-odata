<%@ page import="javax.servlet.ServletContext" %>

<%
//adopt to your needs
String baseurl = "odata.fis.uni-bamberg.de/";
String folder = "";
String url = baseurl + folder;
%>
<!DOCTYPE html>
<html>
<head>
	<title>OData Uni-Bamberg</title>
	<style>
	pattern {margin:5px; padding:5px;}
code   {
border:2px solid lightgrey; color:#4c5f07;}
h2    {color: #00457D;}
h4    {color: #003866;}
h6    {color: #002a4d;}
</style>
<body>

	<h2 id="top">OData API for fis.uni-bamberg.de</h2>
	<div>
	<p>This API offer read-access to contents of the FIS. 
	It is based on the <a href="https://docs.oasis-open.org/odata/odata/v4.01/">OASIS OData Specification</a>, Version 4.0.1. 
	The standard response format is JSON. </p>
	<p><a href="generator.html">Query-Generator (Alpha)</a></p>
	<p>Table of Content:
	<ul>
	<li><a href='#top'>OData API for fis.uni-bamberg.de</a></li>
	<li><a href='#ent'>Entities and Relations</a></li>
	<li><a href='#sample'>Sample Requests</a>
	<ul>
	<li><a href='#request-entity-set'>Get Collection of Entities</a></li>
	<li><a href='#request-entity'>Get a specific entity</a></li>
	<li><a href='#request-entity-relation'>Get entities which are linked to each other</a></li>
	<li><a href='#filters'>Filters</a></li>
	<li><a href='#functions'>Functions</a>
	<ul><li><a href='#csl'>CSL</a></ul></li></ul>
	</ul>
	</div>
	
	<h4 id="ent">Entities and their Relations:</h4>
	<div>
<img src="schema.svg" alt="Entities and their Relations" height="400px">
<p>Visual overview on entities and their relations. Coming up entities are marked with <code>*</code> . All relations can also be traversed in their inverse relationship, e.g. Researchers/Publications and Publications/Researchers. Relations can only be traversed by a depth of 2.</p>
	
	<h6>Complete overview of metadata (all entities, properties and relations</h6>
	<a href="/ODataService.svc/$metadata?$format=application/json">
		<p><%=baseurl %>ODataService.svc/$metadata?$format=application/json</p>
	</a>
	</div>
	
	<h2 id="sample">Sample Requests:</h2>
	<div>
	<p>All Requests are GET-Requests. POST-Requests are not supported by now.</p>
	<h4>How to use the API (examples):</h4>

	
	<h4 id="request-entity-set">Get Collection of Entities: </h4>
	<p class="pattern">Pattern: <code>{Service}/{EntitySet}/</code></p>
		
	<h6>Get all Projects:</h6>
	<p><%=url %>ODataService.svc/Projects</p>

	<h6>Get all Researchers:</h6>
	<p><%=url %>ODataService.svc/Researchers</p>

	<h6>Get all Orgunits:</h6>
	<p><%=url %>ODataService.svc/Orgunits</p>
	
	<h6>Get all Journals:</h6>
	<p><%=url %>ODataService.svc/Journals</p>

	<h4 id="request-entity">Get a specific entity:</h4>
	<p class="pattern">Pattern: <code>{Service}/{EntitySet}(id)/</code></p>
	<h6>Get Project entity with id 5:</h6>
	<p><%=url %>ODataService.svc/Projects(5)</p>

	<h6>Get Researcher entity with id 5:</h6>
	<p><%=url %>ODataService.svc/Researchers(5)</p>

	<h6>Get Orgunit entity with id 5:</h6>
	<p><%=url %>ODataService.svc/Orgunits(5)</p>
	
	<h6>Get Journal entity with id 28:</h6>
	<p>o<%=url %>ODataService.svc/Journals(28)</p>

	<h4 id="request-entity-relation">Get entities which are linked to each other</h4>
	<p class="pattern">Pattern:	<code>{Service}/{EntitySet}(id)/RelatedEntitySet</code></p>
	
	<h6>Get all Publications of OrganisationUnit with id 5</h6>
	<p><%=url %>ODataService.svc/Orgunits(5)/Publications</p>

	<h6>Get all Projects of Researcher with id 5</h6>
	<p><%=url %>ODataService.svc/Researchers(5)/Projects</p>

	<h6>Get all Researchers of OrganisationUnit with id 5</h6>
	<p><%=url %>ODataService.svc/Orgunits(5)/Researchers</p>

	<h6>Get all Publications of Journal with id 28</h6>
	<p><%=url %>ODataService.svc/Journals(28)/Publications</p>
	
	<h6>Get all Publications of Project with id 5</h6>
	<p><%=url %>ODataService.svc/Projects(5)/Publications</p>
	
	<h4 id="filters">Further options to add to request:</h4>
	<div>
	<p>Filter and Options can be added as Parameters <code>?</code></p>
	<p>This implementation supports main functionality from the <a href="https://www.odata.org/getting-started/basic-tutorial/#queryData">Odata Specification</a>. For Syntax have a look there:</p>
	<p><ul><li><code>?$orderby=</code> : Sort results on a parameter ascending or descending. Sorting on two levels is possible , e.g. '$orderby=completedyear desc, author asc'</li>
	<li><code>?$top=</code> : Return top x entries  </li>
	<li><code>?$count=</code> : Return number of entries  </li>
	<li><code>?$skip=</code> : Skip x entries  </li>
	<li><code>?$expand=</code> : expand selected entity with further information  </li>
	<li><code>?$filter=</code> : apply filter to content. Filter Expression is evaluated in a boolean tree.  </li>
	<li><code>?$format=</code> : requested serialization format: $format=application/atom+xml , $format=application/json </li>
	</ul></p>
	</div>
	
	<h6>Get Project entity with id 5 only containing metadata about attribute "title":</h6>
	<p><%=url %>ODataService.svc/Projects(5)?$select=title</p>

	<h6>Get Organisation Unit entity with id 5 and include all Researchers belonging to this entity</h6>
	<p><%=url %>ODataService.svc/Orgunits(5)?$expand=Researchers</p>

	<h6>Get Project entity with id 5 and include all Publications belonging to this entity</h6>
	<p><%=url %>ODataService.svc/Projects(5)?$expand=Publications</p>
	
	<h6>Get Journal entity with id 5 and include all Publications belonging to this entity</h6>
	<p><%=url %>ODataService.svc/Journals(5)?$expand=Publications</p>

	<h6>Get Publications of Orgunit with id 5 filtered by type 'book':</h6>
	<p><%=url %>ODataService.svc/Orgunits(5)/Publications?$filter=contains(type,'book')</p>

	<h6>Get Publications of Researcher with id 5 and order the result by completedyear</h6>
	<p><%=url %>ODataService.svc/Researchers(5)/Publications?$orderby= completedyear desc</p>

	<h4 id="functions">Functions</h4>
	<p class="pattern">Pattern: <code>{Service}/{Function(Param*)}</code></p>
	<p>Filter options can also be applied to functions</p>
	<h5 id="csl">How to use Citation Style Language:</h5>
	
	<p>There are several methods which can be used to create a csl style: cslforresearcher(style,id);cslfororgunit(style,id) etc...</p>

	<h6>Get Publications of researcher with id 1411 in style 'apa'</h6>
	<p><%=baseurl %>ODataService.svc/cslforresearcher(style='apa',id=1411)</p>

	<h6>Get Publications of orgunit with id 11 in style 'ieee'</h6>
	<p><%=baseurl %>ODataService.svc/cslfororgunit(style='ieee',id=11)</p>

	<h6>It is still possible to add filter or order options to the uri:</h6>
	<p><%=baseurl %>ODataService.svc/cslfororgunit(style='ieee',id=11)?$filter=contains(type,'book')</p>
	
	<h6>Get Publications of researcher with id 1411 in style 'apa'</h6>
	<p><%=baseurl %>ODataService.svc/cslforresearcher(style='apa',id=1411)</p>
	
	<h6>Get Publications of journal with id 28 in style 'apa'</h6>
	<p><%=baseurl %>ODataService.svc/cslforjournal(style='apa',id=28)</p>
	
	<h6>Get Publications of project with id 236 in style 'apa'</h6>
	<p><%=baseurl %>ODataService.svc/cslforproject(style='apa',id=236)</p>

	</div>
	
</body>

</html>