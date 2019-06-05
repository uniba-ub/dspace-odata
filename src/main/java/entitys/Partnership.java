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
	public final static String RECOURCE_TYPE_FILTER= 
	"resourcetype_filter:\"nobjects\n" + 
	"|||\n" + 
	"N-Object###default\"";
	// nested objects need a parent key
	public static final int PARENT_FK = 10;
	public static final String SCHEMA = "ncrisprojectpartnership";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Partnership() {

		CsdlProperty projectpartner = new CsdlProperty().setName("projectpartner")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty cooperative = new CsdlProperty().setName("cooperative")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		

		complexType = new CsdlComplexType();
		complexType.setName(CT_PARTNERSHIP_NAME);
		complexType.setProperties(Arrays.asList(name, projectpartner, cooperative, description, url, type));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("projectpartner", "ncrisprojectpartnership.partnershipprojectpartner");
		mapping.put("cooperative","ncrisprojectpartnership.partnershipcooperative");
		mapping.put("name", "ncrisprojectpartnership.partnershipname");
		mapping.put("description", "ncrisprojectpartnership.partnershipdescription");
		mapping.put("url", "ncrisprojectpartnership.partnershipurl");
		mapping.put("type", "ncrisprojectpartnership.partnershiptypeofpartnership");

		
		
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
		
	public String getRecourceTypeFilter() {
		return RECOURCE_TYPE_FILTER;
	}

	public int getParentFK() {
		return PARENT_FK;
	}

	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}


	public String getSchema() {
		return SCHEMA;
	}


}
