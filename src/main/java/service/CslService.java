package service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import de.undercouch.citeproc.csl.CSLName;
import de.undercouch.citeproc.csl.CSLNameBuilder;
import de.undercouch.citeproc.csl.CSLType;

public class CslService {
	
	private CSL citeproc;
	private List<CSLItemData> itemdatalist;
	private CslProvider provider;
	private List<String> ids;

	
	public EntityCollection enhanceCollection(EntityCollection collection, String style) throws IOException {
		itemdatalist = new LinkedList<CSLItemData>();
		ids = new LinkedList<String>();
		for(Entity entity: collection.getEntities()) {
			itemdatalist.add(buildCslItem(entity));
			ids.add((String) entity.getProperty("id").getValue().toString());
		}
		provider = new CslProvider(itemdatalist);	
		createCiteproc(style);
		int counter = 0;
		for(Entity entity: collection.getEntities()) {
			Property property = new Property(null, "csl", ValueType.PRIMITIVE, citeproc.makeBibliography().getEntries()[counter]);
			entity.addProperty(property);
			counter++;
		}
		return collection;
	}
	
	private CSLItemData buildCslItem(Entity entity) {
		CSLItemDataBuilder builder = new CSLItemDataBuilder()
				.id((String) entity.getProperty("id").getValue().toString())
				.type((getType((String)checkValueNull(entity.getProperty("type")))))
				.title((String) checkValueNull(entity.getProperty("title")))
				.author(authorNameSpliter((String) checkValueNull(entity.getProperty("author"))))		
				.language((String) checkValueNull(entity.getProperty("language")))
				.publisher((String)checkValueNull(entity.getProperty("publisher")))
				.publisherPlace((String) checkValueNull(entity.getProperty("publisherplace")));

			if((String)checkValueNull(entity.getProperty("completedyear"))!=null) {
				builder.issued(Integer.valueOf((String)entity.getProperty("completedyear").getValue()));
			}
		return builder.build();
	}	
	
	
	private CSLType getType(String type) {
		if(type==null) {
			return null;
		}
		if(type.equals("workingpaper")) {
			return CSLType.REPORT;
		} 
		else if(type.equals("dissertation")||type.equals("masterthesis")||type.equals("habilitation")) {
			return CSLType.THESIS;
		
		}
		else if(type.equals("periodicalpart")) {
			return CSLType.ARTICLE_JOURNAL;
		}
		else {
			return CSLType.fromString(type);
		}	
	}
	
	private void createCiteproc(String style) throws IOException {
		citeproc = new CSL(provider, style);
		citeproc.setOutputFormat("html");	
		citeproc.registerCitationItems(ids.stream().map(x->x).toArray(String[]::new));
	}
	
	private CSLName[] authorNameSpliter(String authorfield) {
		if(authorfield==null) {
			return null;
		}
		CSLNameBuilder builder;
		String authors[] = authorfield.split(";");
		List<CSLName> resultList = new LinkedList<CSLName>();
		for(int i=0; i<authors.length; i++) {
			builder = new CSLNameBuilder();
			String splitauthorname[] = authors[i].split(",");
			builder.given(splitauthorname[0]);
			builder.family(splitauthorname[1]);
			resultList.add(builder.build());
		}
		CSLName[] resultArray =  resultList.stream().map(x->x).toArray(CSLName[]::new);
		return resultArray;
	}
	
	private Object checkValueNull(Property property) {
		if(property==null) {
			return null;
		} else {
			return property.getValue();
		}
	}
}
