package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Series implements EntityModel {

	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_SERIES_NAME = "Serie";
	public static final FullQualifiedName ET_SERIES_FQN = new FullQualifiedName(NAMESPACE, ET_SERIES_NAME);
	public static final String ES_SERIES_NAME = "Series";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Series\"";
	private final HashMap<String, String> idconverter;

	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;
	
	public Series() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(series[0-9]{1,6})", "cris.legacyId"); //until series09999
		idconverter.put("([1-9][0-9]{4,5})|([1]{1}[0-9]{5})", "handle"); //everything greater than 10.000 or greater than 100.000
		idconverter.put("([0]{0,1}[0-9]{1,4})", "cris.legacyId"); //until (0)9999
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
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issn = new CsdlProperty().setName("issn")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty abbrevation = new CsdlProperty().setName("abbreviation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty homepage = new CsdlProperty().setName("homepage")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		// creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties
		
		entityType = new CsdlEntityType();
		entityType.setName(ET_SERIES_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, name, issn, abbrevation, homepage, description));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_SERIES_NAME);
		entitySet.setType(ET_SERIES_FQN);
			
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("issn", List.of("crisseries.issn"));
		mapping.put("abbreviation", List.of("crisseries.abbreviation"));
		mapping.put("homepage", List.of("crisseries.homepage"));
		mapping.put("description", List.of("crisseries.description"));
		
		ENTITYFILTER = new ArrayList<>();
	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_SERIES_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_SERIES_NAME;
	}

	@Override
	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

	@Override
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
		return "series";
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
