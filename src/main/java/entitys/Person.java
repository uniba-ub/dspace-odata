package entitys;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

/*
 * Allpersons. 
 * */
public class Person implements EntityModel {

	public final static String NAMESPACE = "profkat";
	
	public static final String ET_PERSON_NAME = "Person";
	public static final FullQualifiedName ET_PERSON_FQN = new FullQualifiedName(NAMESPACE, ET_PERSON_NAME);
	public static final String ES_PERSONS_NAME = "Persons";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"001allpersons\n|||\nAllPersons###allpersons\"";
	public final static String ID_CONVERTER_TYP= "person";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Person() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty displayName = new CsdlProperty().setName("displayName")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty firstname = new CsdlProperty().setName("firstName")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty lastname = new CsdlProperty().setName("lastName")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty isprofessor = new CsdlProperty().setName("isprofessor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gender = new CsdlProperty().setName("gender")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty category = new CsdlProperty().setName("category")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty denomination = new CsdlProperty().setName("denomination")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty institutions = new CsdlProperty().setName("institutions")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty datelife = new CsdlProperty().setName("datelife")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty placelife = new CsdlProperty().setName("placelife")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty links = new CsdlProperty().setName("links")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty dnb = new CsdlProperty().setName("dnb")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty profession = new CsdlProperty().setName("profession")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty furtherInfo = new CsdlProperty().setName("furtherInfo")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty birthyear = new CsdlProperty().setName("birthyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty deathyear = new CsdlProperty().setName("deathyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
											
		//complex type / aka nested entities
		//should all be indexed?
		//TODO:
		
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");
		

		entityType = new CsdlEntityType();
		entityType.setName(ET_PERSON_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, title, displayName, firstname, lastname, isprofessor, gender, category, denomination, institutions, datelife, placelife, links, dnb, furtherInfo, profession, birthyear, deathyear));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PERSONS_NAME);
		entitySet.setType(ET_PERSON_FQN);
		
		mapping = new HashMap<String, String>();
		//TODO: check Mapping for persons. Not all might be indexed
		mapping.put("cris-id", "cris-id");
		mapping.put("title", "crisperson.persontitle");
		mapping.put("displayName", "crisdo.name");
		mapping.put("firstName", "crisperson.personfirstName");
		mapping.put("lastName", "crisperson.personname");
		mapping.put("isprofessor", "crisperson.personisProfessor");
		mapping.put("gender", "crisperson.persongender");
		mapping.put("category", "crisperson.personcategory");
		mapping.put("denomination", "crisperson.persondenomination");
		mapping.put("institutions", "crisperson.personinstitutions");
		mapping.put("datelife", "crisperson.persondatelife");
		mapping.put("placelife", "crisperson.personplacelife");
		mapping.put("links", "crisperson.personlinks");
		mapping.put("dnb", "crisperson.persondnb");
		mapping.put("furtherInfo", "crisperson.personfurtherInformation");
		mapping.put("profession", "crisperson.personprofession");
		mapping.put("birthyear", "crisperson.birthyear");
		mapping.put("deathyear", "crisperson.deathyear");
		
	}
	
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_PERSON_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_PERSONS_NAME;
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
		String navigationFilter = "";
		if(sourceType.equals("Orgunits")) {
			navigationFilter = ("personinstitutions_authority:\""+id+"\"");
		}
		if(sourceType.equals("Places")) {
			navigationFilter = ("personplacelife_authority:\""+id+"\"");
		}
		return navigationFilter;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
