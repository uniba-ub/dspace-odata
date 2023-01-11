package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Education implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_EDUCATION_NAME = "Education";
	public static final FullQualifiedName CT_EDUCATION_FQN = new FullQualifiedName(NAMESPACE, CT_EDUCATION_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 9;
	public static final String SCHEMA = "ncrisrpeducation";
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	public Education() {

		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startdate = new CsdlProperty().setName("startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("enddate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty desc = new CsdlProperty().setName("desc")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty place = new CsdlProperty().setName("place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		complexType = new CsdlComplexType();
		complexType.setName(CT_EDUCATION_NAME);
		complexType.setProperties(Arrays.asList(uuid, startdate, enddate, desc, place));
		
		mapping = new HashMap<>();
		
		mapping.put("uuid", "cris-uuid");
		mapping.put("startdate", "ncrisrpeducation.educationstartdate");
		mapping.put("enddate", "ncrisrpeducation.educationenddate");
		mapping.put("desc","ncrisrpeducation.educationdescription");
		mapping.put("place", "ncrisrpeducation.educationplace");
	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_EDUCATION_FQN;
	}
	public String getName() {
		return CT_EDUCATION_NAME;
	}

	public int getParentFK() {
		return PARENT_FK;
	}

	public String getSchema() {
		return SCHEMA;
	}

	public String getNavigationFilter() {
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

