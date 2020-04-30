/**
 * OData Schema Parser and Query Generator
 * florian.gantner@uni-bamberg.de - Universität Bamberg 2020
 * 
 * This tool fetches the OData-Schema and generates forms for building querys to odata-databases.
 * It allows the building of Querys-URL by parsing existing Querys or building completly new. 
 * Configurations can be modified by defining the urls, adding white/blacklist and module options and human-readable aliases for schema-syntax names. 
 * 
 * This tool was and opimized for the needs and use with our Dspace Odata API and our Data Model.
 * Therefore it does not support all functions of OData, only those .
 * What is known to the author and is not supported by the tool:
 * - Entity()/Entity() Selections
 * - Selections over more than two Paths Entity/Entity/Entity
 * - other Data-types apart from Int/String
 * - skip-options
 * - complex filters (those with brackets) 
 * 
 * How to start?
 * - include jquery (We build the tool with the latest, 3.4.1)
 * - include generator.js file
 * - modify options in generator.js file //TODO: add complex  
 * - define div where Generator-Content is parsed
 * - follow the steps below:
 * To include in html, provide jquery attribute with element's unique id
 * call the functions as shown below to providefull functionality
 * 
 * Example: 
 * <div id="generator></div>
 * 
 * var generator = new Generator($("#generator"));
 * generator.loadSchema();
 * generator.createGUI();
 * generator.registerEvents();
 */

