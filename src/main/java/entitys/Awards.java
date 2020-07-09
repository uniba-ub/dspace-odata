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

public class Awards implements EntityModel {
	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_AWARD_NAME = "Award";
	public static final FullQualifiedName ET_AWARD_FQN = new FullQualifiedName(NAMESPACE, ET_AWARD_NAME);
	public static final String ES_AWARDS_NAME = "Awards";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"awards\n|||\nawards###crisawards\"";
	public final static String ID_CONVERTER_TYP= "awards";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Awards() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty awardseries = new CsdlProperty().setName("awardseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty category = new CsdlProperty().setName("category")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("description")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty person = new CsdlProperty().setName("person")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publication = new CsdlProperty().setName("publication")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty url = new CsdlProperty().setName("url")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty year = new CsdlProperty().setName("year")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		//The following properties are used for holding authority Keys to other entities
		CsdlProperty award2awardseries = new CsdlProperty().setName("award2awardseries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty award2rp = new CsdlProperty().setName("award2rp")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties		
		entityType = new CsdlEntityType();
		entityType.setName(ET_AWARD_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, awardseries, category, description, person, publication, name, url, year, award2rp, award2awardseries));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_AWARDS_NAME);
		entitySet.setType(ET_AWARD_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("category", "crisawards.awardseries.category");
		mapping.put("description", "crisawards.awardsdescription");
		mapping.put("name", "crisawards.awardsname");
		mapping.put("url", "crisawards.awardsurl");
		mapping.put("year", "crisawards.awardsyear");
		mapping.put("person", "crisawards.awardsperson");
		mapping.put("publication", "crisawards.awardspublication");
		mapping.put("awardseries", "awardseries");
		mapping.put("publication", "crisawards.awardspublication");
		
		mapping.put("award2awardseries", "crisawards.awardseries_authority");
		mapping.put("award2rp", "crisawards.awardsperson_authority");
		//mapping.put("award2publ", "crisawards.awardsperson_authority");

	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_AWARD_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_AWARDS_NAME;
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
			navigationFilter = ("crisawards.awardseries_authority:\""+id+"\"");
		}
		if(sourceType.equals("Researchers")) {
			navigationFilter = ("crisawards.awardsperson_authority:\""+id+"\"");
		}	
		return navigationFilter;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
