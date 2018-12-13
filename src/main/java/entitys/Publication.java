package entitys;

import java.util.Arrays;
import java.util.Collections;

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
	
	
	public Publication(){
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("dc.title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty alternativeTitle = new CsdlProperty().setName("dc.title.alternative")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty contributorEditor = new CsdlProperty().setName("dc.contributor.editor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("dc.description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty  language= new CsdlProperty().setName("dc.language.iso.en_US")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisher = new CsdlProperty().setName("dc.publisher")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty series = new CsdlProperty().setName("dc.relation.ispartofseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty subject = new CsdlProperty().setName("dc.subject")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contributor = new CsdlProperty().setName("dc.contributor.contributor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contributorSupervisor = new CsdlProperty().setName("dc.contributor.supervisor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contributorCorporation = new CsdlProperty().setName("dc.contributor.corporation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publisherPlace = new CsdlProperty().setName("dc.publisher.place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty relationEdition = new CsdlProperty().setName("dc.relation.edition")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty relationSeriesnumber = new CsdlProperty().setName("dc.relation.seriesnumber")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty risPartOfOtherSeries = new CsdlProperty().setName("dc.relation.ispartofotherseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issued= new CsdlProperty().setName("dateIssued.year")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty faculty= new CsdlProperty().setName("ubg.faculty.org")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uriIdentifier= new CsdlProperty().setName("dc.identifier.uri")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty authors= new CsdlProperty().setName("dc.contributor.author")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		// creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");


		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_PUBLICATION_NAME);
		entityType.setProperties(Arrays.asList(id, handle, title, alternativeTitle, contributorEditor, description, description, language, publisher, series, subject,contributor,contributorSupervisor,contributorCorporation,publisherPlace,relationEdition,relationSeriesnumber,risPartOfOtherSeries,issued,faculty,uriIdentifier,authors));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PUBLICATIONS_NAME);
		entitySet.setType(ET_PUBLICATION_FQN);
		
		
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

}
