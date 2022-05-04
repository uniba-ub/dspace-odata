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
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Funding() {
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
		complexType.setProperties(Arrays.asList( funder, funder_auth, period, supportCode, grant, program, url));
		
		mapping = new HashMap<String, String>();
		mapping.put("funder", "crispj.funding.funder");
		mapping.put("funder_authority", "crispj.funding.funder_authority");
		mapping.put("grant", "crispj.funding.grant");
		mapping.put("period","crispj.funding.period");
		mapping.put("program", "crispj.funding.program");
		mapping.put("supportcode", "crispj.funding.supportcode");
		mapping.put("url", "crispj.funding.url");

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

	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
