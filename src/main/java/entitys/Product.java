package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Product implements EntityModel {
	
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_PRODUCT_NAME = "Product";
	public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
	public static final String ES_PRODUCTS_NAME = "Products";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"ResearchData\"";
	private HashMap<String, String> idconverter;
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, List<String>> mapping;
	private ArrayList<String> ENTITYFILTER;
	
	public Product(){
		idconverter = new HashMap<String, String>();
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
		CsdlProperty creator= new CsdlProperty().setName("creator")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty author= new CsdlProperty().setName("author")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contributor= new CsdlProperty().setName("contributor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty corporation= new CsdlProperty().setName("corporation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty csl= new CsdlProperty().setName("csl")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty doi = new CsdlProperty().setName("doi")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ourdoi = new CsdlProperty().setName("ourdoi")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty extent = new CsdlProperty().setName("extent")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty format = new CsdlProperty().setName("format")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty faculty= new CsdlProperty().setName("faculty")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issued= new CsdlProperty().setName("completedyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty  language= new CsdlProperty().setName("language")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisher = new CsdlProperty().setName("publisher")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty uriIdentifier= new CsdlProperty().setName("uri")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty urlIdentifier= new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty version= new CsdlProperty().setName("version")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		CsdlProperty prod2rp= new CsdlProperty().setName("prod2rp")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty prod2pj= new CsdlProperty().setName("prod2pj")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty prod2ou= new CsdlProperty().setName("prod2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty prod2awards= new CsdlProperty().setName("prod2award")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		//creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_PRODUCT_NAME);
		entityType.setProperties(Arrays.asList(id, uuid, entitytype, handle, name, title, creator, contributor, author, description, doi, ourdoi, extent, format, type, language, publisher, issued,faculty,uriIdentifier, urlIdentifier, corporation, version, csl, prod2ou, prod2pj, prod2rp, prod2awards));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PRODUCTS_NAME);
		entitySet.setType(ET_PRODUCT_FQN);
		
		mapping = new HashMap<String, List<String>>();
		
		mapping.put("handle", List.of("handle"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));
		
		mapping.put("creator", List.of("dc.creator"));
		mapping.put("author", List.of("dc.creator"));
		mapping.put("contributor", List.of("contributors")); //ubg.contributor.*
		mapping.put("completedyear", List.of("researchdatacompleted"));
		mapping.put("issued", List.of("dateIssued.year_sort"));
		mapping.put("corporation", List.of("dc.creator.corporation"));
		mapping.put("description", List.of("ubg.description.abstract"));
		mapping.put("extent", List.of("dcterms.extent"));
		mapping.put("format", List.of("dcterms.format"));
		mapping.put("doi", List.of("dc.identifier.doi"));
		mapping.put("ourdoi", List.of("ubg.identifier.doi"));
		mapping.put("faculty",List.of("ubg.researchdata.org"));
		mapping.put("language", List.of("dc.language.iso"));
		mapping.put("publisher", List.of("dc.publisher"));
		mapping.put("type", List.of("dc.type"));
		mapping.put("title", List.of("dc.title"));
		mapping.put("uri", List.of("dc.identifier.uri"));
		mapping.put("url", List.of("dc.identifier.url"));
		mapping.put("version", List.of("ubg.version.description"));
		
		mapping.put("prod2rp", List.of("contributor_authority"));
		mapping.put("prod2pj", List.of("ubg.relation.project_authority"));
		mapping.put("prod2ou", List.of("ubg.researchdata.org_authority"));
		mapping.put("prod2award", List.of("ubg.relation.award_authority"));

		ENTITYFILTER = new ArrayList<String>();

	}

	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_PRODUCT_FQN;
	}

	public String getEntitySetName() {
		return ES_PRODUCTS_NAME;
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
		String navigationFilter = "";
		if(sourceType.equals("Researchers")) {
			navigationFilter = ("contributor_authority:\"");
			navigationFilter = (navigationFilter+id+"\"");
		} else if(sourceType.equals("Orgunits")) {
			navigationFilter = ("ubg.researchdata.org_authority:\"");
			navigationFilter = (navigationFilter+id+"\"");
		} else if(sourceType.equals("Projects")) {
			navigationFilter = ("ubg.relation.project_authority:\"");
			navigationFilter = (navigationFilter+id+"\"");
		}
		
			return navigationFilter;
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
