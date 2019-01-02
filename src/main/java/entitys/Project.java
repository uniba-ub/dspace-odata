package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Project implements EntityModel{
	
	public final static String NAMESPACE = "dspace";

	public static final String ET_PROJECT_NAME = "Project";
	public static final FullQualifiedName ET_PROJECT_FQN = new FullQualifiedName(NAMESPACE, ET_PROJECT_NAME);
	public static final String ES_PROJECTS_NAME = "Projects";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"010projects\n" + 
			"|||\n" + 
			"Fundings###fundings\"";
	public final static String ID_CONVERTER_TYP= "pj";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Project() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("crisproject.title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startDate = new CsdlProperty().setName("crisproject.startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty endDate = new CsdlProperty().setName("crisproject.expdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty keywords = new CsdlProperty().setName("crisproject.keywords")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty status = new CsdlProperty().setName("crisproject.status")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty abstracts = new CsdlProperty().setName("crisproject.abstract")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty budget = new CsdlProperty().setName("crisproject.budget")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty url = new CsdlProperty().setName("crisproject.projectURL")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PROJECT_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, title, abstracts, budget, startDate, endDate,keywords,status, url));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PROJECTS_NAME);
		entitySet.setType(ET_PROJECT_FQN);
		
	}
	
	

	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_PROJECT_FQN;
	}

	public String getEntitySetName() {
		return ES_PROJECTS_NAME;
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
			navigationFilter = ("crisproject.principalinvestigator_authority:\""+ id+"\"");
			navigationFilter = (navigationFilter+ "OR ");
			navigationFilter = (navigationFilter+ "crisproject.coinvestigators_authority:\""+ id+"\"");
			
		} else if(sourceType.equals("Orgunits")) {
			navigationFilter = ("TODO");
		
		}
			return navigationFilter;
	}



	public HashMap<String, String> getMapping() {
		// TODO Auto-generated method stub
		return null;
	}

}
