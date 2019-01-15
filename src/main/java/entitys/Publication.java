package entitys;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"001publications\n|||\nPublications###publications\"";
	public final static String ID_CONVERTER_TYP= "http://hdl.handle.net/123456789/";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	
	public Publication(){
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty  language= new CsdlProperty().setName("language")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisher = new CsdlProperty().setName("publisher")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty series = new CsdlProperty().setName("ispartofseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty subject = new CsdlProperty().setName("subject")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisherPlace = new CsdlProperty().setName("publisherplace")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issued= new CsdlProperty().setName("completedyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty faculty= new CsdlProperty().setName("faculty")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uriIdentifier= new CsdlProperty().setName("uri")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty authors= new CsdlProperty().setName("author")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty fulltext= new CsdlProperty().setName("fulltext")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		// creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");


		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_PUBLICATION_NAME);
		entityType.setProperties(Arrays.asList(id, handle, title, description, type, language, publisher, series, fulltext,subject,publisherPlace,issued,faculty,uriIdentifier,authors));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PUBLICATIONS_NAME);
		entitySet.setType(ET_PUBLICATION_FQN);
		
		mapping = new HashMap<String, String>();
		
		mapping.put("handle", "handle");
		mapping.put("title", "dc.title");
		mapping.put("description", "dc.description");
		mapping.put("publisher", "dc.publisher");
		mapping.put("subject", "dc.subject");
		mapping.put("author", "dc.contributor.author");
		mapping.put("uri", "dc.identifier.uri");
		mapping.put("completedyear", "ubg.date.completedyear");
		mapping.put("language", "dc.language.iso");
		mapping.put("ispartofseries", "dc.relation.ispartofseries");
		mapping.put("publisherplace", "dc.publisher.place");
		mapping.put("faculty","ubg.faculty.org");
		mapping.put("type", "dc.type");
		mapping.put("fulltext", "infofulltext");

		
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

	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}

	public String getNavigationFilter(String sourceType, String id) {
		String navigationFilter = "";
		if(sourceType.equals("Researchers")) {
			navigationFilter = ("dc.contributor.author_authority:\"");
			navigationFilter = (navigationFilter+id+"\"");

		} else if(sourceType.equals("Orgunits")) {
			navigationFilter = ("ubg.faculty.org_authority:\"");
			navigationFilter = (navigationFilter+id+"\"");
			
		
		}
			return navigationFilter;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
