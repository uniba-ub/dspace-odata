package service;

public class IdConverter {
		
	
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
	
	
	public String convertToCrisID(String id, String type) {
		int crisIdAsInt = Integer.valueOf(id);
		String crisId = String.format("%s%05d", type,crisIdAsInt);
		return crisId;
	}
	
}


