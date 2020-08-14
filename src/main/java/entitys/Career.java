package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Career implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_CAREER_NAME = "Career";
	public static final FullQualifiedName CT_CAREER_FQN = new FullQualifiedName(NAMESPACE, CT_CAREER_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersoncareer";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Career() {

		CsdlProperty careerstep = new CsdlProperty().setName("careerstep")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty ou = new CsdlProperty().setName("ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty start = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty end = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_CAREER_NAME);
		complexType.setProperties(Arrays.asList(careerstep, ou, start, end, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("careerstep", "ncrispersonpersoncareer.careerstep_career");
		mapping.put("ou", "ncrispersonpersoncareer.ou_career");
		mapping.put("start", "ncrispersonpersoncareer.start_career");
		mapping.put("end", "ncrispersonpersoncareer.end_career");
		mapping.put("note", "ncrispersonpersoncareer.note_career");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_CAREER_FQN;
	}
	public String getName() {
		return CT_CAREER_NAME;
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
