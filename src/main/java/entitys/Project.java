package entitys;

import java.util.Arrays;

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
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Project() {
		
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty acronym = new CsdlProperty().setName("Kürzel")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("Titel")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("Beschreibung")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startDate = new CsdlProperty().setName("Startzeitpunkt")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty durationInMonths = new CsdlProperty().setName("Dauer in Monaten")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty endDate = new CsdlProperty().setName("Endzeitpunkt")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty budget = new CsdlProperty().setName("Budget")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty keywords = new CsdlProperty().setName("Stichwörter")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("Url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty status = new CsdlProperty().setName("Status")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty specialProject = new CsdlProperty().setName("Sonderprojekt")
				.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
		CsdlProperty visibility = new CsdlProperty().setName("Sichtbarkeit")
				.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());


		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PROJECT_NAME);
		entityType.setProperties(Arrays.asList(id, acronym, title, description, startDate,durationInMonths, endDate, budget,keywords,url,status,specialProject, visibility));
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

}
