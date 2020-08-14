package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

/**
 * alias Ehrungen
 * */
public class Awards implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_AWARDS_NAME = "Awards";
	public static final FullQualifiedName CT_AWARDS_FQN = new FullQualifiedName(NAMESPACE, CT_AWARDS_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonawards";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Awards() {

		CsdlProperty award = new CsdlProperty().setName("award")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty body = new CsdlProperty().setName("body")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty date = new CsdlProperty().setName("date")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_AWARDS_NAME);
		complexType.setProperties(Arrays.asList(award, body, date, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("award", "ncrispersonpersonawards.award_awards");
		mapping.put("body", "ncrispersonpersonawards.body_awards");
		mapping.put("date", "ncrispersonpersonawards.date_awards");
		mapping.put("note", "ncrispersonpersonawards.note_awards");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_AWARDS_FQN;
	}
	public String getName() {
		return CT_AWARDS_NAME;
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
