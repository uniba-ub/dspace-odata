package util;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

import entitys.EntityModel;

public class Util {

	public static Entity findEntity(EdmEntityType edmEntityType, EntityCollection entitySet,
			List<UriParameter> keyParams) {

		List<Entity> entityList = entitySet.getEntities();
		for (Entity entity : entityList) {
			boolean foundEntity = entityMatchesAllKeys(edmEntityType, entity, keyParams);
			if (foundEntity) {
				return entity;
			}
		}

		return null;
	}

	public static boolean entityMatchesAllKeys(EdmEntityType edmEntityType, Entity rt_entity,
			List<UriParameter> keyParams) {
		for (final UriParameter key : keyParams) {
			String keyName = key.getName();
			String keyText = key.getText();
			EdmProperty edmKeyProperty = (EdmProperty) edmEntityType.getProperty(keyName);
			Boolean isNullable = edmKeyProperty.isNullable();
			Integer maxLength = edmKeyProperty.getMaxLength();
			Integer precision = edmKeyProperty.getPrecision();
			Boolean isUnicode = edmKeyProperty.isUnicode();
			Integer scale = edmKeyProperty.getScale();
			EdmType edmType = edmKeyProperty.getType();
			EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType) edmType;
			Object valueObject = rt_entity.getProperty(keyName).getValue();

			String valueAsString = null;
			try {
				valueAsString = edmPrimitiveType.valueToString(valueObject, isNullable, maxLength, precision, scale,
						isUnicode);
			} catch (EdmPrimitiveTypeException e) {
				return false;
			}

			if (valueAsString == null) {
				return false;
			}

			boolean matches = valueAsString.equals(keyText);
			if (matches) {
				continue;
			} else {
				return false;
			}
		}

		return true;
	}

	public static EdmEntitySet getNavigationTargetEntitySet(EdmEntitySet startEdmEntitySet,
			EdmNavigationProperty edmNavigationProperty) throws ODataApplicationException {

		EdmEntitySet navigationTargetEntitySet = null;

		String navPropName = edmNavigationProperty.getName();
		EdmBindingTarget edmBindingTarget = startEdmEntitySet.getRelatedBindingTarget(navPropName);
		if (edmBindingTarget == null) {
			throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ROOT);
		}

		if (edmBindingTarget instanceof EdmEntitySet) {
			navigationTargetEntitySet = (EdmEntitySet) edmBindingTarget;
		} else {
			throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ROOT);
		}

		return navigationTargetEntitySet;
	}

	public static String[] calculatereverseRelation(EntityModel sourceModel, EntityModel targetModel, Entity sourceEntity, String dspaceId,
			String mode) {
		//solr documents only save one pointer in a document (project -> researcher) referencing each other, not two
		//this method is used to get cris-id's to enable the establishment of "reverse" navigation paths
		//Definitions are actually here, not in the entitys models, because sourceEntity is already given and no third solr query is necessary
		String navigationFilter = "";
		String[] result = new String[1]; //use array for complexer querys; e.g. multiple Fields
		try {
		if(!mode.contentEquals("reverse")) {
			return null;
		}
		String sourceType = sourceModel.getEntitySetName();
		String targetType = targetModel.getEntitySetName();
		/*
		if(sourceType.contentEquals("Projects") && targetType.contentEquals("Researchers")) {
			String filterquery = sourceEntity.getProperty("pj2rp").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Projects") && targetType.contentEquals("Orgunits")) {
			String filterquery = sourceEntity.getProperty("pj2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");				
		}else if(sourceType.contentEquals("Publications") && targetType.contentEquals("Researchers")) {
			String filterquery = sourceEntity.getProperty("publ2rp").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Publications") && targetType.contentEquals("Projects")) {
			String filterquery = sourceEntity.getProperty("publ2pj").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");
		}else if(sourceType.contentEquals("Publications") && targetType.contentEquals("Orgunits")) {
			String filterquery = sourceEntity.getProperty("publ2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Publications") && targetType.contentEquals("Series")) {
			String filterquery = sourceEntity.getProperty("publ2series").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Publications") && targetType.contentEquals("Journals")) { //TODO: delete entry later, when journals isn't used
			String filterquery = sourceEntity.getProperty("publ2journals").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");				
		}else if(sourceType.contentEquals("Researchers") && targetType.contentEquals("Orgunits")) {
			String filterquery = sourceEntity.getProperty("rp2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}
		*/	
		//TODO: define reverse navigationPaths
		/*
		if(sourceType.contentEquals("") && targetType.contentEquals("Orgunits")) {
			String filterquery = sourceEntity.getProperty("pj2rp").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Orgunits") && targetType.contentEquals("Places")) {
			String filterquery = sourceEntity.getProperty("pj2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");				
		}else if(sourceType.contentEquals("Persons") && targetType.contentEquals("Places")) {
			String filterquery = sourceEntity.getProperty("publ2rp").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}*/
		if(sourceType.contentEquals("Corporations") && targetType.contentEquals("Places")) {
			String filterquery = sourceEntity.getProperty("ou2place").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}
		/*else if(sourceType.contentEquals("Corporations") && targetType.contentEquals("Persons")) {
			String filterquery = sourceEntity.getProperty("person2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}*/else if(sourceType.contentEquals("Persons") && targetType.contentEquals("Places")) {
			String filterquery = sourceEntity.getProperty("person2place").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}else if(sourceType.contentEquals("Persons") && targetType.contentEquals("Corporations")) {
			String filterquery = sourceEntity.getProperty("person2ou").getValue().toString();
			navigationFilter = reverseQueryGenerator(filterquery, "cris-id");			
		}
		
		}catch(Exception e) {
			e.printStackTrace();
			return null;

		}
		if(navigationFilter.contentEquals("")) {
			return null;
		}else {
		result[0] = navigationFilter;	
		return result;
		}
	}
	private static String reverseQueryGenerator(String values, String param) {
		String output = "";
		try {
		String[] split = values.split(",");
		int it = 0;
		while(it < (split.length -1)) {
			split[it] = split[it].trim();
			output += (param + ":\"" + split[it] +"\" OR ");	
			it++;
		} //No or for last query parameter
		split[split.length-1] = split[split.length-1].trim();
		output += (param + ":\"" + split[split.length-1] +"\" ");
		return output;
		}catch(Exception e) {
			return "";
		}
	}
	

}
