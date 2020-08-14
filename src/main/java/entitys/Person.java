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
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty firstName = new CsdlProperty().setName("firstName")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty profession = new CsdlProperty().setName("profession")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty isProfessor = new CsdlProperty().setName("isProfessor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty category = new CsdlProperty().setName("category")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty denomination = new CsdlProperty().setName("denomination")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty links = new CsdlProperty().setName("links")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gender = new CsdlProperty().setName("gender")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty alternativeWritingOfSurname = new CsdlProperty().setName("alternativeWritingOfSurname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty pictureKey = new CsdlProperty().setName("pictureKey")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty title = new CsdlProperty().setName("title")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty employeKey = new CsdlProperty().setName("employeKey")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty dnb = new CsdlProperty().setName("dnb")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty furtherInformation = new CsdlProperty().setName("furtherInformation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty references = new CsdlProperty().setName("references")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());	
		CsdlProperty remarks = new CsdlProperty().setName("remarks")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty publications = new CsdlProperty().setName("Publications")
				.setType(Publications.CT_PUBLICATIONS_FQN).setCollection(true);
		CsdlProperty biography = new CsdlProperty().setName("Biography")
				.setType(Biography.CT_BIOGRAPHY_FQN).setCollection(true);
		CsdlProperty publicationlibaries = new CsdlProperty().setName("publicationlibaries")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty life = new CsdlProperty().setName("Life")
				.setType(Life.CT_LIFE_FQN).setCollection(true);
		CsdlProperty home = new CsdlProperty().setName("Home")
				.setType(Home.CT_HOME_FQN).setCollection(true);
		CsdlProperty parentinfamily = new CsdlProperty().setName("parentinfamily")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty childinfamily = new CsdlProperty().setName("childinfamily")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty adoptivechildinFamily = new CsdlProperty().setName("adoptivechildinFamily")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty study = new CsdlProperty().setName("Study")
				.setType(Study.CT_STUDY_FQN).setCollection(true);
		CsdlProperty graduation = new CsdlProperty().setName("Graduation")
				.setType(Graduation.CT_GRADUATION_FQN).setCollection(true);
		CsdlProperty periodOfLife = new CsdlProperty().setName("PeriodOfLife")
				.setType(PeriodOfLife.CT_PERIODOFLIFE_FQN).setCollection(true);
		CsdlProperty career = new CsdlProperty().setName("Career")
				.setType(Career.CT_CAREER_FQN).setCollection(true);
		CsdlProperty socialRole = new CsdlProperty().setName("SocialRole")
				.setType(SocialRole.CT_SOCIALROLE_FQN).setCollection(true);
		CsdlProperty membership = new CsdlProperty().setName("Membership")
				.setType(Membership.CT_MEMBERSHIP_FQN).setCollection(true);
		CsdlProperty awards = new CsdlProperty().setName("Awards")
				.setType(Awards.CT_AWARDS_FQN).setCollection(true);
		CsdlProperty birthyear = new CsdlProperty().setName("birthyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty deathyear = new CsdlProperty().setName("deathyear")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty person2ou = new CsdlProperty().setName("person2ou")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty person2place = new CsdlProperty().setName("person2place")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		

CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PERSON_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, title, name, firstName, profession, isProfessor, category, denomination, links, gender,alternativeWritingOfSurname,pictureKey, title,employeKey, dnb,
				furtherInformation, references, remarks, publications, biography, publicationlibaries, life, home, parentinfamily, childinfamily, adoptivechildinFamily, study, graduation, periodOfLife, career, socialRole, membership, awards, person2ou, person2place, deathyear, birthyear));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PERSONS_NAME);
		entitySet.setType(ET_PERSON_FQN);
		
		

		mapping = new HashMap<String, String>();
		
		mapping.put("cris-id", "cris-id");
		mapping.put("title", "crisperson.persontitle");
		mapping.put("displayName", "crisdo.name");
		mapping.put("name", "crisperson.personname");
		mapping.put("firstName", "crisperson.personfirstName");
		mapping.put("lastName", "crisperson.personname");	
		mapping.put("profession", "crisperson.personprofession");
		mapping.put("isProfessor", "crisperson.personisProfessor");
		mapping.put("category", "crisperson.personcategory");
		mapping.put("denomination", "crisperson.persondenomination");
		mapping.put("institutions", "crisperson.personinstitutions");
		mapping.put("datelife", "crisperson.persondatelife");
		mapping.put("placelife", "crisperson.personplacelife");
		mapping.put("links", "crisperson.personlinks");
		mapping.put("gender", "crisperson.persongender");
		mapping.put("alternativeWritingOfSurname", "crisperson.personalternativeWritingOfSurname");
		mapping.put("pictureKey", "crisperson.personpictureKey");
		mapping.put("title", "crisperson.persontitle");
		mapping.put("employeKey", "crisperson.personemployeKey");
		mapping.put("dnb", "crisperson.persondnb");
		mapping.put("furtherInformation", "crisperson.personfurtherInformation");
		mapping.put("references", "crisperson.personreferences");
		mapping.put("remarks", "crisperson.personremarks");
		mapping.put("publications", "crisperson.publications");
		mapping.put("biography", "crisperson.biography");
		mapping.put("publicationlibaries", "crisperson.publicationlibaries");
		mapping.put("life", "crisperson.life");
		mapping.put("home", "crisperson.home");
		mapping.put("parentinfamily", "crisperson.parentinfamily");
		mapping.put("childinfamily", "crisperson.childinfamily");
		mapping.put("adoptivechildinFamily", "crisperson.adoptivechildinFamily");
		mapping.put("study", "crisperson.study");
		mapping.put("graduation", "crisperson.graduation");
		mapping.put("periodOfLife", "crisperson.periodOfLife");
		mapping.put("career", "crisperson.career");
		mapping.put("socialRole", "crisperson.socialRole");
		mapping.put("membership", "crisperson.membership");
		mapping.put("awards", "crisperson.awards");
		mapping.put("birthyear", "crisperson.birthyear");
		mapping.put("deathyear", "crisperson.deathyear");
		mapping.put("person2ou", "personinstitutions_authority");
		mapping.put("person2place", "personplacelife_authority");
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
		if(sourceType.equals("Corporations")) {
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
