package entitys;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlAnnotationPath;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlExpression;

import util.Util;

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
		
		// configuration of the Entity Type and adding of properties
		entityType = new CsdlEntityType();
		entityType.setName(ET_RESEARCHER_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, displayName, researchinterests, description, title, email, biography, researcharea, contacturl, contactemail, orcid, dept, contact, rp2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_RESEARCHERS_NAME);
		entitySet.setType(ET_RESEARCHER_FQN);
			
		mapping = new HashMap<String, String>();
		
		mapping.put("cris-id", "cris-id");
		mapping.put("biography", "biography");
		mapping.put("contact", "crisrp.contact");
		mapping.put("contacturl", "crisrp.contacturl");
		mapping.put("contactemail", "crisrp.contactemail");
		mapping.put("displayname", "crisrp.displayName");
		mapping.put("description", "crisrp.description");
		mapping.put("dept", "crisrp.dept");
		mapping.put("email", "crisrp.email");
		mapping.put("orcid", "crisrp.orcid");
		mapping.put("researcharea", "crisrp.researcharea");
		mapping.put("researchinterests", "crisrp.researchinterests");
		mapping.put("title", "crisrp.title");
		
		mapping.put("rp2ou", "crisrp.dept_authority");
		
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
	
