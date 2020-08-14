package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class PeriodOfLife implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_PERIODOFLIFE_NAME = "PeriodOfLife";
	public static final FullQualifiedName CT_PERIODOFLIFE_FQN = new FullQualifiedName(NAMESPACE, CT_PERIODOFLIFE_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonperiodoflife";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public PeriodOfLife() {

		CsdlProperty activity = new CsdlProperty().setName("activity")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty body = new CsdlProperty().setName("body")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty start = new CsdlProperty().setName("start")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty end = new CsdlProperty().setName("end")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_PERIODOFLIFE_NAME);
		complexType.setProperties(Arrays.asList(activity, body, start, end, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("activity", "ncrispersonpersonperiodoflife.activity_periodoflife");
		mapping.put("body", "ncrispersonpersonperiodoflife.body_periodoflife");
		mapping.put("start", "ncrispersonpersonperiodoflife.start_periodoflife");
		mapping.put("end", "ncrispersonpersonperiodoflife.end_periodoflife");
		mapping.put("note", "ncrispersonpersonperiodoflife.note_periodoflife");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_PERIODOFLIFE_FQN;
	}
	public String getName() {
		return CT_PERIODOFLIFE_NAME;
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
