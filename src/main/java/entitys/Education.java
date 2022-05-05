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
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	public Education() {

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
		complexType.setProperties(Arrays.asList(startdate, enddate, desc, place));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("startdate", "crisrp.education.startdate");
		mapping.put("enddate", "crisrp.education.enddate");
		mapping.put("desc","crisrp.education.description");
		mapping.put("place", "crisrp.education.place");
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

	public String getNavigationFilter(String sourceType, String id) {
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