var Generator = function(options){
	/*This is the main function called for initializing functions. it provide internal and external (.this) available function and variables.
	 * Constructor Class is called at end
	 */
	
	/* Urls:
	 * serviceUrl is checked. Because of missing CORS-Headers in the OData-Response access to foreign servers might not be available
	 * */
	const returnUrl = "https://odata.fis.uni-bamberg.de/";
	const serviceUrl = "ODataService.svc/";
	const schemaquery = serviceUrl + "$metadata?$format=application/json";
	const namespace = "dspace";
	
	/* TODO:List of allowed functions and modules being displayed.
	 * Allows Hiding of certain functions 
	 * */
	var modules_active = [];
	var modules_all = ["parse", "query", "checkid" , "refentity", "entity" ,  "functions" , "expand" , "filter" , "orderby" , "selection" , "top"]; 
	
	/* Main Entities for displaying and fetching informations.
	 * */
	var divelem = "";//name of div being hijacked. Used for registering events 
	var entities = {}; // available entities (after white/blacklist)
	var ref_entities = {}; //available ref_entities for this entity (after white/blacklist))
	var entity = "Publication"; //actually selected entity. The one set here is selected by default after fetching the schema 
	var functionname = ""; //actually selected function
	var prop_filter = {}; //allowed properties
	var nav_filter = {}; //allowed navigationproperties
	var schema = {}; //copy of fetched Schema
	
	/* alias for entity
	 * Key-Value:Schema: "Publication" : "Publikation",
	 */
	var alias_entity = {
		"Publication" : "Publikation",
		"Series" : "Schriftenreihe",
		"Projects" : "Projekte",
		"OrgUnit" : "Einrichtungen",
		"Researcher" : "Forschende"
	}
	/* Alias for Properties
	 * - in dropdowns these Properties are listed before other Properties
	 *	Key-Value:Schema: { "Publication" : { "title" : "Titel" }}
	 */
	var alias_prop = {
		"Publication" : {
			"title" : "Titel"
		}
	}
	
	/* whitelist for Properties
	 * if contains property only these are displayed in dropdown
		Schema Key : [Val]
		"Publication" : ["title","author", "completedyear"]
	 */
	var whitelist_prop = {	
	}
	
	/* blacklist for Properties	
	 * props are not shown. Not compatible with use of whitelist
	 * Schema Key : [Val]
	 * "Publication" : ["title","author", "completedyear"]
	 * */
	var blacklist_prop = {
		}
	
	/* blacklist for entities
	 * entities are not shown. Not compatible with use of whitelist
	 * f.e. for complex objects withour own id's a.k.a. nested entities in dspace
	 * */
	var blacklist_entity = {
	"Award" : "",
	"Awardseries" : "",
	"Container" : "",
	"Funding" : "",
	"Partnership" : ""
	}
	
	/* whitelist for entities
	 * only display entities and functions in whitelist
	 * Schema Properties schema: Key :Val -> [] filters referenced entitites
	 * "Publication" : ["Researchers","Journals"],
	 *				"cslforproject" : [],
	 *				"cslforjournal" : []
	  */
	var whitelist_entity = {
			}
	 
	/*
     * Constructor
     * TODO: add further options apart from divelem here. Overloading constructors is not possible
     */
    this.construct = function(options){
        //external constructor
		//$.extend(divelem , options);
        divelem = options;
    };
    
    this.loadSchema = function (){
    	//external function, fetches the Schema from Url and sets the schemaand generate Picker
    	//TODO: bad response or schema not accessible/readable?
    	try{
    	$.get( schemaquery, function(response){
    		console.log(response);	
    		schema = response[namespace];
    		if(schema == null || schema == undefined){
    			throw "Schema not available";
    		}
    		generateEntityPicker(response[namespace]);	
    		selectEntity(entity);
    	});	
    	}catch(msg){
    		console.log(msg);
    	}
    };
    
    this.createGUI = function(){
    	//Text for generating the forms
    	//TODO: add selected options for modules here
    	$(divelem).html(`<div>
    			Entität / Funktion:
    				<select id="entity">
    				</select>
    				<label for="entityid">mit Identifikator</label>
    				<input type="text" id="entityid" name="entityid" placeholder="id oder leer für alle"></input>
    				</div>
    				<div id="refentitypicker">
    				<input type="checkbox" id="refentitycheck"> einer Entität: </input>
    				<select id="refentity" disabled></select>
    				<label for="refentityid" disabled> mit Identifikator</label>
    				<input type="text" id="refentityid" placeholder="id oder leer für alle"></input>
    				<input type="checkbox" id="expand" disabled></input>
    				<label for="expand">Expand (Inkludiere in erste Entität)</label>
    				</div>
    				<div id="functionpicker"></div>
    				<div>
    				Filterung <button id="add-filter">[+]</button><p id="filter"></p>
    				</div>
    				<div>
    				Sortierung
    				<button id="add-orderby">[+]</button>
    				<p id="orderby"></p>
    				</div>
    				<div>
    				Selektion (Wähle bestimmte Attribute aus)
    				<button id="add-select">[+]</button>
    				<p id="select"></p>
    				</div>
    				<p><input type="number" id="top"></input>
    				<label for="top">maximale Anzahl Ergebnisse</label>
    				</p>
    				<div id="idcheck"><button type="submit" class="action">Überprüfe Existenz der eingegebenen ID</button><p id="idcheck-result"></p></div>
    				<div>
    				<button type="submit" id="generatequery" class="action">Generiere Link aus Formular</button><button type="submit" id="parsequery" class="action" disabled>Übertrage Link auf Formular</button>
    				<p>
    				<textarea id="fullurl" placeholder="Ergebnis" rows=4 cols=100></textarea>
    				<label for="fullurl">Gesamtergebnis</label>
    				</p>
    				</div>`);
    }
    
    this.parseQuery = function(query){
    	//external function -> renders/parses query and select option on form
    	parseQueryToForm(query);
    }

    function parseQueryToForm(querys){
    	//internal function
    	//this assumes gui has been build
    	try{
    	if(querys == "" || querys == undefined){
    		throw "No Query to parse found";
    	}
    	// break URI From Query into parts and distinguish options.
    	// replace returnUrl and serviceUrl 
		let query = querys.replace(returnUrl + serviceUrl, "");
		let queryparts = query.split("?");
		
		//-> Split Paths
		//Determine if first Pathelement is more likely a Function or Entity Set
		//Allows up to two Entity-Sets
		let paths = queryparts[0].split("/");
		if(paths.length == 1){
			let pathparts = paths[0].split("(");
			//Check if is Function
			var isFunction = isContainerFunction(pathparts[0]);
			if(!isFunction){
				//Get Response Entity Name for Container ans delect these
				selectEntity(fetchEntityForContainer(pathparts[0]));
				$(divelem).find("select#entity").val(fetchEntityForContainer(pathparts[0]));
				//set Entity id
				let varid = pathparts[1].replace(")", "")
				$(divelem).find("input#entityid").html(varid).val(varid);
				
			}else{
				//set Function as Entity and parse parameters 
				selectEntity(pathparts[0]);
				// parameters
				let functionparam = pathparts[0]
				var params = schema[pathparts[0]][0]["\$Parameter"];	
				//Parse the Parameter of the Function
				fillFunctionPicker(params, pathparts[1].replace(")", "") , functionparam);
				$("#entityid").val();
				$("#refentityid").val();
			}
		
		}else if(paths.length == 2){
			//Two Entity-Sets, the first one with id
    		let pathparts0 = paths[0].split("(");
    		let pathparts1 = paths[1].split("(");
    		let varid = pathparts0[1].replace(")", "");
    		
    		selectEntity(fetchEntityForContainer(pathparts1[0]));
    		$(divelem).find("input#refentityid").html(varid).val(varid);
    		$(divelem).find("select#refentity").val(pathparts0[0]);
    		
    		enableRefEntity();
    		$(divelem).find("#refentitycheck").prop('checked', 'true');
    		//For Two EntitySets with ID, also consider second identifier
    		if(pathparty1[1] != undefined){
    		let varid2 = pathparts1[1].replace(")", "");
       		$(divelem).find("input#entityid").html(varid2).val(varid2);
     		$(divelem).find("input#entityid").attr("disabled", false);
    		}
		}	
	
		// Parse Options, as Parameter on URI
		//Options are already separated from Query by ?
	if(queryparts[1] != null){
	resetOrderOptions();
	var options = queryparts[1].split("$"); //TODO: checkif $ as part of query content is considered
	for(let option of options){
		option = option.replace(/&/g, ""); //replace connector option of option
		let optionentry = option.split("=");
		
		if(optionentry[0] == "filter"){
			//TODO: split by two Connector is not included correctly.and / or is part of result when using regex
			let filters = optionentry[1].split(/\b(and|or)/);
			for(let filter in filters){
				let filterval = filters[filter].split(" ").filter( e => e.trim().length > 0);
			//TODO: split by " " -> complexfilters (with brackets) are not supported by now
			//dropdowntype condition connector
			$(divelem).find("#filter").append(generateAndFillFilter("filter", filter, prop_filter, filterval[0] , filterval[1] , filterval[2] , filterval[3]));		
	    	$(divelem).find(".filterentry > .connector").removeAttr("disabled").last().attr("disabled", "true");	
			}
			
		}else if(optionentry[0] == "orderby"){
		//split by , -> allowing up to two orderby's
			let optionsorderbys = optionentry[1].split(",");
			for(let orderby in optionsorderbys){
				let orderval = optionsorderbys[orderby].split(" ");
		    	$(divelem).find("#orderby").append(generateAndFillOrderBy("orderby", orderby, prop_filter, orderval[0], orderval[1]));
		    	//TODO disable options add if one is showns
			}			
			
		}else if(optionentry[0] == "top"){
			$(divelem).find("#top").val(optionentry[1]);
			
		}else if(optionentry[0] == "select"){
			let optionselects = optionentry[1].split(",");
			for(let select in optionselects){
				$(divelem).find("#select").append(generateAndFillSelect("select", select, prop_filter, optionselects[select]));
			}
			
		}else if(optionentry[0] == "expand"){
			$(divelem).find("#expand").prop('checked', 'true');
        	enableRefEntity();
			//TODO: expand has to be considered in Path as Well,because only one path is selected. Not Working properly, wrong entity is selected 
        	//selectEntity(optionentry[0]);
        	selectRefEntity(fetchEntityForContainer(optionentry[0]));
		}
		
		}
	}else{
		// reset to empty
		resetOrderOptions();
	}		
	
	}catch(e){
		//resetOptions();
		console.log("Error while parsingQuery" + e);
	}finally{
		console.log("Query Parsing ended");
	}
    }
 
    function fillPropFilter(prop_total){
    	// Fills Properties and NavigationFilter from all Filters
    	try{
    	prop_filter = {};
    	nav_filter = {};
    	//For Functions: get Corresponding Result entity and their properties
    	for(const it in prop_total){
    		//filter navigation properties and other attributes
    	if(!(it.startsWith('\$') || prop_total[it]["\$Kind"])){
    		prop_filter[it] = prop_total[it];
    	}else if(prop_total[it]["\$Kind"] && prop_total[it]["\$Kind"] == "NavigationProperty"){
    		nav_filter[it] = prop_total[it];
    	}
    	}	
    	}catch(e){console.log(e)};
    }

    function selectEntity(name){
    	//select one entity by it's name and changeGUI based on informations
    	try{
    	resetOptions();
    	entity = name;
    	//Get Information form Schema
    	if(schema[entity][0] && schema[entity][0]["\$Kind"] == "Function"){
    		//Function
    		var returntype = schema[entity][0]["\$ReturnType"]["\$Type"];
    		returntype = returntype.replace(namespace+".", "")
    		prop_total = schema[returntype];
    		entity = returntype;
    		functionname = name;
    		fillPropFilter(schema[entity]);
    		$("#functionpicker").html("").append(generateFunctionPicker(schema[name], name)).show();
    		$("#refentitypicker").hide();
    		$("#idcheck").hide();
    		$("#entityid").removeAttr("disabled");
    		setEntityIDType($("#entityid"), schema[entity]);
    	}else{
    		//Property
    		functionname = "";
    		fillPropFilter(schema[entity]);
    		generateRefEntityPicker(nav_filter);
    		$("#functionpicker").html("").hide();
    		$("#refentitypicker").show();
    		$("#idcheck").show();
    		setEntityIDType($("#entityid"), schema[entity]);
    	}
    	
    	//Default: Set one Orderby and one filter-Option
    	addFilter();
    	addOrderBy();
    	}catch(e){console.log(e)};
    }
    
    
    function selectRefEntity(val){
    	//Selecte Referenced/Second Entityset by it's name
    	$("#refentity").html(val).val(val);
    }

    function addOrderBy(){
    	try{
    	//Adds OrderBy-Option
    	$("#orderby").append(generateOrderBy("orderby", 1, prop_filter));
    	}catch(e){console.log(e)};s
    }
    function setEntityIDType(elem, schemaentry){
    	//sets the Type of the input of the entityid (String/ID)
    	try{
    	$(elem).attr("type", setEntityType(schemaentry));
    	}catch(e){console.log(e)};s
    }
    
    function setEntityType(schemaentry){
    	//returns type of input box based on schema-identifier for entity-id (refentity and entity)
    	try{
    	//get ID
    	let keyAttr = schemaentry["\$Key"][0]; 
    	if(schemaentry[keyAttr]["\$Type"].startsWith("Edm.Int")){
    		return "number";
    	}else if(schemaentry[keyAttr]["\$Type"] == "Edm.String"){
    		return "text";
    	}
    	//default:
    	return "text";
	}catch(e){console.log(e)};
    }
    
    function generateFunctionPicker(functionschema, name){
    	//Generate Selection for Function based on their attributes
    	try{
    	var res = $('<p class="functionheader '+name+'"/>');
    	for(var it in functionschema[0]["\$Parameter"]){
    		var param = functionschema[0]["\$Parameter"][it];
    		let type = "text";
    		if(param["\$Type"].startsWith("Edm.Int")){
    		type = "number";	
    		}
    		$(res).append("<p class='functionparam param-"+it+"' name=\""+param['\$Name']+"\">"+param['\$Name']+": <input type=\'"+type+"\'></input><p>");		
    	}
    	return res;
    	}catch(e){console.log(e)};s
    }
    
    function fillFunctionPicker(functionschema, values, nameofFunction){
    	//fills Values into Functionschema
    	//Fiter has been generated before
    	//TODO: Check if Int/String is correctly parsed
    	let valuessplit = values.split(",");
    	for(let value of valuessplit){
    		let valueentry = value.split("=");
    		let valueentry2 = valueentry[1].replace(/["']/g, "");
    		$(divelem).find("p[name="+valueentry[0]+"] > input").html(valueentry2).val(valueentry2);
    	}
    }

    function resetOptions(){
    	//reset all entries
    	resetOrderOptions();
    	$("#entityid").val(0);
    	$("#refentityid").val(0);
    }
    function resetOrderOptions(){
    	//reset all OrderOptions
    	$("#filter > p").remove();
    	$("#orderby > p").remove();
    	$("#select > p").remove();
    	$("#top").val(0); 	
    }

    function generateEntityPicker(entries){
    	//Generates main dropdown of entity selector based on whitelist and blacklist.UsesDisplayName as Alias
    	try{
    	var res = "";
    	for(var it in entries){	
    	var display = false;
    	if((Object.keys(whitelist_entity).length > 0 && whitelist_entity.hasOwnProperty(it))){
    	//check for whitelist and blacklise-entries
    	display = true;
    	}else if((Object.keys(blacklist_entity).length > 0 && blacklist_entity.hasOwnProperty(it))){
    		display = false;
    	}else{
    		//no lists
    		display = true;
    	}
    	//else no list, print
    	if(display){
    		//if(!(entries[it]["\$Kind"] && ( entries[it]["\$Kind"] == "ComplexType" || entries[it]["\$Kind"] == "EntityContainer" ) )){
    			res = res + "<option value=\""+it+"\">"+printDisplayName(it)+"</option>";
    			//}		
    		}
    	}
    	$("#entity").append(res);
    	}catch(e){console.log(e)};
    }


    function generateRefEntityPicker(entries){
    	//Generates main dropdown of entity selector based on whitelist and blacklist.UsesDisplayName as Alias
    	try{
    	var res = "";
    	for(var it in entries){
    	var display = false;
    	//check for whitelist and blacklist
    	if((Object.keys(whitelist_entity).length > 0 && whitelist_entity.hasOwnProperty(entity) && whitelist_entity[entity].includes(it))){
    	display = true;
    	}else if((Object.keys(blacklist_entity).length > 0 && blacklist_entity.hasOwnProperty(entity) && blacklist_entity[entity].includes(it))){
    		display = false;
    	}else{
    		//no lists
    		display = true;
    	}
    	//else no list, print
    	if(display){
    			res = res + "<option value=\""+it+"\">"+printDisplayName(it)+"</option>";
    	}

    	if(entries.length == 0){
    		$("#refentitypicker").hide();
    	}else{
    		$("#refentitypicker").show();
    	}
    	$("#refentity").html("").append(res);
    	}
    }catch(e){console.log(e)};
    }
   
    function printDisplayName(it){
    	//look at alias table and get Display Name
    	if(alias_entity.hasOwnProperty(it)){
    		return alias_entity[it];
    	}else{
    		return it;
    	}
    }
    
    function generateFilter(type, id, content){
    	//generate Filter Function
    	var res = $('<p class="filterentry" />' , { id : type+""+id });
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateFilterTypes());
    	$(res).append(generateFilterCondition());
    	$(res).append(generateFilterConnector());
    	$(res).append(generateDeleteButton());
    	return res;
    }

    function generateAndFillFilter(type, id, content, value1, value2, value3, value4){
    	//generate Filter Function and set Values
    	var elem = generateFilter(type, id, content);
		$(elem).find("select.prop").val(value1);
		$(elem).find("select.type").val(value2);
		$(elem).find("input.condition").val(value3).html(value3);
		$(elem).find("select.connector").val(value4);
		console.log(elem);
		return elem;
    }
    
    function generateOrderBy(type, id, content){
    	//generate OrderBy
    	var res = $('<p class="orderbyentry" />', { id : type+""+id});
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateOrderByTypes());
    	$(res).append(generateDeleteButton());
    	return res;
    }
    
    function generateAndFillOrderBy(type, id, content, value1, value2){
    	//generate OrderBy And Set Values
    	var elem = generateOrderBy(type, id, content);
		$(elem).find("select.prop").val(value1);
		if(value2 != null){ //sorting is optional
		$(elem).find("select.type").val(value2);
		}
		console.log(elem);
		return elem;
    }

    function generateSelect(type, id, content){
    	//generate Select
    	var res = $('<p class="selectentry" />', { id : type+""+id});
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateDeleteButton());
    	return res;
    }
    function generateAndFillSelect(type, id, content, value){
    	//generate Select and fill Value
    	var elem = generateSelect(type, id, content);
    		$(elem).find("select.prop").val(value);
    		return elem;
    }

    function generateDeleteButton(){
    	return "<button class='delete'>x</button>";
    }
    function generateOrderByTypes(){
    	return $('<select class="type" />').append("<option value='desc'>absteigend</option>").append("<option value='asc'>aufsteigend</option>");
    }
    function generateFilterTypes(){
    	return $('<select class="type" />').append("<option value='eq'>ist gleich</option>").append("<option value='ne'>nicht gleichs</option>").append("<option value='lt'>kleiner/älter</option>").append("<option value='gt'>größer/jünger</option>");
    }

    function generateFilterCondition(){
    	return $('<input class="condition"  type="text" placeholder="Bedingung"></input>');		
    }
    function generateFilterConnector(){
    	return $('<select class="connector" />').append("<option value='and'>und</option>").append("<option value='or'>oder</option>");
    }

    function generateDropdownListWithEntry(content){
    //generate Dropdown list
    	return $('<select class="prop" />').append(generateDropdownwithAttributes(content));
    }

    function generateDropdownwithAttributes(content){
    //Generate dropdown form given attributes. Check white/blacklist and check alias
    	try{
    var res = "";
    var res_preferred = "";
    for(const it in content){
    	//TODO: Get Mapping Name
    	var displayValue = printDisplayName(it);
    	
    	var display = false;
    		if((Object.keys(whitelist_entity).length > 0 && whitelist_entity.hasOwnProperty(entity) && whitelist_entity[entity].includes(it))){
    		//check for whitelist
    		display = true;
    		}else if((Object.keys(blacklist_entity).length > 0 && blacklist_entity.hasOwnProperty(entity) && blacklist_entity[entity].includes(it))){
    			//check for blacklist
    			display = false;    			
    		}else{
    			//no lists
    			display = true;
    		}
    		//else no list, print
    		if(display){
    			if(displayValue == it){
    				res = res + "<option content=\""+content[it]['$Type']+"\" value=\""+it+"\">"+it+"</option>";	
    				
    			}else{
    				res_preferred = res_preferred + "<option content=\""+content[it]['$Type']+"\" value=\""+it+"\">"+displayValue+"</option>";
    			}		
    		}
    
    }
    return res_preferred + res;

    function printDisplayName(it){
    	if(alias_prop.hasOwnProperty(entity) && alias_prop[entity].hasOwnProperty(it)){
    		return alias_prop[entity][it];
    	}else{
    		return it;
    	}
    }
    	}catch(e){console.log(e)};
    }

    this.registerEvents = function(){
    //external Function. Register Events on Created Elements
    	try{
    $(divelem).on("click", ".delete", function(){
    	//if orderby -> toggle add button
    	if($(this).parents('#orderby').length > 0){
    		//Toggle orderby-add, when max is achieved
    		if($("#orderby").find(".orderbyentry").length == 2){
    			$("#add-orderby").toggle();
    		}	
    	}
    	if($(this).parents('#filter').length > 0){
    		//disable last - 1 element
    		if($("#filter").find(".filterentry").length > 1){
    			$("#filter").find(".filterentry > .connector").eq(-2).attr("disabled", "true");
    		}	
    	}
    	$(this).parent('p').remove();
    	
    });
    
    $(divelem).on("click", "#add-orderby", function(){
    	//check length, otherwise toggle add button
    	addOrderBy();
    	if($("#orderby").find(".orderbyentry").length >= 2){
    		$("#add-orderby").toggle();
    	}
    });
    
    $(divelem).on("click", "#add-filter", function(){
    	addFilter();
    });

    $(divelem).on("click", "#add-select", function(){
    	//check length, otherwise toggle add button
    	$(divelem).find($("#select")).append(generateSelect("select", $("#select").find(".selectentry").length+1, prop_filter));
    });

    $("#refentityid").on("change", function(){
    	//if orderby -> toggle add button
    	$("#idcheck-result").html("");
    });

    $("#refentitycheck").on("change", function(){
    	//if orderby -> toggle add button
    	enableRefEntity();
    	$("#idcheck-result").html("");
    	if($("#refentitycheck").is(":checked")){
    		$("#refentityid").removeAttr("disabled");
    		$("#expand").removeAttr("disabled");
    		$("#refentity").removeAttr("disabled");
    		$("#entityid").attr("disabled", true);
    		
    		let val = $("#refentity").val();
        	val = fetchEntityForContainer(val);
        	setEntityIDType($("#refentityid"), schema[val])
    		
    	}else{
    		$("#entityid").removeAttr("disabled");
    		$("#refentityid").attr("disabled", true);
    		$("#expand").attr("disabled", true);
    		$("#refentity").attr("disabled", true);
    	}
    	
    });

    $("#entityid").on("change", function(){
    	//if orderby -> toggle add button
    	$("#idcheck-result").html("");
    });

    $("#refentityid").on("change", function(){
    	//if orderby -> toggle add button
    	$("#idcheck-result").html("");
    });
    $("#refentity").on("change", function(){
    	//if orderby -> toggle add button
    	//Ref-Entity uses containers
    	let val = $("#refentity").val();
    	val = fetchEntityForContainer(val);
    	setEntityIDType($("#refentityid"), schema[val])
    	$("#idcheck-result").html("");
    });
    

    $("#entity").on("change", function(){
    	//if orderby -> toggle add button
    	selectEntity($("#entity").val());
    });    
    
    $("#generatequery").on("click", function(){
    	//Parsing the Content can also be done by other libraries
    	try{
    	generateQuery()
    	checkfullUrl();
    	console.log("Query generated");
    	}catch(ex){
    		console.log("Error while generating query: " + ex);
    	}
        });

    $("#idcheck > button").on("click", function(){
    	//check function -> not possible,  entityset1 researcher not in schema, just Publications
    	try{
		idcheck();
		console.log("id checked");
    	}catch(e){
    		console.log("Error while checking existence of id: " + e);
    	}
    	});
    
    $("#parsequery").on("click", function(){
    	//Parsing the Content can also be done by other libraries
    	try{
    	parseQueryToForm($("#fullurl").val());
    	console.log("Querys parsed");
    	}catch(e){
    		console.log("Error while parsing query" + e);
    	}
        });
    
    $("#fullurl").on("change", function(){
    	console.log("change on fullurl" + $("#fullurl").val());
    	console.log(serviceUrl);
    	checkfullUrl();
    })
    
    	}catch(e){console.log(e)};
    } //end registerEvents
    
    function enableRefEntity(){
    	//Enables Reference Entity Set Elements
	$("#idcheck-result").html("");
	if($("#refentitycheck").is(":checked")){
		$("#refentityid").removeAttr("disabled");
		$("#expand").removeAttr("disabled");
		$("#refentity").removeAttr("disabled");
		$("#entityid").attr("disabled", true);
	}else{
		$("#entityid").removeAttr("disabled");
		$("#refentityid").attr("disabled", true);
		$("#expand").attr("disabled", true);
		$("#refentity").attr("disabled", true);
	}
}
    function checkfullUrl(){
    	//enable/disable ParseQuery Functionality based on fullurl information
    	if($(divelem).find("#fullurl").val().includes(serviceUrl)){ //TODO: startswith baseUrl + serviceUrl	
    		$("#parsequery").removeAttr("disabled");
    	}else{
    		$(divelem).find("#parsequery").attr("disabled", true);
    	}
    }
    
    function addFilter(){
    	 //add new Filter
    	$("#filter").append(generateFilter("filter", $("#filter").find(".filterentry").length+1, prop_filter));
    	//disable last entry
    	$("#filter").find(".filterentry > .connector").removeAttr("disabled").last().attr("disabled", "true");	
    }


    function fetchContainerSetForEntity (ent){
    	//Get Set Representation for the given entity (e.g. Publication -> Publications)
    	//schema -> Container -> EntityContainer -> Type -> namespace.
    	if(schema["Container"] && schema["Container"]["\$Kind"] == "EntityContainer"){
    		for(var it in schema["Container"]){
    			if(schema["Container"][it]["\$Type"] == namespace+"."+ent){
    				return it;
    			}
    		}
    	}
    }
    function fetchEntityForContainer(cont){
    	//Get Entity for the fiven Container (e.g. Publications -> Publication)
    	//schema -> Container -> EntityContainer -> Type -> namespace.
    	if(schema["Container"] && schema["Container"]["\$Kind"] == "EntityContainer"){
    		for(var it in schema["Container"]){
    			if(it == cont){
    				return schema["Container"][it]["\$Type"].replace(namespace+".", "");
    			}
    		}
    	}
    }
    
    function isContainerFunction(cont){
    	//check, if given Container is Function
    	console.log(schema[cont]);
    	if(schema[cont] && schema[cont][0]["\$Kind"] == "Function"){
    		return true;
    	}else{
    		return false;
    	}
    }

    function parseFilterToString(val, remain){
    	//input jquery val and serialize these filter contents to string
    	try{
    	var condition = $(val).find(".condition").val();
    	var type = $(val).find(".type").val();
    	var connector = $(val).find(".connector").val();
    	var prop = $(val).find(".prop").val();
    	var result = "";
    	if(condition == undefined || condition == "" || condition == " "){
    		//remove unused filter from display.
    		console.log("debug no property value, thus remove")
    		$(val).remove();
    		return result;
    	}
    	result = prop + " " + type + " " + condition;
    	if(remain == 1 || $(val).find(".connector").attr("disabled)")){
    		//no connector
    	}else{
    		result = result + " " + connector + " ";
    	}
    	return result;
    	}catch(e){console.log(e)};
    };
    
