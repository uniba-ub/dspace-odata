package entitys;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Researcher implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_RESEARCHER_NAME = "Researcher";
	public static final FullQualifiedName ET_RESEARCHER_FQN = new FullQualifiedName(NAMESPACE, ET_RESEARCHER_NAME);
	public static final String ES_RESEARCHERS_NAME = "Researchers";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Researcher() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty fullName = new CsdlProperty().setName("Name")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty  creditName= new CsdlProperty().setName("Anzeigename")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researchInterests = new CsdlProperty().setName("Forschungsinteressen")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("Beschreibung")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("Titel")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty email = new CsdlProperty().setName("E-Mail")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty position = new CsdlProperty().setName("Position")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty transferabstract = new CsdlProperty().setName("Transfer Abstract")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty transferkeywords = new CsdlProperty().setName("Transfer Stichwörter")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		// creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");


		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();
		entityType.setName(ET_RESEARCHER_NAME);
		entityType.setProperties(Arrays.asList(id, fullName, creditName, researchInterests, description, title, email, position, transferabstract, transferkeywords));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_RESEARCHERS_NAME);
		entitySet.setType(ET_RESEARCHER_FQN);
		
		
		
	}
	
	public CsdlEntityType getEntityType() {	
		
		return entityType;
	}
	

	public FullQualifiedName getFullQualifiedName() {
		return ET_RESEARCHER_FQN;
	}

	public String getEntitySetName() {
		return ES_RESEARCHERS_NAME;
	}

	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

}
	

