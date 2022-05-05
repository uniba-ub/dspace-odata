package entitys;

import java.util.ArrayList;
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
	public final static String RECOURCE_TYPE_FILTER= "search.resourcetype:\"Item\" and search.entitytype:\"Award\"";
	private HashMap<String, String> idconverter;
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	private ArrayList<String> ENTITYFILTER;	
	
	public Awards() {
		idconverter = new HashMap<String, String>();
		idconverter.put("([a-z0-9\\-]{36})", "search.resourceid");
		idconverter.put("(awards[0-9]{1,6})", "cris.legacyId");
		idconverter.put("([1-9][0-9]{1,5})", "handle");
		idconverter.put("([0][0-9]{1,4})", "cris.legacyId"); //until awards09999 are considered as legcayvalues
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
		CsdlProperty persontitle = new CsdlProperty().setName("persontitle")
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
		CsdlProperty award2pj = new CsdlProperty().setName("award2pj")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		
		// creation of PropertyRef for the key Element
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties		
		entityType = new CsdlEntityType();
		entityType.setName(ET_AWARD_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, uuid, entitytype, handle, awardseries, category, description, person, persontitle, publication, name, url, year, award2rp, award2awardseries, award2pj));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_AWARDS_NAME);
		entitySet.setType(ET_AWARD_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris.legacyId");
		mapping.put("uuid", "search.resourceid");
		mapping.put("handle", "handle");
		mapping.put("entitytype", "search.entitytype");
		mapping.put("name", "dc.title");
		
		mapping.put("category", "cris.virtual.awardseriescategory");
		mapping.put("description", "crisaward.description");
		mapping.put("url", "crisaward.url");
		mapping.put("year", "crisaward.date");
		mapping.put("person", "crisaward.person");
		mapping.put("persontitle", "cris.virtual.awardpersontitle");
		mapping.put("publication", "crisaward.publication");
		mapping.put("awardseries", "crisaward.awardseries");
		
		mapping.put("award2awardseries", "crisaward.awardseries_authority");
		mapping.put("award2rp", "crisaward.person_authority");
		mapping.put("award2pj", "crisaward.project_authority");

		ENTITYFILTER = new ArrayList<String>();

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

	public HashMap<String, String> getIdConverter() {
		return idconverter;
	}

	@Override
	public String getNavigationFilter(String sourceType, String id) {
		String navigationFilter = "";
		if(sourceType.equals("Awardseries")) {
			navigationFilter = ("crisaward.awardseries_authority:\""+id+"\"");
		} else if(sourceType.equals("Projects")) {
			navigationFilter = ("crisaward.awardsproject_authority:\""+ id+"\"");
		} else
		if(sourceType.equals("Researchers")) {
			navigationFilter = ("crisaward.awardsperson_authority:\""+id+"\"");
		}	
		return navigationFilter;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

	@Override
	public ArrayList<String> getEntityFilter() {
		// TODO Auto-generated method stub0,

		return ENTITYFILTER;
	}

	@Override
	public String getLegacyPrefix() {
		return "awards";
	}

}
