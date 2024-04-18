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

public class Event implements EntityModel {


	public final static String NAMESPACE = "dspace";

	public static final String ET_EVENT_NAME = "Event";
	public static final FullQualifiedName ET_EVENT_FQN = new FullQualifiedName(NAMESPACE, ET_EVENT_NAME);
	public static final String ES_EVENTS_NAME = "Events";

	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Event\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;

	public Event(){
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
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty prod2ou= new CsdlProperty().setName("prod2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		//creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();	
		entityType.setName(ET_EVENT_NAME);
		entityType.setProperties(Arrays.asList(id, uuid, entitytype, handle, name,
			title, type, prod2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_EVENTS_NAME);
		entitySet.setType(ET_EVENT_FQN);
		
		mapping = new HashMap<>();
		
		mapping.put("handle", List.of("handle"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("type", List.of("dc.type"));
		mapping.put("title", List.of("dc.title"));

		mapping.put("prod2rp", List.of("contributor_authority"));
		mapping.put("prod2pj", List.of("ubg.relation.project_authority"));
		mapping.put("prod2ou", List.of("dc.relation.creatororgunit_authority"));

		ENTITYFILTER = new ArrayList<>();

	}

	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_EVENT_FQN;
	}

	public String getEntitySetName() {
		return ES_EVENTS_NAME;
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
			case "Persons" -> ("contributor_authority:\"" + id + "\"");
			case "Orgunits" -> ("ubg.relation.creatororgunit_authority:\"" + id + "\"");
			case "Projects" -> ("ubg.relation.project_authority:\"" + id + "\"");
			default -> "";
		};
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
