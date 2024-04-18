package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Qualification implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_QUALIFICATION_NAME = "Qualification";
	public static final FullQualifiedName CT_QUALIFICATION_FQN = new FullQualifiedName(NAMESPACE, CT_QUALIFICATION_NAME);

	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	public Qualification() {

		CsdlProperty start = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty end = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		complexType = new CsdlComplexType();
		complexType.setName(CT_QUALIFICATION_NAME);
		complexType.setProperties(Arrays.asList(start, end, title));
		
		mapping = new HashMap<>();
		
		mapping.put("start", "crisrp.qualification.start");
		mapping.put("end", "crisrp.qualification.end");
		mapping.put("title", "crisrp.qualification");
	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_QUALIFICATION_FQN;
	}
	public String getName() {
		return CT_QUALIFICATION_NAME;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

