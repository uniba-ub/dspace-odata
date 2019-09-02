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
		return new CSLItemDataBuilder()
				.id((String) entity.getProperty("id").getValue().toString())
				.type((getType((String)entity.getProperty("type").getValue())))
				.title((String) entity.getProperty("title").getValue())
				.author(authorNameSpliter((String) entity.getProperty("author").getValue()))
			
				.issued(Integer.valueOf((String) entity.getProperty("completedyear").getValue()))
				.build();
			
	}	
	
	
	private CSLType getType(String type) {
		if(type.equals("workingpaper")) {
			return CSLType.REPORT;
		} else if(type.equals("dissertation")||type.equals("masterthesis")||type.equals("habilitation")) {
			return CSLType.THESIS;
		} else {
			return CSLType.fromString(type);
		}	
	}
	
	private void createCiteproc(String style) throws IOException {
		citeproc = new CSL(provider, style);
		citeproc.setOutputFormat("html");	
		citeproc.registerCitationItems(ids.stream().map(x->x).toArray(String[]::new));
	}
	
	private CSLName[] authorNameSpliter(String author) {
		//TODO: split authors with ;
		//TODO: split author name with , into given and family name
		//TODO: Build CSLName (List), then stream list into CSLName Array
		
		
		
		CSLNameBuilder builder = new CSLNameBuilder();
		builder.given("Jonathan");
		builder.family("Boss");
		CSLName name = builder.build();
		CSLName name2 = builder.build();

		CSLName[] namearray = {name,name2};
		return namearray;
		
	}
}
