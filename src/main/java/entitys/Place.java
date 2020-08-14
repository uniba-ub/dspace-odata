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
 * Places. 
 * */
public class Place implements EntityModel {

	public final static String NAMESPACE = "profkat";
	
	public static final String ET_PLACE_NAME = "Place";
	public static final FullQualifiedName ET_PLACE_FQN = new FullQualifiedName(NAMESPACE, ET_PLACE_NAME);
	public static final String ES_PLACES_NAME = "Places";
	public final static String RECOURCE_TYPE_FILTER= "resourcetype_filter:\"002place\n|||\nPlaces###place\"";
	public final static String ID_CONVERTER_TYP= "place";
	private CsdlEntityType entityType;
	private CsdlEntitySet entitySet;
	private HashMap<String, String> mapping;

	
	
	public Place() {
		
		CsdlProperty id = new CsdlProperty().setName("id")
				.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		CsdlProperty crisId = new CsdlProperty().setName("cris-id")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty name = new CsdlProperty().setName("name")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty gnd = new CsdlProperty().setName("gnd")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty geonames = new CsdlProperty().setName("geonames")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty country = new CsdlProperty().setName("country")
				.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
		CsdlProperty predecessor = new CsdlProperty().setName("predecessor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty area = new CsdlProperty().setName("area")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		CsdlProperty successor  = new CsdlProperty().setName("successor")
				.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		
		//complex type
		//TODO:
		
		CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		propertyRef.setName("id");

		entityType = new CsdlEntityType();
		entityType.setName(ET_PLACE_NAME);
		entityType.setProperties(Arrays.asList(id,crisId, name, gnd, geonames, country, area, predecessor, successor));
		entityType.setKey(Arrays.asList(propertyRef));
		
		entitySet = new CsdlEntitySet();
		entitySet.setName(ES_PLACES_NAME);
		entitySet.setType(ET_PLACE_FQN);
		
		

		mapping = new HashMap<String, String>();
		//TODO: check Mapping for places etc... Not all might be indexed
		mapping.put("cris-id", "cris-id");
		mapping.put("name", "crisdo.name");
		mapping.put("gnd", "crisplace.placegnd");
		mapping.put("geonames", "crisplace.placegeonames");
		mapping.put("country", "crisplace.placecountry");
		mapping.put("area", "crisplace.placeadministrativeArea");
		mapping.put("predecessor", "crisplace.placepredecessor");
		mapping.put("successor", "crisplace.placesuccessor");
		mapping.put("geonames", "crisplace.placegeonames");
		mapping.put("gnd", "crisplace.placegnd");

		
	}
	
	@Override
	public CsdlEntityType getEntityType() {
		return entityType;
	}

	@Override
	public FullQualifiedName getFullQualifiedName() {
		return ET_PLACE_FQN;
	}

	@Override
	public String getEntitySetName() {
		return ES_PLACES_NAME;
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
		
		return navigationFilter;
	}

	@Override
	public HashMap<String, String> getMapping() {
		return mapping;
	}

}
