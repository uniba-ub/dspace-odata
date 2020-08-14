package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Home implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_HOME_NAME = "Home";
	public static final FullQualifiedName CT_HOME_FQN = new FullQualifiedName(NAMESPACE, CT_HOME_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonhome";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Home() {

		CsdlProperty address = new CsdlProperty().setName("address")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty place = new CsdlProperty().setName("place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty from = new CsdlProperty().setName("from")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty to = new CsdlProperty().setName("to")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		

		complexType = new CsdlComplexType();
		complexType.setName(CT_HOME_NAME);
		complexType.setProperties(Arrays.asList(address, place, from, to));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("address", "ncrispersonpersonhome.address_home");
		mapping.put("place", "ncrispersonpersonhome.place_home");
		mapping.put("from", "ncrispersonpersonhome.from_home");
		mapping.put("to", "ncrispersonpersonhome.to_home");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_HOME_FQN;
	}
	public String getName() {
		return CT_HOME_NAME;
	}

	public int getParentFK() {
		return PARENT_FK;
	}

	public String getSchema() {
		return SCHEMA;
	}

	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
