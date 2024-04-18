package entitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Equipment implements EntityModel {


	public final static String NAMESPACE = "dspace";

	public static final String ET_EQUIPMENT_NAME = "Equipment";
	public static final FullQualifiedName ET_EQUIPMENT_FQN = new FullQualifiedName(NAMESPACE, ET_EQUIPMENT_NAME);
	public static final String ES_EQUIPMENTS_NAME = "Equipments";

	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Equipment\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;

	public Equipment(){
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("([0-9]{1,6})", "handle");
		idconverter.put("(uniba/[0-9]{1,6})", "handle");
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty acronym = new CsdlProperty().setName("acronym")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty internalid = new CsdlProperty().setName("internalid")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ownerou = new CsdlProperty().setName("ownerou")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty equipment2ou = new CsdlProperty().setName("equipment2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		//creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_EQUIPMENT_NAME);
		entityType.setProperties(Arrays.asList(id, uuid, entitytype, handle, name,
			acronym, description, internalid, ownerou,
			equipment2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_EQUIPMENTS_NAME);
		entitySet.setType(ET_EQUIPMENT_FQN);
		
		mapping = new HashMap<>();
		
		mapping.put("handle", List.of("handle"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));
		
		mapping.put("acronym", List.of("dc.creator"));
		mapping.put("description", List.of("dc.creator"));
		mapping.put("internalid", List.of("contributors"));
		mapping.put("ownerou", List.of("crisequipment.ownerou"));

		mapping.put("equipment2ou", List.of("crisequipment.ownerou_authority"));

		ENTITYFILTER = new ArrayList<>();

	}

	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_EQUIPMENT_FQN;
	}

	public String getEntitySetName() {
		return ES_EQUIPMENTS_NAME;
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
		return null;
	}

	public String getNavigationFilter(String sourceType, String id) {
		return switch (sourceType) {
			case "Orgunits" -> ("crisequipment.ownerou_authority:\"" + id + "\"");
			default -> "";
		};
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
