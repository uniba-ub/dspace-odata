package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class SocialRole implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_SOCIALROLE_NAME = "SocialRole";
	public static final FullQualifiedName CT_SOCIALROLE_FQN = new FullQualifiedName(NAMESPACE, CT_SOCIALROLE_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonsocialrole";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public SocialRole() {

		CsdlProperty role = new CsdlProperty().setName("role")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty body = new CsdlProperty().setName("body")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty start = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty end = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_SOCIALROLE_NAME);
		complexType.setProperties(Arrays.asList(role, body, start, end, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("role", "ncrispersonpersonsocialrole.role_socialrole");
		mapping.put("body", "ncrispersonpersonsocialrole.body_socialrole");
		mapping.put("start", "ncrispersonpersonsocialrole.start_socialrole");
		mapping.put("end", "ncrispersonpersonsocialrole.end_socialrole");
		mapping.put("note", "ncrispersonpersonsocialrole.note_socialrole");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_SOCIALROLE_FQN;
	}
	public String getName() {
		return CT_SOCIALROLE_NAME;
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
