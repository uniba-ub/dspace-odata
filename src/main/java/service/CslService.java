package service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.RegExUtils;
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
		itemdatalist = new LinkedList<>();
		ids = new LinkedList<>();
		for (Entity entity: collection.getEntities()) {
			itemdatalist.add(buildCslItem(entity));
			ids.add(entity.getProperty("id").getValue().toString());
		}
		provider = new CslProvider(itemdatalist);
		createCiteproc(style);
		String[] entries = citeproc.makeBibliography().getEntries();
		int counter = 0;
		for (Entity entity: collection.getEntities()) {
			Property property = new Property(null, "csl", ValueType.PRIMITIVE, replaceEscapedHTML(entries[counter]));
			entity.addProperty(property);
			counter ++;
		   }
		return collection;
	}
	
	public EntityCollection enhanceProductCollection(EntityCollection collection, String style) throws IOException {
		itemdatalist = new LinkedList<>();
		ids = new LinkedList<>();
		for (Entity entity: collection.getEntities()) {
			itemdatalist.add(buildProductCslItem(entity));
			ids.add(entity.getProperty("id").getValue().toString());
		}
		provider = new CslProvider(itemdatalist);
		createCiteproc(style);
		String[] entries = citeproc.makeBibliography().getEntries();
		int counter = 0;
		for (Entity entity: collection.getEntities()) {
			Property property = new Property(null, "csl", ValueType.PRIMITIVE, replaceEscapedHTML(entries[counter]));
			entity.addProperty(property);
			counter ++;
	   }
		return collection;
	}
	
	private CSLItemData buildCslItem(Entity entity) {
		CSLItemDataBuilder builder = new CSLItemDataBuilder()
				.id(entity.getProperty("id").getValue().toString())
				.type((getPublicationType((String)checkValueNull(entity.getProperty("type")))))
				.title(enhanceTitleWithHandle(entity))
				.author(authorNameSpliter((String) checkValueNull(entity.getProperty("author"))))		
				.language((String) checkValueNull(entity.getProperty("language")))
				.publisher((String)checkValueNull(entity.getProperty("publisher")))
				.publisherPlace((String) checkValueNull(entity.getProperty("publisherplace")))
				.ISSN((String) checkValueNull(entity.getProperty("issn")))
				.page((String) checkValueNull(entity.getProperty("pages")))
				.numberOfPages((String) checkValueNull(entity.getProperty("numpages")))
				.medium((String) checkValueNull(entity.getProperty("medium")))
				.keyword((String) checkValueNull(entity.getProperty("gndsw")))
				.abstrct((String) checkValueNull(entity.getProperty("description")))
				.containerAuthor(authorNameSpliter((String) checkValueNull(entity.getProperty("articlecollection"))))
				.edition((String) checkValueNull(entity.getProperty("edition")))
				.ISBN((String) checkValueNull(entity.getProperty("isbn")))
				.issue((String) checkValueNull(entity.getProperty("issue")))				
				.collectionNumber((String) checkValueNull(entity.getProperty("seriesnumber")));
		
		if (checkValueNull(entity.getProperty("journal")) != null) {
			builder.containerTitle(journalTitleSplitter((String) checkValueNull(entity.getProperty("journal"))));
		} else if (checkValueNull(entity.getProperty("articlecollectionTitle")) != null) {
			builder.containerTitle(replaceWhitespaceColon((String) checkValueNull(entity.getProperty("articlecollectionTitle"))));
		}
		
			if (entity.getProperty("ispartofseries") != null) {
				builder.collectionTitle((String) checkValueNull(entity.getProperty("ispartofseries")));
			} else if (entity.getProperty("ispartofotherseries") != null) {
				builder.collectionTitle((String) checkValueNull(entity.getProperty("ispartofotherseries")));
			}
		
		
			if (checkValueNull(entity.getProperty("completedyear")) != null) {
				builder.issued((Integer.parseInt((String)entity.getProperty("completedyear").getValue())));
			}
			if (checkValueNull(entity.getProperty("volume")) != null) {
				builder.volume((String)entity.getProperty("volume").getValue());	
			} else if (checkValueNull(entity.getProperty("multipartTitel")) != null) {
				builder.volume((String)entity.getProperty("multipartTitel").getValue());
			}
			//
			if (checkValueNull(entity.getProperty("type")) != null &&
				entity.getProperty("type").getValue().toString().equals("book") &&
				checkValueNull(entity.getProperty("author")) == null) {
				if (checkValueNull(entity.getProperty("editor")) != null) {
					builder.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("editor"))));
				} else if (checkValueNull(entity.getProperty("corporation")) != null) {
					builder.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("corporation"))));
				}
			} else if (checkValueNull(entity.getProperty("type")) != null &&
				entity.getProperty("type").getValue().toString().equals("bookpart")) {
				if (checkValueNull(entity.getProperty("articlecollectionEditor")) != null) {
					builder.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("articlecollectionEditor"))));
				} else if (checkValueNull(entity.getProperty("corporation")) != null) {
					builder.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("corporation"))));
				}
			} else if (checkValueNull(entity.getProperty("type")) != null &&
				entity.getProperty("type").getValue().toString().equals("periodicalpart") &&
				checkValueNull(entity.getProperty("author")) == null) {
				if (checkValueNull(entity.getProperty("editor")) != null) {
					builder.editor(authorNameSpliter((String) checkValueNull(entity.getProperty("editor"))));
				}
			}
			
			//Use doi (registered by us) preferred or external doi's
			if (entity.getProperty("doiour") != null) {
				builder.DOI((String) checkValueNull(entity.getProperty("doiour")));
			} else if (entity.getProperty("doi") != null) {
				builder.DOI((String) checkValueNull(entity.getProperty("doi")));
			}
			
		return builder.build();
	}	
	
	private CSLItemData buildProductCslItem(Entity entity) {
		CSLItemDataBuilder builder = new CSLItemDataBuilder()
				.id(entity.getProperty("id").getValue().toString())
				.type((getProductType((String)checkValueNull(entity.getProperty("type")))))
				.title(enhanceTitleWithHandle(entity))
				.language((String) checkValueNull(entity.getProperty("language")))
				.publisher((String)checkValueNull(entity.getProperty("publisher")))
				.numberOfPages((String) checkValueNull(entity.getProperty("extent")))
				.medium((String) checkValueNull(entity.getProperty("format")))
				.version((String) checkValueNull(entity.getProperty("version")));
			
			try {
				//Not sure if we can expect integer value
				if (checkValueNull(entity.getProperty("completedyear")) != null) {
					builder.issued((Integer.parseInt((String)entity.getProperty("completedyear").getValue())));
				}
			} catch(NumberFormatException e) {
				//ignore
			}

			//Map author and contributor rules
			if (checkValueNull(entity.getProperty("creator")) != null) {
				builder.author(authorNameSpliter((String) checkValueNull(entity.getProperty("creator"))));
			} else if (checkValueNull(entity.getProperty("corporation")) != null) {
				builder.author(authorNameSpliter((String) checkValueNull(entity.getProperty("corporation"))));
			} else if (checkValueNull(entity.getProperty("contributor")) != null) {
				builder.author(authorNameSpliter((String) checkValueNull(entity.getProperty("contributor"))));
			}
			
			//Use doi (registered by us) preferred or external doi's
			if (entity.getProperty("doiour") != null) {
				builder.DOI((String) checkValueNull(entity.getProperty("doiour")));
			} else if (entity.getProperty("doi") != null) {
				builder.DOI((String) checkValueNull(entity.getProperty("doi")));
			}
						
		return builder.build();
	}	
	
	
	private CSLType getPublicationType(String type) {
		if (type == null) {
			return null;
		}	
		switch (type) {
			case "article":
				return CSLType.ARTICLE_JOURNAL;
			case "articlecollection":
			case "other":
				return CSLType.ARTICLE;
			case "workingpaper":
			case "report":
				return CSLType.REPORT;
			case "masterthesis":
			case "doctoralthesis":
			case "habilitation":
				return CSLType.THESIS;
			case "book":
			case "periodicalpart":
				return CSLType.BOOK;
			case "conferenceobject":
				return CSLType.PAPER_CONFERENCE;
			case "preprint":
				return CSLType.MANUSCRIPT;
			case "review":
				return CSLType.REVIEW;
			case "bookpart":
				return CSLType.CHAPTER;
		}
		return null;
	}
	
	private CSLType getProductType(String type) {
		if (type == null) {
			return null;
		}	
		switch (type) {
			case "image":
				return CSLType.GRAPHIC;
			case "dataset":
				return CSLType.DATASET;
			case "video":
			case "movingimage":
				return CSLType.MOTION_PICTURE;
			}
		//return Dataset as default Type
		return CSLType.DATASET;
	}
	
	private void createCiteproc(String style) throws IOException {
		citeproc = new CSL(provider, style);
		citeproc.setOutputFormat("html");	
		citeproc.registerCitationItems(ids.toArray(String[]::new),true);
	}
	
	private CSLName[] authorNameSpliter(String authorfield) {
		if (authorfield == null) {
			return null;
		}
		CSLNameBuilder builder;
		String[] authors = authorfield.split("; ");
		List<CSLName> resultList = new LinkedList<>();
		for (String author : authors) {
			builder = new CSLNameBuilder();
			String[] splitauthorname = author.split(", ");
			if (splitauthorname.length > 1) {
				builder.given(splitauthorname[1]);
				builder.family(splitauthorname[0]);
			} else {
				builder.given(splitauthorname[0]);
			}
			resultList.add(builder.build());
		}
		return resultList.toArray(CSLName[]::new);
	}
	
	private String replaceWhitespaceColon(String val) {
		/* Replace Whitespace before first colon */
		try {
			val = RegExUtils.replaceAll(val, " : ", ": ");
			val = RegExUtils.replaceAll(val, " ; ", "; ");
		} catch(Exception e) {
			//
		}
		return val;
	}
	
	private String journalTitleSplitter(String titleField){
		//split journal/articlecollection title by first colon, if any and take the part on the left
		try {
			int poscolon = titleField.indexOf(":");
			if (poscolon < 1) return titleField;
			return (titleField.substring(0, (poscolon))).trim();		
		} catch(Exception e) {
			//return default value
		}
		return titleField;
	}

	private Object checkValueNull(Property property) {
		if (Objects.isNull(property)) {
			return null;
		} else {
			return property.getValue();
		}
	}

	private String enhanceTitleWithHandle(Entity entity) {
		String title = replaceWhitespaceColon((String) checkValueNull(entity.getProperty("title")))+ "</a>";
		String url = "<a href=\"https://fis.uni-bamberg.de/handle/"+ checkValueNull(entity.getProperty("handle")) + "\">";
		return url + title;
	}
	
	private String replaceEscapedHTML(String html) {
		if (html == null) {
			return null;
		} else {
			html = html.replace("\n", "");
			html = html.replace("&#60;", "<");
			html= html.replace("&#62;", ">");
			html = html.replace("\"", "'");
			html = html.replace("&#38;", "&");
		}
		return html;
	}
}
