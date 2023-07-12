package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Career implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_CAREER_NAME = "Career";
	public static final FullQualifiedName CT_CAREER_FQN = new FullQualifiedName(NAMESPACE, CT_CAREER_NAME);

	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	public Career() {

		CsdlProperty startdate = new CsdlProperty().setName("startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("enddate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty desc = new CsdlProperty().setName("desc")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty place = new CsdlProperty().setName("place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		complexType = new CsdlComplexType();
		complexType.setName(CT_CAREER_NAME);
		complexType.setProperties(Arrays.asList(startdate, enddate, desc, place));
		
		mapping = new HashMap<>();
		
		mapping.put("startdate", "crisrp.career.startdate");
		mapping.put("enddate", "crisrp.career.enddate");
		mapping.put("desc","crisrp.career.description");
		mapping.put("place", "crisrp.career.place");
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

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

