package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Membership implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_MEMBERSHIP_NAME = "Membership";
	public static final FullQualifiedName CT_MEMBERSHIP_FQN = new FullQualifiedName(NAMESPACE, CT_MEMBERSHIP_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonmembership";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Membership() {

		CsdlProperty type = new CsdlProperty().setName("type")
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
		complexType.setName(CT_MEMBERSHIP_NAME);
		complexType.setProperties(Arrays.asList(type, body, start, end, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("type", "ncrispersonpersonmembership.type_membership");
		mapping.put("body", "ncrispersonpersonmembership.body_membership");
		mapping.put("start", "ncrispersonpersonmembership.start_membership");
		mapping.put("end", "ncrispersonpersonmembership.end_membership");
		mapping.put("note", "ncrispersonpersonmembership.note_membership");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_MEMBERSHIP_FQN;
	}
	public String getName() {
		return CT_MEMBERSHIP_NAME;
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
