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
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 9;
	public static final String SCHEMA = "ncrisrpcareer";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
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
		
		mapping = new HashMap<String, String>();
		
		mapping.put("startdate", "ncrisrpcareer.careerstartdate");
		mapping.put("enddate", "ncrisrpcareer.careerenddate");
		mapping.put("desc","ncrisrpcareer.careerdescription");
		mapping.put("place", "ncrisrpcareer.careerplace");
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

