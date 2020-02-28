package entitys;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Researcher implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_RESEARCHER_NAME = "Researcher";
	public static final FullQualifiedName ET_RESEARCHER_FQN = new FullQualifiedName(NAMESPACE, ET_RESEARCHER_NAME);
	public static final String ES_RESEARCHERS_NAME = "Researchers";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"009researchers\n|||\nResearcher profiles###researcherprofiles\"";
	public final static String ID_CONVERTER_TYP= "rp";
	private HashMap<String, String> mapping;

	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Researcher() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty displayName = new CsdlProperty().setName("displayname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researchinterests = new CsdlProperty().setName("researchinterests")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty email = new CsdlProperty().setName("email")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty position = new CsdlProperty().setName("position")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty transferabstract = new CsdlProperty().setName("transferabstract")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty transferkeywords = new CsdlProperty().setName("transferkeywords")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contactroom = new CsdlProperty().setName("contactroom")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contactaddress = new CsdlProperty().setName("contactaddress")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contactphone = new CsdlProperty().setName("contactphone")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contacturl = new CsdlProperty().setName("contacturl")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contactemail = new CsdlProperty().setName("contactemail")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty consultation = new CsdlProperty().setName("consultation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researcharea = new CsdlProperty().setName("researcharea")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty biography = new CsdlProperty().setName("biography")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty dept = new CsdlProperty().setName("dept")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty orcid = new CsdlProperty().setName("orcid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties

		entityType = new CsdlEntityType();
		entityType.setName(ET_RESEARCHER_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, displayName, researchinterests, description, title, email, position, biography, researcharea, contactroom, contactaddress, contactphone, contacturl, contactemail, consultation, transferabstract, transferkeywords, orcid, dept));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_RESEARCHERS_NAME);
		entitySet.setType(ET_RESEARCHER_FQN);
			
		mapping = new HashMap<String, String>();
		
		mapping.put("cris-id", "cris-id");
		mapping.put("displayname", "crisrp.displayName");
		mapping.put("researchinterests", "crisrp.researchinterests");
		mapping.put("description", "crisrp.description");
		mapping.put("title", "crisrp.title");
		mapping.put("email", "crisrp.email");
		mapping.put("position", "crisrp.position");
		mapping.put("transferabstract", "crisrp.transfer-abstract");
		mapping.put("transferkeywords", "crisrp.transfer-keyword");
		mapping.put("contactroom", "crisrp.contactroom");
		mapping.put("contactaddress", "crisrp.contactaddress");
		mapping.put("contactphone", "crisrp.contactphone");
		mapping.put("contacturl", "crisrp.contacturl");
		mapping.put("contactemail", "crisrp.contactemail");
		mapping.put("consultation", "crisrp.consultation");
		mapping.put("researcharea", "crisrp.researcharea");
		mapping.put("biography", "biography");
		mapping.put("orcid", "crisrp.orcid");
		mapping.put("dept", "crisrp.dept");
	}
	
	public CsdlEntityType getEntityType() {	
		
		return entityType;
	}
	
	public FullQualifiedName getFullQualifiedName() {
		return ET_RESEARCHER_FQN;
	}

	public String getEntitySetName() {
		return ES_RESEARCHERS_NAME;
	}

	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

	public String getRecourceTypeFilter() {
		return RECOURCE_TYPE_FILTER;
	}

	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}

	public String getNavigationFilter(String sourceType, String id) {
			String navigationFilter = "";
			if(sourceType.equals("Orgunits")) {
				navigationFilter = ("crisrp.dept_authority:\"");
				navigationFilter = (navigationFilter+id+"\"");
			}
				return navigationFilter;
		
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
	
