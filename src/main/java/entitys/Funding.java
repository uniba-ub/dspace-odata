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

public class Funding implements EntityModel{

	public final static String NAMESPACE = "dspace";

	public static final String ET_FUNDING_NAME = "Funding";
	public static final FullQualifiedName ET_FUNDING_FQN = new FullQualifiedName(NAMESPACE, ET_FUNDING_NAME);
	public static final String ES_FUNDINGS_NAME = "Fundings";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Funding\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;

	public Funding() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(pj[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([1-9]{1}[0-9]{4})|([1]{1}[0-9]{5})", "handle"); //greater than 10.000 or greater than 100.000
		idconverter.put("([0]{0,1}[0-9]{1,4})", "cris.legacyId"); //until pj09999 are considered as legcayvalues
		idconverter.put("(uniba/[0-9]{1,6})", "handle");

		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty uuid = new CsdlProperty().setName("uuid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty entitytype = new CsdlProperty().setName("entitytype")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty handle = new CsdlProperty().setName("handle")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
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

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_FUNDING_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, name,
			url, researchprofile, potentialfield, createdate, pj2rp, pj2ou));
		entityType.setKey(List.of(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_FUNDINGS_NAME);
		entitySet.setType(ET_FUNDING_FQN);
		
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("url", List.of("crispj.projectURL"));
		mapping.put("potentialfield", List.of("crispj.deptproject"));
		mapping.put("researchprofile", List.of("crispj.deptproject"));
		mapping.put("createdate", List.of("dc.date.accessioned_dt")); //Creation-Time of Entity
		
		mapping.put("pj2rp", List.of("projectinvestigators_authority"));
		mapping.put("pj2ou", List.of("crispj.deptproject_authority"));

		ENTITYFILTER = new ArrayList<>();
	}
	
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	public FullQualifiedName getFullQualifiedName() {
		return ET_FUNDING_FQN;
	}

	public String getEntitySetName() {
		return ES_FUNDINGS_NAME;
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
			case "Persons" -> ("crispj.principalinvestigator_authority:\"" + id +
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
