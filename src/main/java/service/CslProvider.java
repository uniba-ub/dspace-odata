package service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;

public class CslProvider implements ItemDataProvider{

	List<CSLItemData> itemdataList;

	public CslProvider(List<CSLItemData> itemdata) {
		itemdataList = itemdata;
	}

	public Collection<String> getIds() {
		return itemdataList.stream().map(CSLItemData::getId).collect(Collectors.toList());
	}

	public CSLItemData retrieveItem(String id) {
		for (CSLItemData item: itemdataList) {
			if (item.getId().equals(id)) {
				return item;
			} 
		}
		return null;
	}
	
}
