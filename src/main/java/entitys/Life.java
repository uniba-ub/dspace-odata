package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Life implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_LIFE_NAME = "Life";
	public static final FullQualifiedName CT_LIFE_FQN = new FullQualifiedName(NAMESPACE, CT_LIFE_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonlife";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Life() {

		CsdlProperty event = new CsdlProperty().setName("event")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty place = new CsdlProperty().setName("place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty date = new CsdlProperty().setName("date")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		

		complexType = new CsdlComplexType();
		complexType.setName(CT_LIFE_NAME);
		complexType.setProperties(Arrays.asList(event, place, date, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("event", "ncrispersonpersonlife.event_life");
		mapping.put("place", "ncrispersonpersonlife.place_life");
		mapping.put("date", "ncrispersonpersonlife.date_life");
		mapping.put("note", "ncrispersonpersonlife.note_life");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_LIFE_FQN;
	}
	public String getName() {
		return CT_LIFE_NAME;
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
