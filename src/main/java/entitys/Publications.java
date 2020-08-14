package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;

public class Publications implements ComplexModel {

	public final static String NAMESPACE = "profkat";

	public static final String CT_PUBLICATIONS_NAME = "Publications";
	public static final FullQualifiedName CT_PUBLICATIONS_FQN = new FullQualifiedName(NAMESPACE, CT_PUBLICATIONS_NAME);
	// nested objects need a parent key and a search schema
	public static final int PARENT_FK = 1001;
	public static final String SCHEMA = "ncrispersonpersonpublications";
	private CsdlComplexType complexType;
	private HashMap<String, String> mapping;
	
	
	public Publications() {

		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty date = new CsdlProperty().setName("date")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty link = new CsdlProperty().setName("link")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty note = new CsdlProperty().setName("note")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		complexType = new CsdlComplexType();
		complexType.setName(CT_PUBLICATIONS_NAME);
		complexType.setProperties(Arrays.asList(name, date, link, note));
		
		mapping = new HashMap<String, String>();
		
		mapping.put("name", "ncrispersonpersonpublications.name_publications");
		mapping.put("date", "ncrispersonpersonpublications.date_publications");
		mapping.put("link", "ncrispersonpersonpublications.link_publications");
		mapping.put("note", "ncrispersonpersonpublications.note_publications");

	}
	
	public CsdlComplexType getComplexType() {
		return complexType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return CT_PUBLICATIONS_FQN;
	}
	public String getName() {
		return CT_PUBLICATIONS_NAME;
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
