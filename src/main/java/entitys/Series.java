package entitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
	public final static String ID_CONVERTER_TYP= "series";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	private ArrayList<String> ENTITYFILTER;
	
	public Series() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
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
		entityType.setProperties(Arrays.asList(id, crisId, uuid, name, issn, abbrevation, homepage, description));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_SERIES_NAME);
		entitySet.setType(ET_SERIES_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("uuid", "cris-uuid");
		mapping.put("name", "crisseries.journalsname");
		mapping.put("issn", "crisseries.journalsissn");
		mapping.put("abbreviation", "crisseries.journalsabbreviation");
		mapping.put("homepage", "crisseries.journalshomepage");
		mapping.put("description", "crisseries.journalsdescription");
		
		ENTITYFILTER = new ArrayList<String>();
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

	@Override
	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		return null;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
