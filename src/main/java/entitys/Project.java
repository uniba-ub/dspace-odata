package entitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Project\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;
	
	public Project() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(pj[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([1-9][0-9]{1,5})", "handle");
		idconverter.put("([0][0-9]{1,4})", "cris.legacyId"); //until pj09999 are considered as legcayvalues
		idconverter.put("(uniba/[0-9]{1,6})", "handle");

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
		CsdlProperty abstracts = new CsdlProperty().setName("abstract")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty budget = new CsdlProperty().setName("budget")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());		
		CsdlProperty dept = new CsdlProperty().setName("dept")
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
		CsdlProperty potentialfield = new CsdlProperty().setName("potentialfield")
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
		entityType.setProperties(Arrays.asList(id,crisId,uuid, entitytype, handle, title, abstracts, principalinvestigator, coinvestigators, budget, startDate, endDate, projectarea,acronym,keywords,status, url,funding, partnership, researchprofile, potentialfield, createdate, dept, pj2rp, pj2ou));
		entityType.setKey(List.of(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PROJECTS_NAME);
		entitySet.setType(ET_PROJECT_FQN);
		
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("abstract", List.of("crispj.abstract"));
		mapping.put("budget", List.of("crispj.budget"));
		mapping.put("acronym", List.of("crispj.acronym"));
		mapping.put("coinvestigators", List.of("crispj.coinvestigators"));
		mapping.put("expdate", List.of("crispj.expdate"));
		mapping.put("keywords", List.of("crispj.keywords"));
		mapping.put("principalinvestigator", List.of("crispj.principalinvestigator"));
		mapping.put("projectarea", List.of("crispj.projectArea"));
		mapping.put("researchprofile", List.of("crispj.researchprofileuniba"));
		mapping.put("potentialfield", List.of("crispj.potentialfield"));
		mapping.put("startdate", List.of("crispj.startdate"));
		mapping.put("status", List.of("crispj.status"));
		mapping.put("title", List.of("crispj.title"));
		mapping.put("url", List.of("crispj.projectURL"));
		mapping.put("dept", List.of("crispj.deptproject"));
		mapping.put("createdate", List.of("dc.date.accessioned_dt")); //Creation-Time of Entity
		
		mapping.put("pj2rp", List.of("projectinvestigators_authority"));
		mapping.put("pj2ou", List.of("crispj.deptproject_authority"));

		ENTITYFILTER = new ArrayList<>();
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

	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "pj";
	}

	public String getNavigationFilter(String sourceType, String id) {
		return switch (sourceType) {
			case "Researchers" -> ("crispj.principalinvestigator_authority:\"" + id +
				"\" OR crispj.coinvestigators_authority:\"" + id + "\"");
			case "Orgunits" -> ("crispj.deptproject_authority:\"" + id + "\"");
			case "Projects" -> ("crispj.parentproject_authority:\"" + id + "\"");
			case "Orgunits_CHILD" ->
				/* special function: returns all projects which belong to the specified ou and all children ou's and their projects. Use some special field being indexed in Dspace.*/
				("pjsubsuccorgunit_authority:\"" + id + "\"");
			default -> "";
		};
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