function generateQuery(){
	//Functions for Generating Query
	//base Url
	try{
	var result = returnUrl + serviceUrl;
	if(functionname != ""){
	var params = [];
		//Assume order of printing of parameters is correct
		$.each($("."+functionname).find(".functionparam"), function(i, val){
			params.push($(val).attr("name")+"=\'"+$(val).find("input").val()+"\'");
			});
		result = result + functionname + "(" + params.join(",") + ")";
	}else{
		
	}
		//Get Single Property		
		if(!$("#refentitycheck").is(":checked")){
			var ent_id = $("#entityid").val();
			if(ent_id != ""){ //Not fetching set
				//get first id-key and Format: Int (Default) or String
				let keyAttr = schema[entity]["\$Key"][0]; 
				if(schema[entity][keyAttr]["\$Type"] == "Edm.String"){
					//if String, replace Quotes around it
					ent_id = ent_id.replace(/["']/g, "");
					 ent_id = "'" + ent_id +  "'";
				}else if(schema[entity][keyAttr]["\$Type"].startsWith("Edm.Int")){
					//replace non-numeric values
					ent_id = ent_id.replace(/\D/g,'');
				}
				ent_id = "(" + ent_id +")";
			}
		result = result + fetchContainerSetForEntity(entity) + ent_id;	
		}else{
			//Get NavigationPathProperty
			var ref_ent_id = $("#refentityid").val();
			if(ref_ent_id != ""){ //Not fetching set
				ref_ent_id = "(" + ref_ent_id +")";
			}
			if($("#expand").is(":checked")){
				result = result + $("#refentity > option:selected").val() + ref_ent_id;
			}else{
				result = result + $("#refentity > option:selected").val() + ref_ent_id +"/" + fetchContainerSetForEntity(entity);	
			}
			//no need to fetch reference, already written as set.
		}
	
	//add Options ?
	var options = [];	
	
	//add expand funciton, if set
	if($("#expand").is(":checked") && $("#refentitycheck").is(":checked") && functionname == ""){
		options.push("$expand=" + fetchContainerSetForEntity(entity));
	}
	
	//add Filter $filter
	var optionsfilter = "";
	var filterstotal = $("#filter").find(".filterentry").length;
	$.each($("#filter").find(".filterentry"), function(i, val){
		//get Values(Property, Condition, Type, Connector)
		optionsfilter = optionsfilter + parseFilterToString($(val), (filterstotal - i));		
	});
	
	if(optionsfilter != ""){
	options.push("$filter=" + optionsfilter);
	}
	
	//add OrderBy $orderby
	var optionsorderby = [];
	$.each($("#orderby").find(".orderbyentry"), function(i, val){
		optionsorderby.push($(val).find(".prop").val() + " " + $(val).find(".type").val());
	});
	if(optionsorderby != ""){
	options.push("$orderby=" + optionsorderby.join(",")); 
	}
	
	//add Select $select
	var optionsselect = [];
	$.each($("#select").find(".selectentry"), function(i, val){
	optionsselect.push($(val).find(".prop").val());
	});
	if(optionsselect != ""){
	options.push("$select=" + optionsselect.join(",")); 
	}
	
	//add Top
	if($("#top").val() > 0){
		options.push("$top=" + $("#top").val()); 
	}
	if(options.length > 0){
	result = result + "?"+options.join("&");
	}
	$("#fullurl").val(result);
}catch(e){console.log(e)}finally{console.log($("#fullurl").val())}
}

function idcheck(){
	try{
	var result = returnUrl + serviceUrl;
	
	//check single entity
	if(!$("#refentitycheck").is(":checked")){
		result = result + fetchContainerSetForEntity(entity) + "("+$("#refentityid").val()+")";	
	}else{
	//Get NavigationPathProperty
		result = result + $("#refentity > option:selected").val() + "("+$("#refentityid").val()+")";
	}				
	
	   var idcheck = $.get(result);
	   idcheck.done(function(data, status){
		   console.log(data);
		   if (status == 'success'){
	    	$("#idcheck-result").html("").append(generateDataFromResult(data));
	    	$("#idcheck-result").css("border", "3px solid green");
	    }else{
	    	$("#idcheck-result").html("Nicht gefunden").css("border", "3px solid red");	
	    }
	   }).catch(function(err){
	    	$("#idcheck-result").html("Nicht gefunden").css("border", "3px solid red");	
		   console.log("Error occured fetching information for id: " + err);
	   });
	//check series
	
	   function generateDataFromResult(data){
		var todisplayifExist = ["cris-id", "id", "handle", "displayName", "name", "title"];
		var result = [];
		for(var it in todisplayifExist){
			var elem = todisplayifExist[it];
			if(data[elem])	{
				result.push(data[elem]);
			}
		}
		return result.join(" ; ");
		}
	}catch(e){console.log(e)};
}
   
    /*
     * Pass options to construct when class instantiated
     */
    this.construct(options);
}

