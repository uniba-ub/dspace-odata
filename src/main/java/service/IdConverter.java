package service;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class IdConverter {
	
	private String handleprefix = "uniba";
	
	
	public String convertODataIDToDSpaceID(String id) {
		//because we've got some string parameter, replace all single quotes
		id = id.replaceAll("'", "");	
		return id;
	}


	public int convertHandleToId(String handle) {
		String convertedId = handle.replaceAll(".*/", "");
		int resultId = Integer.valueOf(convertedId);
		return resultId;

	}


	public String getIdField(String id, HashMap<String, String> idConverter) {
		if(idConverter == null || idConverter.isEmpty()) {
			return "search.resourceid";
		}
		
		for(Entry<String, String> entry : idConverter.entrySet()) {
			try {
				if(Pattern.matches(entry.getKey(), id)) {
					
					return entry.getValue();
				} 
			}catch(Exception e) {
				//
			}
		}
		//no match, return default
		return "search.resourceid";
	}
	
	//add some identifier resolver prefix or similar depending on the search field
	public String addIdentifierPrefix(String id, String type, String legacyPrefix) {
		if(type.contentEquals("handle")) {
			if(!id.startsWith(handleprefix)) {
				return handleprefix+"/"+id;
			}
		}
		if(type.contentEquals("cris.legacyId")) {
			//add legacy prefix
			if(legacyPrefix != null) {
				if(!id.startsWith(legacyPrefix)) {
					//at least 5 digits with leading zeros!
					return String.format("%s%05d", legacyPrefix,Integer.parseInt(id));
				}else {
					//withprefix ->
					String idclean = id.replace(legacyPrefix, "");
					return String.format("%s%05d", legacyPrefix,Integer.parseInt(idclean));
				}
			}
		}
		return id;
	}
	
}


