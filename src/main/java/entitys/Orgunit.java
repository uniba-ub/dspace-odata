package entitys;

import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Orgunit implements EntityModel{
	
	public final static String NAMESPACE = "dspace";

	public static final String ET_ORGUNIT_NAME = "Orgunit";
	public static final FullQualifiedName ET_ORGUNIT_FQN = new FullQualifiedName(NAMESPACE, ET_ORGUNIT_NAME);
	public static final String ES_ORGUNITS_NAME = "Orgunits";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;



	public Orgunit(){
		
	CsdlProperty id = new CsdlProperty().setName("id")
			.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
	CsdlProperty idmKey = new CsdlProperty().setName("idmKey")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty name = new CsdlProperty().setName("Name")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty description = new CsdlProperty().setName("Beschreibung")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty url = new CsdlProperty().setName("Url")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty startDate = new CsdlProperty().setName("Startzeitpunkt")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty endDate = new CsdlProperty().setName("Endzeitpunkt")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

	CsdlPropertyRef propertyRef = new CsdlPropertyRef();
	propertyRef.setName("id");

	entityType = new CsdlEntityType();
	entityType.setName(ET_ORGUNIT_NAME);
	entityType.setProperties(Arrays.asList(id, idmKey, name, description, url, startDate, endDate));
	entityType.setKey(Arrays.asList(propertyRef));
	
	entitySet = new CsdlEntitySet();
	entitySet.setName(ES_ORGUNITS_NAME);
	entitySet.setType(ET_ORGUNIT_FQN);
	
	
	}


	public CsdlEntityType getEntityType() {
		return entityType;
	}



	public FullQualifiedName getFullQualifiedName() {
		return ET_ORGUNIT_FQN;
	}



	public String getEntitySetName() {
		return ES_ORGUNITS_NAME;
	}



	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}


	public String getRecourceTypeFilter() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getIDConverterTyp() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getNavigationFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
