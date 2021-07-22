package service;


public class IdConverter {

	
	public String getIDSolrFilter (String converterTyp) {
		if(converterTyp.equals("uniba/")) {
			return "handle";
		}else {
			return "cris-id";		}	
	}
	
	
	public int convertCrisToId(String crisId) {
		String convertedId = crisId.replaceAll("[^\\d.]", "");
		convertedId = convertedId.replaceFirst("^0+(?!$)", "");
		int resultId = Integer.valueOf(convertedId);

		return resultId;
	}	
	
	public int convertHandleToId(String handle) {
		String convertedId = handle.replaceAll(".*/", "");
		int resultId = Integer.valueOf(convertedId);
		return resultId;
		
	}
	
	
	public String convertODataIDToDSpaceID(String id, String type) {
		if(type.equals("http://hdl.handle.net/123456789/")) {
			type = type.replaceAll("[^0-9]", "");
			type= type + "/";
		}
		int dspaceIdAsInt = Integer.valueOf(id);
			if(type.equals("uniba/")) {
				String dspaceId = String.format("%s%d", type,dspaceIdAsInt);
				return dspaceId;
			}
		String dspaceId = String.format("%s%05d", type,dspaceIdAsInt);
		return dspaceId;
	}
	
}


