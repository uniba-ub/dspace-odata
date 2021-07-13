package entitys;

import java.util.Arrays;
import java.util.HashMap;

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
	private HashMap<String, String> mapping;

	public Orgunit(){
		
	CsdlProperty id = new CsdlProperty().setName("id")
			.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
	CsdlProperty uuid = new CsdlProperty().setName("uuid")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty active = new CsdlProperty().setName("active")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty idmKey = new CsdlProperty().setName("cris-id")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty crossrefid = new CsdlProperty().setName("crossrefid")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty date = new CsdlProperty().setName("date")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty description = new CsdlProperty().setName("description")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty director = new CsdlProperty().setName("director")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty endDate = new CsdlProperty().setName("enddate")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty name = new CsdlProperty().setName("name")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty url = new CsdlProperty().setName("url")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	
	CsdlProperty parentorgunit = new CsdlProperty().setName("parentorgunit")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	
	

	CsdlPropertyRef propertyRef = new CsdlPropertyRef();
	propertyRef.setName("id");

	entityType = new CsdlEntityType();
	entityType.setName(ET_ORGUNIT_NAME);
	entityType.setProperties(Arrays.asList(id, uuid, idmKey, name, description, url, director, date, endDate,active,crossrefid, parentorgunit));
	entityType.setKey(Arrays.asList(propertyRef));
	
	entitySet = new CsdlEntitySet();
	entitySet.setName(ES_ORGUNITS_NAME);
	entitySet.setType(ET_ORGUNIT_FQN);
	
	mapping = new HashMap<String, String>();
	mapping.put("cris-id", "cris-id");
	mapping.put("uuid", "cris-uuid");
	mapping.put("active", "crisou.active");
	mapping.put("crossrefid", "crisou.crossrefid");
	mapping.put("director","crisou.director");
	mapping.put("date", "crisou.date");
	mapping.put("description", "crisou.description");
	mapping.put("endDate", "crisou.enddate");
	mapping.put("name", "crisou.name");
	mapping.put("parentorgunit", "crisou.parentorgunit_authority");
	mapping.put("url", "crisou.url");
	
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
		String navigationFilter = "";
		if(sourceType.equals("Orgunits")) {
			navigationFilter = ("crisou.parentorgunit_authority:\""+id+"\"");
		}		
		return navigationFilter;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}
	
}
