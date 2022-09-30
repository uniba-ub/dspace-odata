package entitys;

import java.util.*;

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

	private HashMap<String, List<String>> mapping;
	private ArrayList<String> ENTITYFILTER;

	private HashMap<String, String> idconverter;
	
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	
	public Researcher() {
		idconverter = new HashMap<String, String>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(rp[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([4-9][0-9]{1,5})", "handle");
		idconverter.put("([0-3][0-9]{1,4})", "cris.legacyId"); //rp until rp30000 are considered as legcyvalues
		idconverter.put("(uniba/[0-9]{1,6})", "handle");
		idconverter.put("(([0-9]{4})-([0-9]{4})-([0-9]{4})-([0-9]{4})){1}", "person.identifier.orcid");
		
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
		CsdlProperty name = new CsdlProperty().setName("name")
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
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, name, displayName, researchinterests, description, title, email, biography, researcharea, contacturl, contactemail, orcid, gnd, dept, contact, affiliation, career, education, achievement, rp2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_RESEARCHERS_NAME);
		entitySet.setType(ET_RESEARCHER_FQN);
			
		mapping = new HashMap<String, List<String>>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));
		
		mapping.put("biography", List.of("crisrp.biography"));
		mapping.put("contact", List.of("crisrp.contact"));
		mapping.put("contacturl", List.of("crisrp.contacturl"));
		mapping.put("contactemail", List.of("crisrp.contactemail"));
		mapping.put("displayname", List.of("dc.title"));
		mapping.put("description", List.of("crisrp.description"));
		mapping.put("dept", List.of("crisrp.dept"));
		mapping.put("email", List.of("crisrp.email"));
		mapping.put("orcid", List.of("person.identifier.orcid"));
		mapping.put("gnd", List.of("crisrp.gndId"));
		mapping.put("researcharea", List.of("crisrp.researcharea"));
		mapping.put("researchinterests", List.of("crisrp.researchinterests"));
		mapping.put("title", List.of("crisrp.title"));
		
		mapping.put("rp2ou", List.of("crisrp.dept_authority"));
		
		ENTITYFILTER = new ArrayList<String>();
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

	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "rp";
	}

	public String getNavigationFilter(String sourceType, String id) {
			String navigationFilter = "";
			if(sourceType.equals("Orgunits")) {
				navigationFilter = ("crisrp.dept_authority:\"");
				navigationFilter = (navigationFilter+id+"\"");
			}
				return navigationFilter;
		
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
	
