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
	public static final int PARENT_FK = 10;
	public static final String SCHEMA = "ncrisprojectpartnership";
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	
	public Partnership() {
		
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty cooperative = new CsdlProperty().setName("cooperative")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
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
		complexType.setProperties(Arrays.asList(uuid,name, projectpartner, cooperative, description, url, type));
		
		mapping = new HashMap<>();
		
		mapping.put("uuid", "cris-uuid");
		mapping.put("cooperative","ncrisprojectpartnership.partnershipcooperative");
		mapping.put("description", "ncrisprojectpartnership.partnershipdescription");
		mapping.put("projectpartner", "ncrisprojectpartnership.partnershipprojectpartner");
		mapping.put("name", "ncrisprojectpartnership.partnershipname");
		mapping.put("type", "ncrisprojectpartnership.partnershiptypeofpartnership");
		mapping.put("url", "ncrisprojectpartnership.partnershipurl");

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

	public int getParentFK() {
		return PARENT_FK;
	}

	public String getNavigationFilter() {
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}


	public String getSchema() {
		return SCHEMA;
	}


}
