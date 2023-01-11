package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Affiliation implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_AFFILIATION_NAME = "Affiliation";
	public static final FullQualifiedName CT_AFFILIATION_FQN = new FullQualifiedName(NAMESPACE, CT_AFFILIATION_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 9;
	public static final String SCHEMA = "ncrisrpaffiliation";
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	public Affiliation() {

		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startdate = new CsdlProperty().setName("startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("enddate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty role = new CsdlProperty().setName("role")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ouname = new CsdlProperty().setName("ouname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty aff2ou = new CsdlProperty().setName("aff2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_AFFILIATION_NAME);
		complexType.setProperties(Arrays.asList(uuid, startdate, enddate, role, ouname, aff2ou));
		
		mapping = new HashMap<>();
		
		mapping.put("uuid", "cris-uuid");
		mapping.put("startdate", "ncrisrpaffiliation.affiliationstartdate");
		mapping.put("enddate", "ncrisrpaffiliation.affiliationenddate");
		mapping.put("role","ncrisrpaffiliation.affiliationrole");
		mapping.put("ouname", "ncrisrpaffiliation.affiliationorgunit");
		mapping.put("aff2ou", "ncrisrpaffiliation.affiliationorgunit_authority");
		//NOTE: Some kind of navigation paths to OU? realizable?
	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_AFFILIATION_FQN;
	}
	public String getName() {
		return CT_AFFILIATION_NAME;
	}

	public int getParentFK() {
		return PARENT_FK;
	}

	public String getSchema() {
		return SCHEMA;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

