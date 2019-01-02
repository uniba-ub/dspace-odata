package entitys;

import java.util.Arrays;
import java.util.HashMap;
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
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"008orgunits\n|||\nOrganizations###orgunits\"";
	public final static String ID_CONVERTER_TYP= "ou";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;



	public Orgunit(){
		
	CsdlProperty id = new CsdlProperty().setName("id")
			.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
	CsdlProperty idmKey = new CsdlProperty().setName("cris-id")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty name = new CsdlProperty().setName("crisou.name")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty description = new CsdlProperty().setName("Beschreibung")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty url = new CsdlProperty().setName("crisou.iso-3166-country")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty startDate = new CsdlProperty().setName("startdate")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty endDate = new CsdlProperty().setName("crisou.city")
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
		return RECOURCE_TYPE_FILTER;
	}


	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}


	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		return null;
	}


	public HashMap<String, String> getMapping() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
