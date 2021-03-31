package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Project implements EntityModel{
	
	public final static String NAMESPACE = "dspace";

	public static final String ET_PROJECT_NAME = "Project";
	public static final FullQualifiedName ET_PROJECT_FQN = new FullQualifiedName(NAMESPACE, ET_PROJECT_NAME);
	public static final String ES_PROJECTS_NAME = "Projects";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"010projects\n" + 
			"|||\n" + 
			"Fundings###fundings\"";
	public final static String ID_CONVERTER_TYP= "pj";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Project() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty abstracts = new CsdlProperty().setName("abstract")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty budget = new CsdlProperty().setName("budget")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty coinvestigators = new CsdlProperty().setName("coinvestigators")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty acronym = new CsdlProperty().setName("acronym")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty endDate = new CsdlProperty().setName("expdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty keywords = new CsdlProperty().setName("keywords")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty principalinvestigator = new CsdlProperty().setName("principalinvestigator")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty projectarea = new CsdlProperty().setName("projectarea")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty startDate = new CsdlProperty().setName("startdate")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty status = new CsdlProperty().setName("status")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty researchprofile = new CsdlProperty().setName("researchprofile")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty createdate = new CsdlProperty().setName("createdate")
				.setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName());
		//The following properties are used for holding authority Keys to other entities
		CsdlProperty pj2rp = new CsdlProperty().setName("pj2rp")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty pj2ou = new CsdlProperty().setName("pj2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	

		//complex type
		CsdlProperty funding = new CsdlProperty().setName("Funding").setType(Funding.CT_FUNDING_FQN).setCollection(true);
		CsdlProperty partnership = new CsdlProperty().setName("Partnership").setType(Partnership.CT_PARTNERSHIP_FQN).setCollection(true);

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PROJECT_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, title, abstracts, principalinvestigator, coinvestigators, budget, startDate, endDate, projectarea,acronym,keywords,status, url,funding, partnership, researchprofile, createdate, pj2rp, pj2ou));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PROJECTS_NAME);
		entitySet.setType(ET_PROJECT_FQN);
		
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("abstract", "crisproject.abstract");
		mapping.put("budget", "crisproject.budget");
		mapping.put("acronym", "crisproject.acronym");
		mapping.put("coinvestigators", "crisproject.coinvestigators");
		mapping.put("expdate", "crisproject.expdate");
		mapping.put("keywords", "crisproject.keywords");
		mapping.put("principalinvestigator", "crisproject.principalinvestigator");
		mapping.put("projectarea", "crisproject.projectArea");
		mapping.put("researchprofile", "crisproject.researchProfileUniBa");
		mapping.put("startdate", "crisproject.startdate");
		mapping.put("status", "crisproject.status");
		mapping.put("title", "crisproject.title");
		mapping.put("url", "crisproject.projectURL");
		mapping.put("createdate", "crisproject.time_creation_dt"); //Creation-Time of Entity
		
		mapping.put("pj2rp", "projectinvestigators_authority");
		mapping.put("pj2ou", "crisproject.deptproject_authority");

	}
	
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_PROJECT_FQN;
	}

	public String getEntitySetName() {
		return ES_PROJECTS_NAME;
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
		if(sourceType.equals("Researchers")) {
			navigationFilter = ("crisproject.principalinvestigator_authority:\""+ id+"\"");
			navigationFilter = (navigationFilter+ "OR ");
			navigationFilter = (navigationFilter+ "crisproject.coinvestigators_authority:\""+ id+"\"");
			
		} else if(sourceType.equals("Orgunits")) {
			navigationFilter = ("crisproject.deptproject_authority:\""+ id+"\"");
		} else if(sourceType.equals("Projects")) {
			navigationFilter = ("crisproject.parentproject_authority:\""+ id+"\"");
		} else if(sourceType.equals("Orgunits_CHILD")) {
			/* special function: returns all projects which belong to the specified ou and all children ou's and their projects. Use some special field being indexed in Dspace.*/
			navigationFilter = ("projectchildoforgunits:\""+ id+"\"");
		}
			return navigationFilter;
	}

	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
