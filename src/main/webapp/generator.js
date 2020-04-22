/**
 * OData Schema Parser and Query Generator
 * florian.gantner@uni-bamberg.de - Universität Bamberg 2020
 * This tool fetches some OData-Schema and fills the Form with possibily options regarding a subset of the official odata syntax. Finally it allows the Creation of valid Query Links.
 * This tool was developed and Perfored for the Dspace Odata API.
 * 
 * How to start?
 * - include jquery
 * - include generator
 * - define div where Generator is parsed and define id as jquery
 * - follow the steps below
 * - options can be made in this file
 * To include in html, provide jquery attribute with element's unique id
 * call the functions as shown below to providefull functionality
 * Example: 
 * 
 * var generator = new Generator($("#generator"));
 * generator.loadSchema();
 * generator.createGUI();
 * generator.registerEvents();
 */

var Generator = function(options){
	
	 /*
     * Variables accessible
     * in the class
     */
	const returnUrl = "https://odata.fis.uni-bamberg.de/";
	const serviceUrl = "ODataService.svc/";
	const schemaquery = serviceUrl + "$metadata?$format=application/json";
	const namespace = "dspace";
	
	var divelem = "";//div being hijacked
	var entities = {}; // available entities
	var ref_entities = {}; //available ref_entities for this entity
	var entity = "Publication"; //actually selected entity
	var functionname = ""; //actually selected function
	var prop_filter = {}; //allowed properties
	var nav_filter = {}; //allowed navigationproperties
	var schema = {}; //completly fetched schema 
	
	var alias_entity = {
		"Publication" : "Publikation",
		"Series" : "Schriftenreihe",
		//alias for entity (perhaps define also sets)
	}
	
	var alias_prop = {
		//alias for property
		//these Properties are listed before other Properties
		//Entity Name -> Property Name
		"Publication" : {
			"title" : "Titel"
		}
	}
	
	var whitelist_prop = {
		//whitelist for Properties	
		//if contains property only these are displayed in dropdown
		//Schema Key : [Val]
		//"Publication" : ["title","author", "completedyear"]
	}
	var blacklist_prop = {
			//blacklist for Properties	
			//props are not shown. Not compatible with use of whitelist
			//Schema Key : [Val]
			//"Publication" : ["title","author", "completedyear"]
		}
	
	var blacklist_entity = {
	//blacklis for entities
	//Entities are not shown. Not compatible with use of whitelist
	"Award" : "",
	"Awardseries" : "",
	"Container" : "",
	"Funding" : "",
	"Partnership" : ""
	}
	var whitelist_entity = {
			//only display entities and functions in whitelist
			//Schema Properties schema: Key :Val -> [] filters referenced entitites
					/*"Publication" : ["Researchers","Journals"],
					"cslforproject" : [],
					"cslforjournal" : []*/
			}
	
	 
	 
	/*
     * Constructor
     */
    this.construct = function(options){
        //$.extend(divelem , options);
        divelem = options;
    };
    
    /*
     * fetch the schema from url
     * */
    this.loadSchema = function (){
    	$.get( schemaquery, function(response){
    		console.log(response);	
    		schema = response['dspace'];
    		generateEntityPicker(response['dspace']);	
    		selectEntity("Publication"); // Default Publication
    	});	    
    };
    
    this.createGUI = function(){
    	$(divelem).html(`<div>
    			Entität / Funktion:
    				<select id="entity">
    				</select>
    				<label for="entityid">mit Identifikator</label>
    				<input type="text" id="entityid" placeholder="id oder leer für alle"></input>
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
    				<button type="submit" id="generatequery" class="action">Generiere Link</button>
    				<p>
    				<textarea id="fullurl" placeholder="Ergebnis" rows=4 cols=100></textarea>
    				<label for="fullurl">Gesamtergebnis</label>
    				</p>
    				</div>`);
    }
    
    this.renderQuery = function(query){
    	//renders query to options
    	try{
    		let query = query.replace(returnUrl + serviceUrl, "");
    		var queryparts = query.split("?");
    		//split by paths
        	//check query Path (after Base Url)
        	//isfunction or is entity
    		//contains comma or = => function
    		//TODO: select correct Entity (also from Schema)
    		
    		
    	//for entities
    	//get Identifier of Entities
    		$("#entityid").val(0);
        	$("#refentityid").val(0);	
    	//for options -> split by ?
    	
    	if(querparts[1] != null){
    		resetOrderOptions();
    	var options = queryparts[1].split("$");
    	for(let option of options){
    		let optionentry = option.split("=");
    		if(optionentry[0] == "filter"){
    			//TODO: split by two 
				let filters = optionentry[1].split("and"|"or");
    			for(let filter in filters){
    				let filterval = filter.split(" ");
    			//TODO: split by " " -> complexfilters (with brackets) are not supported by now
    			//dropdowntype condition connector
    			$("#filter").append(generateAndFillFilter("filter", filter, prop_filter, filterval[0] , filterval[1] , filterval[2] , filterval[3]));
    			//disable last connector
    			}
    			
    		}else if(optionentry[0] == "orderby"){
    		//split by , -> two orderby?
    			let optionsorderbys = optionentry[1].split(",");
    			for(let orderby in optionsorderbys){
    				let orderval = optionsorderbys[orderby].split(" ");
    		    	$("#orderby").append(generateAndFillOrderBy("orderby", orderby, prop_filter, orderval[0], orderval[1]));
    			}
    			//disableoptionsadd if one is showns
    			
    		}else if(optionentry[0] == "top"){
    			$("#top").val(optionentry[1]);		
    		}else if(optionentry[0] == "select"){
    			let optionselects = optionentry[1].split(",");
    			for(let select in optionselects){
    				$("#select").append(generateAndFillSelect("select", select, prop_filter, optionselects[select]));
    			}
    		}
    		}
    	}else{
    		//else reset to empty
    		resetOrderOptions();
    	}		
    	
    	}catch(e){
    		resetOptions();
    		console.log("Error while parsingQuery");
    		console.log(e);
    	}finally{
    		console.log("Query Parsing ended");
    	}
    }

    function fillPropFilter(prop_total){
    	prop_filter = {};
    	nav_filter = {};
    	
    	console.log(prop_total);
    	//For Functions: get Corresponding Result entity and their properties
    	for(const it in prop_total){
    		//filter navigation properties and other attributes
    	if(!(it.startsWith('\$') || prop_total[it]["\$Kind"])){
    		prop_filter[it] = prop_total[it];
    	}else if(prop_total[it]["\$Kind"] && prop_total[it]["\$Kind"] == "NavigationProperty"){
    		nav_filter[it] = prop_total[it];
    	}
    	}	
    	console.log(prop_filter);
    }

    function selectEntity(name){
    	resetOptions();
    	//set new Prop Filter
    	entity = name;
    	//getEntityShortname
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
    	}else{
    		//Property
    		functionname = "";
    		fillPropFilter(schema[entity]);
    		generateRefEntityPicker(nav_filter);
    		$("#functionpicker").html("").hide();
    		$("#refentitypicker").show();
    		$("#idcheck").show();
    	}
    	
    	//Default: Set one Orderby and one filter-Option
    	addFilter();
    	addOrderBy();
    }

    function addOrderBy(){
    	$("#orderby").append(generateOrderBy("orderby", 1, prop_filter));
    }

    function generateFunctionPicker(functionschema, name){
    	//Generate Selection for Function based on their attributes
    	console.log(functionschema);
    	console.log(name);
    	var res = $('<p class="functionheader '+name+'"/>');
    	for(var it in functionschema[0]["\$Parameter"]){
    		var param = functionschema[0]["\$Parameter"][it];
    		$(res).append("<p class='functionparam param-"+it+"' name=\""+param['\$Name']+"\">"+param['\$Name']+": <input type=\'text\' content=\'"+param['\$Type']+"\'></input><p>");		
    	}
    	return res;
    }

    function resetOptions(){
    	//reset all entries
    	resetOrderOptions();
    	$("#entityid").val(0);
    	$("#refentityid").val(0);
    }
    function resetOrderOptions(){
    	$("#filter > p").remove();
    	$("#orderby > p").remove();
    	$("#top").val(0); 	
    }

    function generateEntityPicker(entries){
    	var res = "";
    	for(var it in entries){	
    	var display = false;
    	if((Object.keys(whitelist_entity).length > 0 && whitelist_entity.hasOwnProperty(it))){
    	//check for whitelist
    	display = true;
    	}else if((Object.keys(blacklist_entity).length > 0 && blacklist_entity.hasOwnProperty(it))){
    		//check for blacklist
    		display = false;
    	//check for blacklist
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
    }

    function printDisplayName(it){
    	//look at alias table and get Display Name
    	if(alias_entity.hasOwnProperty(it)){
    		return alias_entity[it];
    	}else{
    		return it;
    	}
    }

    function generateRefEntityPicker(entries){
    	var res = "";
    	for(var it in entries){
    		//TODO: print displays on whitelist (How to handle other things)
    	
    	var display = false;
    	if((Object.keys(whitelist_entity).length > 0 && whitelist_entity.hasOwnProperty(entity) && whitelist_entity[entity].includes(it))){
    	//check for whitelist
    	display = true;
    	}else if((Object.keys(blacklist_entity).length > 0 && blacklist_entity.hasOwnProperty(entity) && blacklist_entity[entity].includes(it))){
    		//check for blacklist
    		display = false;
    	//check for blacklist
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
    }

    function generateFilter(type, id, content){
    	var res = $('<p class="filterentry" />' , { id : type+""+id });
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateFilterTypes());
    	$(res).append(generateFilterCondition());
    	$(res).append(generateFilterConnector());
    	$(res).append(generateDeleteButton());
    	return res;
    }

    function generateAndFillOrderBy(type, id, content, value1, value2, value3, value4){
    	var elem = generateFilter(type, id, content);
		$(elem).find("select.prop").val(value1);
		$(elem).find("select.type").val(value2);
		$(elem).find("select.condition").val(value3);
		$(elem).find("select.connector").val(value4);
    }
    
    function generateOrderBy(type, id, content){
    	var res = $('<p class="orderbyentry" />', { id : type+""+id});
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateOrderByTypes());
    	$(res).append(generateDeleteButton());
    	return res;
    }
    
    function generateAndFillOrderBy(type, id, content, value1, value2){
    	var elem = generateOrderBy(type, id, content);
		$(elem).find("select.prop").val(value1);
		if(value2 != null){ //sorting is optional
		$(elem).find("select.type").val(value2);
		}
    }

    function generateSelect(type, id, content){
    	var res = $('<p class="selectentry" />', { id : type+""+id});
    	$(res).append(generateDropdownListWithEntry(content));
    	$(res).append(generateDeleteButton());
    	return res;
    }
    function generateAndFillSelect(type, id, content, value){
    	var elem = generateSelect(type, id, content);
    		$(elem).find("select.prop").val(value);
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
    //Generate options
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
    			
    		//check for blacklist
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
    }

    this.registerEvents = function(){
    
    $(divelem).on("click", ".delete", function(){
    	console.log("click");
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
    			console.log("last diabled");
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
    	
    });

    $("#entityid").on("change", function(){
    	//if orderby -> toggle add button
    	$("#idcheck-result").html("");
    });

    $("#refentityid").on("change", function(){
    	//if orderby -> toggle add button
    	$("#idcheck-result").html("");
    });

    $("#entity").on("change", function(){
    	//if orderby -> toggle add button
    	selectEntity($("#entity").val());
    });    
    
    $("#generatequery").on("click", function(){
    	//Parsing the Content can also be done by other libraries
    	console.log("generating query");
    	generateQuery()
        });

    $("#idcheck > button").on("click", function(){
    	//check function -> not possible,  entityset1 researcher not in schema, just Publications
		idcheck();
    	});
    
    } //end registerEvents
    
    function addFilter(){
    	$("#filter").append(generateFilter("filter", $("#filter").find(".filterentry").length+1, prop_filter));
    //add new Filter
    	
    	//disable last entry
    	$("#filter").find(".filterentry > .connector").removeAttr("disabled").last().attr("disabled", "true");	
    }


    function fetchContainerSetForEntity (ent){
    	//Get Set Representation for the given entity
    	//schema -> Container -> EntityContainer -> Type -> namespace.
    	console.log(ent);
    	if(schema["Container"] && schema["Container"]["\$Kind"] == "EntityContainer"){
    		for(var it in schema["Container"]){
    			if(schema["Container"][it]["\$Type"] == namespace+"."+ent){
    				return it;
    			}
    		}
    	}
    }

    function parseFilterToString(val, remain){
    	//input jquery val 
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
    };
function generateQuery(){
	
	//base Url
	var result = returnUrl + serviceUrl;
	if(functionname != ""){
	var params = [];
		//Assume order of printing of parameters is correct
		$.each($("."+functionname).find(".functionparam"), function(i, val){
			params.push($(val).attr("name")+"=\'"+$(val).find("input").val()+"\'");
			});
		result = result + functionname + "(" + params.join(",") + ")";
	}else{
		//Get Single Property
		console.log("Single Entity");
		
		if(!$("#refentitycheck").is(":checked")){
			var ent_id = $("#entityid").val();
			if(ent_id != ""){ //Not fetching set
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
	alert(result);
	$("#fullurl").val(result);

}

function idcheck(){
	var result = returnUrl + serviceUrl;
	
	//check single entity
	if(!$("#refentitycheck").is(":checked")){
		result = result + fetchContainerSetForEntity(entity) + "("+$("#refentityid").val()+")";	
	}else{
	//Get NavigationPathProperty
		result = result + $("#refentity > option:selected").val() + "("+$("#refentityid").val()+")";
	//no need to fetch reference, already written as set.
	}				
	
	   var idcheck = $.get(result);
	   idcheck.done(function(data, status){
		   console.log(data);
		   console.log(status);
		   if (status == 'success'){
	    	$("#idcheck-result").html("").append(generateDataFromResult(data));
	    	$("#idcheck-result").css("border", "3px solid green");
	    }else{
	    	$("#idcheck-result").html("Nicht gefunden").css("border", "3px solid red");	
	    }
	   }).catch(function(err){
	    	$("#idcheck-result").html("Nicht gefunden").css("border", "3px solid red");	
		   console.log(err);
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
	
}
   
    
    /*
     * Pass options when class instantiated
     */
    this.construct(options);
}

