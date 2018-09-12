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


		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PROJECT_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, title,  startDate, endDate,keywords,status));
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
