package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Activity implements ComplexModel {

	public final static String NAMESPACE = "dspace";

	public static final String CT_ACTIVITY_NAME = "Activity";
	public static final FullQualifiedName CT_ACTIVITY_FQN = new FullQualifiedName(NAMESPACE, CT_ACTIVITY_NAME);
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;

	public Activity() {
		CsdlProperty startdate = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty desc = new CsdlProperty().setName("desc")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_ACTIVITY_NAME);
		complexType.setProperties(Arrays.asList(startdate, enddate, desc, type));
		
		mapping = new HashMap<>();
		
		mapping.put("start", "crisrp.activity.startdate");
		mapping.put("end", "crisrp.activity.enddate");
		mapping.put("desc","crisrp.activity.description");
		mapping.put("type", "crisrp.activity.type");

	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_ACTIVITY_FQN;
	}
	public String getName() {
		return CT_ACTIVITY_NAME;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

