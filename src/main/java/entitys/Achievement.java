package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Achievement implements ComplexModel {
	
	public final static String NAMESPACE = "dspace";

	public static final String CT_ACHIEVEMENT_NAME = "Achievement";
	public static final FullQualifiedName CT_ACHIEVEMENT_FQN = new FullQualifiedName(NAMESPACE, CT_ACHIEVEMENT_NAME);
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	public Achievement() {
		CsdlProperty startdate = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty desc = new CsdlProperty().setName("desc")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty event = new CsdlProperty().setName("event")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	

		complexType = new CsdlComplexType();
		complexType.setName(CT_ACHIEVEMENT_NAME);
		complexType.setProperties(Arrays.asList(startdate, enddate, desc, event, type));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("start", "crisrp.achievement.start");
		mapping.put("end", "crisrp.achievement.end");
		mapping.put("desc","crisrp.achievement.description");
		mapping.put("event", "crisrp.achievement.event");
		mapping.put("type", "crisrp.achievement.type");

	}

	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_ACHIEVEMENT_FQN;
	}
	public String getName() {
		return CT_ACHIEVEMENT_NAME;
	}

	public String getNavigationFilter(String sourceType, String id) {
		return null;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}

