package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Person implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_PERSON_NAME = "Person";
	public static final FullQualifiedName ET_PERSON_FQN = new FullQualifiedName(NAMESPACE, ET_PERSON_NAME);
	public static final String ES_PERSONS_NAME = "Persons";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Person\"";

	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;

	private final HashMap<String, String> idconverter;

	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	
	public Person() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(rp[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([6-9]{1}[0-9]{4})|([0-9]{6})", "handle"); //greater than 60.000 or grater than 100.000
		idconverter.put("([0-3]{0,1}[0-9]{1,4})", "cris.legacyId"); //rp until rp30000 are considered as legacyvalues
		idconverter.put("(uniba/[0-9]{1,6})", "handle");
		idconverter.put("(([0-9]{4})-([0-9]{4})-([0-9]{4})-([0-9]{4})){1}", "person.identifier.orcid");

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

		CsdlProperty biography = new CsdlProperty().setName("biography")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty country = new CsdlProperty().setName("country")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty nameOfficial = new CsdlProperty().setName("nameofficial")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty nameTranslated = new CsdlProperty().setName("nametranslated")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty nameVariant = new CsdlProperty().setName("namevariant")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty mainaffiliation = new CsdlProperty().setName("mainaffiliation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty researchinterests = new CsdlProperty().setName("researchinterests")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty website = new CsdlProperty().setName("website")
			.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		CsdlProperty email = new CsdlProperty().setName("email")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty orcid = new CsdlProperty().setName("orcid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		CsdlProperty rp2ou = new CsdlProperty().setName("rp2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");
		
		//complex type
		CsdlProperty affiliation = new CsdlProperty().setName("Affiliation").setType(Affiliation.CT_AFFILIATION_FQN).setCollection(true);
		CsdlProperty education = new CsdlProperty().setName("Education").setType(Education.CT_EDUCATION_FQN).setCollection(true);
		CsdlProperty qualification = new CsdlProperty().setName("Qualification").setType(Qualification.CT_QUALIFICATION_FQN).setCollection(true);
		
		// configuration of the Entity Type and adding of properties
		entityType = new CsdlEntityType();
		entityType.setName(ET_PERSON_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, name,
			nameOfficial, nameTranslated, nameVariant,
			description, email, biography, researchinterests, website, orcid, mainaffiliation, country,
			affiliation, qualification, education,
			rp2ou));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PERSONS_NAME);
		entitySet.setType(ET_PERSON_FQN);
			
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("nameofficial", List.of("crisrp.name"));
		mapping.put("nametranslated", List.of("crisrp.name.translated"));
		mapping.put("namevariant", List.of("crisrp.name.variant"));

		mapping.put("biography", List.of("dc.description.abstract"));
		mapping.put("country", List.of("crisrp.country"));
		mapping.put("mainaffiliation", List.of("person.affiliation.name"));

		mapping.put("description", List.of("crisrp.description"));
		mapping.put("website", List.of("oairecerif.identifier.url"));
		mapping.put("researchinterests", List.of("dc.subject"));
		mapping.put("email", List.of("person.email"));
		mapping.put("orcid", List.of("person.identifier.orcid"));

		mapping.put("rp2ou", List.of("person.affiliation.name_authority"));
		
		ENTITYFILTER = new ArrayList<>();
	}
	
	public CsdlEntityType getEntityType() {	
		
		return entityType;
	}
	
	public FullQualifiedName getFullQualifiedName() {
		return ET_PERSON_FQN;
	}

	public String getEntitySetName() {
		return ES_PERSONS_NAME;
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
		if (sourceType.equals("Orgunits")) {
			return ("person.affiliation.name_authority:\"" + id + "\"");
		}
		return "";
	}

	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

	/***
	 * Non-discoverable Person will return some limited mapping of fields.
	 * @return hashmap with mapping
	 */
	@Override
	public HashMap<String, List<String>> getNonDiscoverableMapping() {
		HashMap<String, List<String>> mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));
		mapping.put("displayname", List.of("dc.title"));

		mapping.put("orcid", List.of("person.identifier.orcid"));
		return mapping;
	}

	/***
	 * Non-discoverable Person will have no complex entities.
	 * @return boolean
	 */
	@Override
	public boolean hasNonDiscoverableComplexProperties(){
		return false;
	}

}
	
