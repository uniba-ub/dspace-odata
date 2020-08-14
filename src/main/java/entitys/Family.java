package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

/*
 * Families
 * TODO: Family is not indexed in Solr. Keeping empty
 * */
public class Family implements EntityModel {

	public final static String NAMESPACE = "profkat";
	
	public static final String ET_FAMILY_NAME = "Family";
	public static final FullQualifiedName ET_FAMILY_FQN = new FullQualifiedName(NAMESPACE, ET_FAMILY_NAME);
	public static final String ES_FAMILYS_NAME = "Families";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"001allFAMILYs\n|||\nFAMILYs###allFAMILYs\"";
	public final static String ID_CONVERTER_TYP= "family";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Family() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		//complex type
		//TODO:
		
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");
		

		entityType = new CsdlEntityType();
		entityType.setName(ET_FAMILY_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, name));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_FAMILYS_NAME);
		entitySet.setType(ET_FAMILY_FQN);
		
		mapping = new HashMap<String, String>();
		mapping.put("id", "id");
		mapping.put("cris-id", "cris-id");
		mapping.put("name", "crisfamily.familyname");
	}
	
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_FAMILY_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_FAMILYS_NAME;
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
