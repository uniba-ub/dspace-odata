package entitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"OrgUnit\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;

	public Orgunit(){
	idconverter = new HashMap<>();
	idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
	idconverter.put("(ou[0-9]{1,6})", "cris.legacyId");
	idconverter.put("([1-9]{1}[0-9]{4})|([1]{1}[0-9]{5})", "handle"); //Greater than 10.000 or greater than 100.000
	idconverter.put("([0]{0,1}[0-9]{1,4})", "cris.legacyId"); //until ou09999 are considered as legcayvalues
	idconverter.put("(uniba/[0-9]{1,6})", "handle");
		
	CsdlProperty id = new CsdlProperty().setName("id")
			.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
	CsdlProperty crisid = new CsdlProperty().setName("cris-id")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty uuid = new CsdlProperty().setName("uuid")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	CsdlProperty handle = new CsdlProperty().setName("handle")
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
	entityType.setProperties(Arrays.asList(id, crisid, uuid, handle, entitytype, name, description, url, director, date, endDate, parentorgunit));
	entityType.setKey(List.of(propertyRef));
	
	entitySet = new CsdlEntitySet();
	entitySet.setName(ES_ORGUNITS_NAME);
	entitySet.setType(ET_ORGUNIT_FQN);
	
	mapping = new HashMap<>();
	mapping.put("cris-id", List.of("cris.legacyId"));
	mapping.put("uuid", List.of("search.resourceid"));
	mapping.put("handle", List.of("handle"));
	mapping.put("entitytype", List.of("search.entitytype"));
	mapping.put("name", List.of("dc.title"));

	mapping.put("director",List.of("crisou.director"));
	mapping.put("date", List.of("crisou.startdate"));
	mapping.put("description", List.of("crisou.description"));
	mapping.put("endDate", List.of("crisou.enddate"));
	mapping.put("parentorgunit", List.of("crisou.parentorgunit_authority"));
	mapping.put("url", List.of("crisou.url"));
	
	ENTITYFILTER = new ArrayList<>();
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

	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "ou";
	}

	public String getNavigationFilter(String sourceType, String id) {
		String navigationFilter = "";
		if (sourceType.equals("Orgunits")) {
			navigationFilter = ("crisou.parentorgunit_authority:\""+id+"\"");
		}		
		return navigationFilter;
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}
	
}
