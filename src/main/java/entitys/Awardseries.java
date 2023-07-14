package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Awardseries implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_AWARDSERIES_NAME = "Awardserie";
	public static final FullQualifiedName ET_AWARDSERIES_FQN = new FullQualifiedName(NAMESPACE, ET_AWARDSERIES_NAME);
	public static final String ES_AWARDSERIES_NAME = "Awardseries";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Awardseries\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;
	
	public Awardseries() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(awardseries[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([1-9]{1}[0-9]{4})|([1]{1}[0-9]{5})", "handle"); //greater than 10.000 or 100.000
		idconverter.put("([0]{0,1}[0-9]{1,4})", "cris.legacyId"); //until awardseries09999 are considered as legcayvalues
		idconverter.put("(uniba/[0-9]{1,6})", "handle");

		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty category = new CsdlProperty().setName("category")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty institution = new CsdlProperty().setName("institution")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty awardseries2funder = new CsdlProperty().setName("awardseries2funder")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties		
		entityType = new CsdlEntityType();
		entityType.setName(ET_AWARDSERIES_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, category, description, institution, name, url, awardseries2funder));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_AWARDSERIES_NAME);
		entitySet.setType(ET_AWARDSERIES_FQN);
			
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));

		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("description", List.of("crisawardseries.description"));
		mapping.put("category", List.of("crisawardseries.category"));
		mapping.put("institution", List.of("crisawardseries.institution"));
		mapping.put("url", List.of("crisawardseries.url"));
		mapping.put("awardseries2funder", List.of("crisawardseries.institution_authority"));
		
		ENTITYFILTER = new ArrayList<>();
	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_AWARDSERIES_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_AWARDSERIES_NAME;
	}

	@Override
	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

	@Override
	public String getRecourceTypeFilter() {
		return RECOURCE_TYPE_FILTER;
	}

	@Override
	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "awardseries";
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		return null;
	}

	@Override
	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
