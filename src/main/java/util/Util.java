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

}
