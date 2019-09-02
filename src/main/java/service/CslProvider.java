package service;

import java.util.List;

import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;

public class CslProvider implements ItemDataProvider{

	List<CSLItemData> itemdataList;
	String[] ids;

	public CslProvider(List<CSLItemData> itemdata) {
		itemdataList = itemdata;
	}
	
	public String[] getIds() {	
		return itemdataList.stream().map(x-> x.getId()).toArray(String[]::new);
	}


	public CSLItemData retrieveItem(String id) {
		for(CSLItemData item: itemdataList) {
			if(item.getId().equals(id)) {
				return item;
			} 
		}
		return null;
	}
	
}
