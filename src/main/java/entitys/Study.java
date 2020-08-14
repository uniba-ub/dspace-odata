package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Study implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_STUDY_NAME = "Study";
	public static final FullQualifiedName CT_STUDY_FQN = new FullQualifiedName(NAMESPACE, CT_STUDY_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonstudy";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Study() {

		CsdlProperty subject = new CsdlProperty().setName("subject")
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
		complexType.setName(CT_STUDY_NAME);
		complexType.setProperties(Arrays.asList(subject, body, start, end, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("subject", "ncrispersonpersonstudy.subject_study");
		mapping.put("body", "ncrispersonpersonstudy.body_study");
		mapping.put("start", "ncrispersonpersonstudy.start_study");
		mapping.put("end", "ncrispersonpersonstudy.end_study");
		mapping.put("note", "ncrispersonpersonstudy.note_study");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_STUDY_FQN;
	}
	public String getName() {
		return CT_STUDY_NAME;
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
