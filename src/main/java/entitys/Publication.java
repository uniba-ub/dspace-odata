package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Publication implements EntityModel {
	
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_PUBLICATION_NAME = "Publication";
	public static final FullQualifiedName ET_PUBLICATION_FQN = new FullQualifiedName(NAMESPACE, ET_PUBLICATION_NAME);
	public static final String ES_PUBLICATIONS_NAME = "Publications";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Publication\"";

	private final HashMap<String, String> idconverter;

	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;
	
	public Publication(){
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("([0-9]{1,6})", "handle");
		idconverter.put("(uniba/[0-9]{1,6})", "handle");
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty authors= new CsdlProperty().setName("author")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty articlecollectionEditor= new CsdlProperty().setName("articlecollectionEditor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty articlecollectionTitle= new CsdlProperty().setName("articlecollectionTitle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty corporation= new CsdlProperty().setName("corporation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty csl= new CsdlProperty().setName("csl")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty doi = new CsdlProperty().setName("doi")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty doiour = new CsdlProperty().setName("doiour")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty edition= new CsdlProperty().setName("edition")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty faculty= new CsdlProperty().setName("faculty")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty fulltext= new CsdlProperty().setName("fulltext")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gndsw= new CsdlProperty().setName("gndsw")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issued= new CsdlProperty().setName("completedyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty isbn= new CsdlProperty().setName("isbn")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issn= new CsdlProperty().setName("issn")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ispartofotherseries = new CsdlProperty().setName("ispartofotherseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty series = new CsdlProperty().setName("ispartofseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issue= new CsdlProperty().setName("issue")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty journal= new CsdlProperty().setName("journal")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty  language= new CsdlProperty().setName("language")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty multipartTitel= new CsdlProperty().setName("multipartTitel")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisher = new CsdlProperty().setName("publisher")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty pages= new CsdlProperty().setName("pages")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty numpages= new CsdlProperty().setName("numpages")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty medium= new CsdlProperty().setName("medium")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty peerreview= new CsdlProperty().setName("peerreview")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisherPlace = new CsdlProperty().setName("publisherplace")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty seriesnumber = new CsdlProperty().setName("seriesnumber")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty subject = new CsdlProperty().setName("subject")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty thesis= new CsdlProperty().setName("thesis")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty uriIdentifier= new CsdlProperty().setName("uri")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty volume= new CsdlProperty().setName("volume")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty editor= new CsdlProperty().setName("editor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty supervisorname= new CsdlProperty().setName("supervisorname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		CsdlProperty publ2rp= new CsdlProperty().setName("publ2rp")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publ2series= new CsdlProperty().setName("publ2series")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publ2journals= new CsdlProperty().setName("publ2journals")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publ2pj= new CsdlProperty().setName("publ2pj")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publ2ou= new CsdlProperty().setName("publ2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publ2awards= new CsdlProperty().setName("publ2award")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		//creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_PUBLICATION_NAME);
		entityType.setProperties(Arrays.asList(id, uuid, handle, name, entitytype, title, description, doi, doiour, type, language, publisher, series, seriesnumber, volume, articlecollectionEditor, articlecollectionTitle, ispartofotherseries, fulltext,subject,publisherPlace,issued,faculty,uriIdentifier,authors,journal,issn,multipartTitel,issue, pages, numpages, medium, gndsw, corporation, edition, isbn, thesis, peerreview, csl, supervisorname, publ2journals, publ2ou, publ2pj, publ2rp, publ2series, publ2awards, editor));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PUBLICATIONS_NAME);
		entitySet.setType(ET_PUBLICATION_FQN);
		
		mapping = new HashMap<>();
		
		mapping.put("handle", List.of("handle"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("articlecollectionEditor", List.of("ubg.editor.articlecollection"));
		mapping.put("articlecollectionTitle", List.of("ubg.titleparent.articlecollection"));
		mapping.put("author", List.of("dc.contributor.author"));
		mapping.put("completedyear", List.of("dateIssued.year_sort"));
		mapping.put("corporation", List.of("dc.contributor.corporation"));
		mapping.put("description", List.of("dc.description.abstract"));
		mapping.put("doi", List.of("dc.identifier.doi"));
		mapping.put("doiour", List.of("ubg.identifier.doi"));
		mapping.put("edition", List.of("dc.relation.edition"));
		mapping.put("editor", List.of("dc.contributor.editor"));
		mapping.put("supervisorname", List.of("dc.contributor.supervisor"));
		mapping.put("faculty",List.of("ubg.faculty.org"));
		mapping.put("fulltext", List.of("infofulltext"));
		mapping.put("gndsw",List.of("ubg.subject.gndsw"));
		mapping.put("ispartofotherseries", List.of("dc.relation.ispartofotherseries"));
		mapping.put("ispartofseries", List.of("dc.relation.ispartofseries"));
		mapping.put("issn", List.of("dc.identifier.issn"));
		mapping.put("isbn", List.of("dc.identifier.isbn"));
		mapping.put("issue", List.of("ubg.relation.issue"));
		mapping.put("journal", List.of("ubg.titleparent.journal"));
		mapping.put("language", List.of("dc.language.iso"));
		mapping.put("multipartTitel", List.of("ubg.multipartTitel"));
		mapping.put("pages", List.of("ubg.pages.range"));
		mapping.put("numpages", List.of("ubg.pages.count"));
		mapping.put("medium", List.of("ubg.pages.medium"));
		mapping.put("peerreview", List.of("ubg.peerreview"));
		mapping.put("publisher", List.of("dc.publisher"));
		mapping.put("publisherplace", List.of("dc.publisher.place"));
		mapping.put("seriesnumber", List.of("dc.relation.seriesnumber"));
		mapping.put("subject", List.of("dc.subject"));
		mapping.put("thesis", List.of("dc.publisher.thesis"));
		mapping.put("type", List.of("dc.type"));
		mapping.put("title", List.of("dc.title"));
		mapping.put("uri", List.of("dc.identifier.uri"));
		mapping.put("volume", List.of("dc.relation.volume"));
		
		mapping.put("publ2rp", List.of("author_authority", "author_old_authority"));
		mapping.put("publ2series", List.of("dc.relation.ispartofseries_authority"));
		mapping.put("publ2journals", List.of("dc.relation.ispartofseries_authority"));
		mapping.put("publ2pj", List.of("ubg.relation.project_authority"));
		mapping.put("publ2ou", List.of("ubg.faculty.org_authority"));
		mapping.put("publ2award", List.of("ubg.relation.award_authority"));

		ENTITYFILTER = new ArrayList<>();
		//No versions
		ENTITYFILTER.add("-ubg.version.versionof:*");
		
	}

	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_PUBLICATION_FQN;
	}

	public String getEntitySetName() {
		return ES_PUBLICATIONS_NAME;
	}

	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

	public String getRecourceTypeFilter() {
		return RECOURCE_TYPE_FILTER;
	}

	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return null;
	}

	public String getNavigationFilter(String sourceType, String id) {
		return switch (sourceType) {
			case "Researchers" -> ("dc.contributor.author_authority:\"" + id +
				"\" OR dc.contributor.editor_authority:\"" + id + "\"");
			case "Researchers_AUTHOR" -> ("dc.contributor.author_authority:\"" +  id + "\"");
			case "Researchers_SUPERVISOR" -> ("dc.contributor.supervisor_authority:\"" + id + "\"");
			case "Researchers_SELECTED" -> ("relation.isPublicationsSelectedFor:\""  + id + "\"");
				/*See DataHandler Function for selectedPublications where this Key is also defined for sorting*/
			case "Orgunits" -> ("ubg.faculty.org_authority:\"" + id + "\" OR dc.relation.authororgunit_authority:\"" + id +
				"\" OR dc.relation.contributororgunit_authority:\"" + id + "\"");
			case "Series", "Journals" -> ("dc.relation.ispartofseries_authority:\"" + id + "\"");
			case "Projects" -> ("ubg.relation.project_authority:\"" + id + "\"");
			case "Awards" -> ("ubg.relation.award_authority:\"" + id + "\"");
			default -> "";
		};
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
