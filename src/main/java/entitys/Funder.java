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

public class Funder implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_FUNDER_NAME = "Funder";
	public static final FullQualifiedName ET_FUNDER_FQN = new FullQualifiedName(NAMESPACE, ET_FUNDER_NAME);
	public static final String ES_FUNDERS_NAME = "Funders";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"funder\n|||\nfunder###crisfunder\"";
	public final static String ID_CONVERTER_TYP= "funder";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Funder() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty crisuuid = new CsdlProperty().setName("uuid")
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
		CsdlProperty doi = new CsdlProperty().setName("identifier_doi")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gnd = new CsdlProperty().setName("identifier_gnd")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties		
		entityType = new CsdlEntityType();
		entityType.setName(ET_FUNDER_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, crisuuid, name,  country, city, url, description, wikidata, isni, grid, ror, doi, gnd));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_FUNDERS_NAME);
		entitySet.setType(ET_FUNDER_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("uuid", "cris-uuid");
		mapping.put("name", "crisfunder.fundername");
		mapping.put("country", "crisfunder.funderiso-country");
		mapping.put("city", "crisfunder.fundercity");
		mapping.put("url", "crisfunder.funderurl");
		mapping.put("description", "crisfunder.funderdescription");
		//TODO: type?
		mapping.put("identifier_wikidata", "crisfunder.funderidentifier_wikidata");
		mapping.put("identifier_isni", "crisfunder.funderidentifier_isni");
		mapping.put("identifier_grid", "crisfunder.funderidentifier_grid");
		mapping.put("identifier_gnd", "crisfunder.funderidentifier_gnd");
		mapping.put("identifier_ror", "crisfunder.funderidentifier_ror");
		mapping.put("identifier_doi", "crisfunder.funderidentifier_doi");
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
	public String getIDConverterTyp() {
		return ID_CONVERTER_TYP;
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		// TODO Auto-generated method stub
		String navigationFilter = "";
		if(sourceType.equals("Awardseries")) {
			navigationFilter = ("crisawardseries.awardseriesinstitution_authority:\""+id+"\"");
		} else if(sourceType.equals("Projects")) {
			navigationFilter = ("crisproject.funding.fundingfunder_authority:\""+ id+"\"");
		}
		return navigationFilter;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
