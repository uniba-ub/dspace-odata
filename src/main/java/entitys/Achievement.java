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
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 9;
	public static final String SCHEMA = "ncrisrpachievement";
	private final CsdlComplexType complexType;
	private final HashMap<String, String> mapping;
	
	public Achievement() {

		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startdate = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty enddate = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty desc = new CsdlProperty().setName("desc")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty event = new CsdlProperty().setName("event")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		

		complexType = new CsdlComplexType();
		complexType.setName(CT_ACHIEVEMENT_NAME);
		complexType.setProperties(Arrays.asList(uuid, startdate, enddate, desc, event));
		
		mapping = new HashMap<>();
		
		mapping.put("uuid", "cris-uuid");
		mapping.put("start", "ncrisrpachievement.achievementstart");
		mapping.put("end", "ncrisrpachievement.achievementend");
		mapping.put("desc","ncrisrpachievement.achievementdescription");
		mapping.put("event", "ncrisrpachievement.achievementtype");

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

