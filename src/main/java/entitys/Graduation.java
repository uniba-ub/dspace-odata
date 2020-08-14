package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Graduation implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_GRADUATION_NAME = "Life";
	public static final FullQualifiedName CT_GRADUATION_FQN = new FullQualifiedName(NAMESPACE, CT_GRADUATION_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersongraduation";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Graduation() {

		CsdlProperty qualification = new CsdlProperty().setName("qualification")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty body = new CsdlProperty().setName("body")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty date = new CsdlProperty().setName("date")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty document = new CsdlProperty().setName("document")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty reviewer = new CsdlProperty().setName("reviewer")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		

		complexType = new CsdlComplexType();
		complexType.setName(CT_GRADUATION_NAME);
		complexType.setProperties(Arrays.asList(qualification, body, date, document, reviewer, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("qualification", "ncrispersonpersongraduation.qualification_graduation");
		mapping.put("body", "ncrispersonpersongraduation.body_graduation");
		mapping.put("date", "ncrispersonpersongraduation.date_graduation");
		mapping.put("document", "ncrispersonpersongraduation.document_graduation");
		mapping.put("reviewer", "ncrispersonpersongraduation.reviewer_graduation");
		mapping.put("note", "ncrispersonpersongraduation.note_graduation");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_GRADUATION_FQN;
	}
	public String getName() {
		return CT_GRADUATION_NAME;
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
