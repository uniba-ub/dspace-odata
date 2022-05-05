package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Partnership implements ComplexModel {

	public final static String NAMESPACE = "dspace";

	public static final String CT_PARTNERSHIP_NAME = "Partnership";
	public static final FullQualifiedName CT_PARTNERSHIP_FQN = new FullQualifiedName(NAMESPACE, CT_PARTNERSHIP_NAME);
	// nested objects need a parent key and a search schema
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Partnership() {
		
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty projectpartner = new CsdlProperty().setName("projectpartner")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_PARTNERSHIP_NAME);
		complexType.setProperties(Arrays.asList(name, projectpartner, description, url, type));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("description", "crispj.partnership.description");
		mapping.put("projectpartner", "crispj.partnership.projectpartner");
		mapping.put("name", "crispj.partnership.name");
		mapping.put("type", "crispj.partnership.typeofpartnership");
		mapping.put("url", "crispj.partnership.url");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_PARTNERSHIP_FQN;
	}
	public String getName() {
		return CT_PARTNERSHIP_NAME;
	}

	public String getNavigationFilter(String sourceType, String id) {
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}


}
