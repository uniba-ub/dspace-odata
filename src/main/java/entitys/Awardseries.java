package entitys;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"awardseries\n|||\nawardseries###crisawardseries\"";
	public final static String ID_CONVERTER_TYP= "awardseries";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Awardseries() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
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
		entityType.setProperties(Arrays.asList(id, crisId, category, description, institution, name, url, awardseries2funder));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_AWARDSERIES_NAME);
		entitySet.setType(ET_AWARDSERIES_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("description", "crisawardseries.awardseriesdescription");
		mapping.put("category", "crisawardseries.awardseriescategory");
		mapping.put("institution", "crisawardseries.awardseriesinstitution");
		mapping.put("name", "crisawardseries.awardseriesname");
		mapping.put("url", "crisawardseries.awardseriesurl");
		mapping.put("awardseries2funder", "crisawardseries.awardseriesinstitution_authority");
		
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
	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
