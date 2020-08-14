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
	
	public final static String NAMESPACE = "profkat";

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
	CsdlProperty crisId = new CsdlProperty().setName("cris-id")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty name = new CsdlProperty().setName("name")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty outype = new CsdlProperty().setName("outype")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty place = new CsdlProperty().setName("place")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty founded = new CsdlProperty().setName("founded")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty dissolved = new CsdlProperty().setName("dissolved")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty isPartOf = new CsdlProperty().setName("isPartOf")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty cognateBody = new CsdlProperty().setName("cognateBody")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty gnd = new CsdlProperty().setName("gnd")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty comment = new CsdlProperty().setName("comment")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

	CsdlPropertyRef propertyRef = new CsdlPropertyRef();
	propertyRef.setName("id");

	entityType = new CsdlEntityType();
	entityType.setName(ET_ORGUNIT_NAME);
	entityType.setProperties(Arrays.asList(id, crisId, name, outype, place, founded, dissolved, isPartOf,cognateBody,gnd, comment));
	entityType.setKey(Arrays.asList(propertyRef));
	
	entitySet = new CsdlEntitySet();
	entitySet.setName(ES_ORGUNITS_NAME);
	entitySet.setType(ET_ORGUNIT_FQN);
	
	mapping = new HashMap<String, String>();
	
	mapping.put("cris-id", "cris-id");
	mapping.put("name", "crisou.name");
	mapping.put("outype", "crisou.outype");
	mapping.put("place", "crisou.place");
	mapping.put("founded", "crisou.founded");
	mapping.put("dissolved", "crisou.dissolved");
	mapping.put("isPartOf", "crisou.isPartOf");
	mapping.put("cognateBody", "crisou.cognateBody");
	mapping.put("gnd", "crisou.gnd");
	mapping.put("comment", "crisou.comment");
	
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
		return mapping;
	}

	
	
}
