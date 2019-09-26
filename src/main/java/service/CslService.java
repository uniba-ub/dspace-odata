package service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
		String[] entries = citeproc.makeBibliography().getEntries();
		String[] idscitation =citeproc.makeBibliography().getEntryIds();
		int counter = 0;
		Collections.sort(collection.getEntities(), new Comparator<Entity>() {			
			@Override
			public int compare(Entity o1, Entity o2) {
				int i = Arrays.asList(idscitation).indexOf(o1.getProperty("id").getValue().toString());
				int j = Arrays.asList(idscitation).indexOf(o2.getProperty("id").getValue().toString());

				return i-j;
			}
		});		
		for(Entity entity: collection.getEntities()) {
		       	Property property = new Property(null, "csl", ValueType.PRIMITIVE, replaceEscapedHTML(entries[counter]));
				entity.addProperty(property);
		       	counter ++;
		           }
		return collection;
	}
	
	private CSLItemData buildCslItem(Entity entity) {
		CSLItemDataBuilder builder = new CSLItemDataBuilder()
				.id((String) entity.getProperty("id").getValue().toString())
				.type((getType((String)checkValueNull(entity.getProperty("type")))))
				.title(enhanceTitleWithUrl(entity))
				.author(authorNameSpliter((String) checkValueNull(entity.getProperty("author"))))		
				.language((String) checkValueNull(entity.getProperty("language")))
				.publisher((String)checkValueNull(entity.getProperty("publisher")))
				.publisherPlace((String) checkValueNull(entity.getProperty("publisherplace")))
				.containerTitle((String) checkValueNull(entity.getProperty("journal")))
				.ISSN((String) checkValueNull(entity.getProperty("issn")))
				.page((String) checkValueNull(entity.getProperty("pages")))
				.keyword((String) checkValueNull(entity.getProperty("gndsw")))
				.abstrct((String) checkValueNull(entity.getProperty("description")))
//				.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("corporation"))))
				.containerAuthor(authorNameSpliter((String) checkValueNull(entity.getProperty("articlecollection"))))
				.edition((String) checkValueNull(entity.getProperty("edition")))
				.ISBN((String) checkValueNull(entity.getProperty("isbn")))
				.issue((String) checkValueNull(entity.getProperty("issue")))				
				.collectionNumber((String) checkValueNull(entity.getProperty("seriesnumber")))		
				.containerTitle((String) checkValueNull(entity.getProperty("articlecollectionTitle")));
			
			if(entity.getProperty("ispartofseries")!=null) {
				builder.collectionTitle((String) checkValueNull(entity.getProperty("ispartofseries")));
			} else {
				builder.containerTitle((String) checkValueNull(entity.getProperty("ispartofotherseries")));
			}
		
		
			if((String)checkValueNull(entity.getProperty("completedyear"))!=null) {
				builder.issued((Integer.valueOf((String)entity.getProperty("completedyear").getValue())));
			}
			if((String)checkValueNull(entity.getProperty("volume"))!=null) {
				builder.volume((String)entity.getProperty("volume").getValue());	
			} else if((String)checkValueNull(entity.getProperty("multipartTitel"))!=null) {
				builder.volume((String)entity.getProperty("multipartTitel").getValue());
			}
			
			//TODO: Container Author und Editor (wie kommen die Namen an, müssen diese gesplittet werden?)
			
		return builder.build();
	}	
	
	
	private CSLType getType(String type) {
		if(type==null) {
			return null;
		}	
		switch(type) {
		case "article":
			return CSLType.ARTICLE_JOURNAL;
		case "articlecollection":
			return CSLType.ARTICLE;			
		case "workingpaper":
		case "report":
			return CSLType.REPORT;
		case "masterthesis":
		case "doctoralthesis":
		case "habilitation":
			return CSLType.THESIS;
		case "movingimage":
			return CSLType.MOTION_PICTURE;
		case "book":
		case "periodicalpart":
			return CSLType.BOOK;
		case "conferenceobject":
			return CSLType.PAPER_CONFERENCE;
		case "preprint":
			return CSLType.MANUSCRIPT;
		case "review":
			return CSLType.REVIEW;
		case "other":
			return CSLType.ARTICLE;
		case "bookpart":
			return CSLType.CHAPTER;
		case "sound":
			return CSLType.SONG;
		}
		return null;
	}
	
	private void createCiteproc(String style) throws IOException {
		citeproc = new CSL(provider, style);
		citeproc.setOutputFormat("html");	
		citeproc.registerCitationItems(ids.stream().map(x->x).toArray(String[]::new));
		System.out.println(ids.stream().map(x->x).toArray(String[]::new)[1]);
	}
	
	private CSLName[] authorNameSpliter(String authorfield) {
		if(authorfield==null) {
			return null;
		}
		CSLNameBuilder builder;
		String authors[] = authorfield.split("; ");
		List<CSLName> resultList = new LinkedList<CSLName>();
		for(int i=0; i<authors.length; i++) {
			builder = new CSLNameBuilder();
			String splitauthorname[] = authors[i].split(", ");
			builder.given(splitauthorname[1]);
			builder.family(splitauthorname[0]);
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
	
	private String enhanceTitleWithUrl(Entity entity) {
		String title = (String) checkValueNull(entity.getProperty("title"))+ "</a>";
		String url = "<a href="+(String) checkValueNull(entity.getProperty("uri"))+ ">";	
		String result = url + title;
		return result;
	}
	
	private String replaceEscapedHTML(String html) {
		if(html==null) {
			return html;
		}else {
			html = html.replace("\n", "");
			html = html.replace("&#60;", "<");
			html= html.replace("&#62;", ">");
			html = html.replace("\"", "'");
			html = html.replace("&#38;", "&");
		}
		return html;
	}
}
