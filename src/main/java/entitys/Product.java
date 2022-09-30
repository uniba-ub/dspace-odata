package entitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
	public final static String ID_CONVERTER_TYP= "uniba/";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	private ArrayList<String> ENTITYFILTER;
	
	public Product(){
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
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
		entityType.setProperties(Arrays.asList(id, handle, title, creator, contributor, author, description, doi, ourdoi, extent, format, type, language, publisher, issued,faculty,uriIdentifier, urlIdentifier, corporation, version, csl, prod2ou, prod2pj, prod2rp, prod2awards));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PRODUCTS_NAME);
		entitySet.setType(ET_PRODUCT_FQN);
		
		mapping = new HashMap<String, String>();
		
		mapping.put("handle", "handle");
		mapping.put("creator", "dc.creator");
		mapping.put("author", "dc.creator");
		mapping.put("contributor", "contributors"); //ubg.contributor.*
		mapping.put("completedyear", "researchdatacompleted"); //using own indexer for this value. uses dc.date.created or dc.date.issued
		mapping.put("issued", "dateIssued.year_sort");
		mapping.put("corporation", "dc.creator.corporation");
		mapping.put("description", "ubg.description.abstract");
		mapping.put("extent", "dcterms.extent");
		mapping.put("format", "dcterms.format");
		mapping.put("doi", "dc.identifier.doi");
		mapping.put("ourdoi", "ubg.identifier.doi");
		mapping.put("faculty","ubg.researchdata.org");
		mapping.put("language", "dc.language.iso");
		mapping.put("publisher", "dc.publisher");
		mapping.put("type", "dc.type");
		mapping.put("title", "dc.title");
		mapping.put("uri", "dc.identifier.uri");
		mapping.put("url", "dc.identifier.url");
		mapping.put("version", "ubg.version.description");
		
		mapping.put("prod2rp", "contributor_authority");
		mapping.put("prod2pj", "ubg.relation.project_authority");
		mapping.put("prod2ou", "ubg.researchdata.org_authority");
		mapping.put("prod2award", "ubg.relation.award_authority");

		ENTITYFILTER = new ArrayList<String>();
		ENTITYFILTER.add("item.isResearchdata:true");
		
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

	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
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

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
