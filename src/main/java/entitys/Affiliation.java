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
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;

	public Affiliation() {

		CsdlProperty startdate = new CsdlProperty().setName("startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("enddate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty role = new CsdlProperty().setName("role")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ouname = new CsdlProperty().setName("organisation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty aff2ou = new CsdlProperty().setName("aff2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_AFFILIATION_NAME);
		complexType.setProperties(Arrays.asList(startdate, enddate, role, ouname, aff2ou));

		mapping = new HashMap<>();
		mapping.put("startdate", "oairecerif.affiliation.startDate");
		mapping.put("enddate", "oairecerif.affiliation.endDate");
		mapping.put("role",   "oairecerif.affiliation.role");
		mapping.put("organisation", "oairecerif.person.affiliation");
		mapping.put("aff2ou", "oairecerif.person.affiliation_authority");
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

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

