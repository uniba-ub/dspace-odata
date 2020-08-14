package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

/**
 * equals orgunit / ou in dspace
 * */
public class Corporation implements EntityModel{
	
	public final static String NAMESPACE = "profkat";

	public static final String ET_ORGUNIT_NAME = "Corporation";
	public static final FullQualifiedName ET_ORGUNIT_FQN = new FullQualifiedName(NAMESPACE, ET_ORGUNIT_NAME);
	public static final String ES_ORGUNITS_NAME = "Corporations";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"003body\n|||\nInstitutions###body\"";
	public final static String ID_CONVERTER_TYP= "ou";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;

	public Corporation(){
		
	CsdlProperty id = new CsdlProperty().setName("id")
			.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

	CsdlProperty crisid = new CsdlProperty().setName("cris-id")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty cognate = new CsdlProperty().setName("cognateBody")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty partof = new CsdlProperty().setName("partof")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty dissolved = new CsdlProperty().setName("dissolved")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty founded = new CsdlProperty().setName("founded")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty gnd = new CsdlProperty().setName("gnd")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty name = new CsdlProperty().setName("name")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty typ = new CsdlProperty().setName("typ")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty place = new CsdlProperty().setName("place")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	//authority links for browsing
	CsdlProperty ou2place = new CsdlProperty().setName("ou2place")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	//TODO: own properties for cognate and part of values
	CsdlProperty ou2ou = new CsdlProperty().setName("ou2ou")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	

	CsdlPropertyRef propertyRef = new CsdlPropertyRef();
	propertyRef.setName("id");

	entityType = new CsdlEntityType();
	entityType.setName(ET_ORGUNIT_NAME);
	entityType.setProperties(Arrays.asList(id,crisid, cognate, dissolved, founded, gnd, name, typ, place, ou2place, ou2ou ));
	entityType.setKey(Arrays.asList(propertyRef));
	
	entitySet = new CsdlEntitySet();
	entitySet.setName(ES_ORGUNITS_NAME);
	entitySet.setType(ET_ORGUNIT_FQN);
	
	mapping = new HashMap<String, String>();
	//TODO: check Mapping for corporations etc.... Not all might be indexed
	mapping.put("cris-id", "cris-id");
	mapping.put("typ", "crisou.outype");
	mapping.put("name", "crisou.name");
	mapping.put("place", "crisou.place");
	mapping.put("founded", "crisou.founded");
	mapping.put("dissolved", "crisou.dissolved");
	mapping.put("gnd", "crisou.gnd");
	mapping.put("cognateBody", "crisou.cognateBody");
	mapping.put("partof", "crisou.isPartOf");
	mapping.put("ou2place", "crisou.place_authority");
	mapping.put("ou2ou", "crisou.isPartOf_authority");
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
		if(sourceType.equals("Corporation")) {
			navigationFilter = ("crisou.isPartOf_authority:\""+id+"\"");
		}
		if(sourceType.equals("Places")) {
			navigationFilter = ("crisou.place_authority:\""+id+"\"");
		}
		return navigationFilter;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}
	
	
 }
