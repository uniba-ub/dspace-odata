package entitys;

import java.util.ArrayList;
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
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Person\"";
	public final static String ID_CONVERTER_TYP= "rp";
	private HashMap<String, String> mapping;
	private ArrayList<String> ENTITYFILTER;


	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Researcher() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty biography = new CsdlProperty().setName("biography")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contact = new CsdlProperty().setName("contact")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contacturl = new CsdlProperty().setName("contacturl")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty contactemail = new CsdlProperty().setName("contactemail")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty displayName = new CsdlProperty().setName("displayname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty dept = new CsdlProperty().setName("dept")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty email = new CsdlProperty().setName("email")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty orcid = new CsdlProperty().setName("orcid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gnd = new CsdlProperty().setName("gnd")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researcharea = new CsdlProperty().setName("researcharea")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researchinterests = new CsdlProperty().setName("researchinterests")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		CsdlProperty rp2ou = new CsdlProperty().setName("rp2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");
		
		//complex type
		CsdlProperty affiliation = new CsdlProperty().setName("Affiliation").setType(Affiliation.CT_AFFILIATION_FQN).setCollection(true);
		CsdlProperty achievement = new CsdlProperty().setName("Achievement").setType(Achievement.CT_ACHIEVEMENT_FQN).setCollection(true);
		CsdlProperty education = new CsdlProperty().setName("Education").setType(Education.CT_EDUCATION_FQN).setCollection(true);
		CsdlProperty career = new CsdlProperty().setName("Career").setType(Career.CT_CAREER_FQN).setCollection(true);

		
		// configuration of the Entity Type and adding of properties
		entityType = new CsdlEntityType();
		entityType.setName(ET_RESEARCHER_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, displayName, researchinterests, description, title, email, biography, researcharea, contacturl, contactemail, orcid, gnd, dept, contact, affiliation, career, education, achievement, rp2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_RESEARCHERS_NAME);
		entitySet.setType(ET_RESEARCHER_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris.legacyId");
		mapping.put("uuid", "search.resourceid");
		mapping.put("handle", "handle");
		mapping.put("entitytype", "search.entitytype");
		mapping.put("biography", "biography");
		mapping.put("contact", "crisrp.contact");
		mapping.put("contacturl", "crisrp.contacturl");
		mapping.put("contactemail", "crisrp.contactemail");
		mapping.put("displayname", "crisrp.this");
		mapping.put("description", "crisrp.description");
		mapping.put("dept", "crisrp.dept");
		mapping.put("email", "crisrp.email");
		mapping.put("orcid", "crisrp.orcid");
		mapping.put("gnd", "crisrp.gndId");
		mapping.put("researcharea", "crisrp.researcharea");
		mapping.put("researchinterests", "crisrp.researchinterests");
		mapping.put("title", "crisrp.title");
		
		mapping.put("rp2ou", "crisrp.dept_authority");
		
		ENTITYFILTER = new ArrayList<String>();
		ENTITYFILTER.add("-ubg.version.visibility:0");
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
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
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
	
