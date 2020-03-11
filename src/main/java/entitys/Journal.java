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

public class Journal implements EntityModel {

	
	public final static String NAMESPACE = "dspace";
	
	public static final String ET_JOURNAL_NAME = "Journal";
	public static final FullQualifiedName ET_JOURNAL_FQN = new FullQualifiedName(NAMESPACE, ET_JOURNAL_NAME);
	public static final String ES_JOURNALS_NAME = "Journals";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"journals\n|||\njournals###crisjournals\"";
	public final static String ID_CONVERTER_TYP= "journals";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;
	
	public Journal() {
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("journalsname")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty issn = new CsdlProperty().setName("journalsissn")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty abbrevation = new CsdlProperty().setName("journalsabbreviation")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty homepage = new CsdlProperty().setName("journalshomepage")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty description = new CsdlProperty().setName("journalsdescription")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		// creation of PropertyRef for the key Element

		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		// configuration of the Entity Type and adding of properties
		
		entityType = new CsdlEntityType();
		entityType.setName(ET_JOURNAL_NAME);
		entityType.setProperties(Arrays.asList(id, crisId, name, issn, abbrevation, homepage, description));
		entityType.setKey(Collections.singletonList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_JOURNALS_NAME);
		entitySet.setType(ET_JOURNAL_FQN);
			
		mapping = new HashMap<String, String>();
		mapping.put("cris-id", "cris-id");
		mapping.put("journalsname", "crisjournals.journalsname");
		mapping.put("journalsissn", "crisjournals.journalsissn");
		mapping.put("journalsabbreviation", "crisjournals.journalsabbreviation");
		mapping.put("journalshomepage", "crisjournals.journalshomepage");
		mapping.put("journalsdescription", "crisjournals.journalsdescription");
		
	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_JOURNAL_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_JOURNALS_NAME;
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
		return null;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
