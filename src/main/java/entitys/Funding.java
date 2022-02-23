package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Funding implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_FUNDING_NAME = "Funding";
	public static final FullQualifiedName CT_FUNDING_FQN = new FullQualifiedName(NAMESPACE, CT_FUNDING_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 10;
	public static final String SCHEMA = "ncrisprojectfunding";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Funding() {

		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty funder = new CsdlProperty().setName("funder")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty funder_auth = new CsdlProperty().setName("funder_authority")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty grant = new CsdlProperty().setName("grant")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty program = new CsdlProperty().setName("program")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty period = new CsdlProperty().setName("period")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty supportCode = new CsdlProperty().setName("supportcode")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_FUNDING_NAME);
		complexType.setProperties(Arrays.asList(uuid, funder, funder_auth, period, supportCode, grant, program, url));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("uuid", "cris-uuid");
		mapping.put("funder", "ncrisprojectfunding.fundingfunder");
		mapping.put("funder_authority", "ncrisprojectfunding.fundingfunder_auth");
		mapping.put("grant", "ncrisprojectfunding.fundinggrant");
		mapping.put("period","ncrisprojectfunding.fundingperiod");
		mapping.put("program", "ncrisprojectfunding.fundingprogram");
		mapping.put("supportcode", "ncrisprojectfunding.fundingsupportcode");
		mapping.put("url", "ncrisprojectfunding.fundingurl");

	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_FUNDING_FQN;
	}
	public String getName() {
		return CT_FUNDING_NAME;
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
