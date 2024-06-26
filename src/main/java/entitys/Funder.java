package entitys;

import java.util.*;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class Funder implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_FUNDER_NAME = "Funder";
	public static final FullQualifiedName ET_FUNDER_FQN = new FullQualifiedName(NAMESPACE, ET_FUNDER_NAME);
	public static final String ES_FUNDERS_NAME = "Funders";
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Funder\"";
	private final HashMap<String, String> idconverter;
	private final CsdlEntityType entityType;
	private final CsdlEntitySet entitySet;
	private final HashMap<String, List<String>> mapping;
	private final ArrayList<String> ENTITYFILTER;
	
	public Funder() {
		idconverter = new HashMap<>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(funder[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([1-9]{1}[0-9]{4})|([1]{1}[0-9]{5})", "handle"); //greater than 10.000 or 100.000
		idconverter.put("([0]{0,1}[0-9]{1,4})", "cris.legacyId"); //until funder09999 are considered as legcayvalues
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
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty country = new CsdlProperty().setName("country")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty city = new CsdlProperty().setName("city")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty wikidata = new CsdlProperty().setName("identifier_wikidata")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty isni = new CsdlProperty().setName("identifier_isni")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty grid = new CsdlProperty().setName("identifier_grid")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty ror = new CsdlProperty().setName("identifier_ror")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty crossrefid = new CsdlProperty().setName("identifier_crossreffunder")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gnd = new CsdlProperty().setName("identifier_gnd")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty type = new CsdlProperty().setName("type")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty acronym = new CsdlProperty().setName("acronym")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty parentfunder = new CsdlProperty().setName("funder2funder")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties		
		entityType = new CsdlEntityType();
		entityType.setName(ET_FUNDER_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, name,  country, city, url, description, wikidata, isni, grid, ror, crossrefid, gnd, type, acronym, parentfunder));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_FUNDERS_NAME);
		entitySet.setType(ET_FUNDER_FQN);
			
		mapping = new HashMap<>();
		mapping.put("cris-id", List.of("cris.legacyId"));
		mapping.put("uuid", List.of("search.resourceid"));
		mapping.put("handle", List.of("handle"));
		mapping.put("entitytype", List.of("search.entitytype"));
		mapping.put("name", List.of("dc.title"));

		mapping.put("country", List.of("crisfunder.iso-country"));
		mapping.put("city", List.of("crisfunder.city"));
		mapping.put("url", List.of("crisfunder.url"));
		mapping.put("description", List.of("crisfunder.description"));
		mapping.put("acronym", List.of("crisfunder.shortname"));
		mapping.put("type", List.of("crisfunder.type.cerif"));
		mapping.put("identifier_wikidata", List.of("crisfunder.identifier.wikidata"));
		mapping.put("identifier_isni", List.of("crisfunder.identifier.isni"));
		mapping.put("identifier_grid", List.of("crisfunder.identifier.grid"));
		mapping.put("identifier_gnd", List.of("crisfunder.identifier.gnd"));
		mapping.put("identifier_ror", List.of("crisfunder.identifier.ror"));
		mapping.put("identifier_crossreffunder", List.of("crisfunder.identifier.crossreffunder"));
		mapping.put("funder2funder", List.of("crisfunder.parentfunder_authority"));
		
		ENTITYFILTER = new ArrayList<>();
	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_FUNDER_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_FUNDERS_NAME;
	}

	@Override
	public CsdlEntitySet getEntitySet() {
		return entitySet;
	}

	@Override
	public String getRecourceTypeFilter() {
		return RECOURCE_TYPE_FILTER;
	}

	@Override
	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}
	
	public ArrayList<String> getEntityFilter() {
		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "funder";
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		return switch (sourceType) {
			case "Awardseries" -> ("crisawardseries.awardseriesinstitution_authority:\"" + id + "\"");
			case "Projects" -> ("crisproject.funding.fundingfunder_authority:\"" + id + "\"");
			case "Funders" -> ("crisfunder.funderparentfunder_authority:\"" + id + "\"");
			default -> "";
		};
	}

	@Override
	public HashMap<String, List<String>> getMapping() {
		return mapping;
	}

}
