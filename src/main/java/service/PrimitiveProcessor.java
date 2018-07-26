package service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.PrimitiveSerializerOptions;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceProperty;

import data.TestDatabase;

public class PrimitiveProcessor implements org.apache.olingo.server.api.processor.PrimitiveProcessor {

	private OData odata;
	private TestDatabase entityDatabase;
	private ServiceMetadata serviceMetadata;

	public PrimitiveProcessor(TestDatabase entityDatabase) {
		this.entityDatabase = entityDatabase;
	}

	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;

	}

	public void deletePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo)
			throws ODataApplicationException {
		throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ROOT);
	}

	public void readPrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, SerializerException {

		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		UriResourceEntitySet uriEntityset = (UriResourceEntitySet) resourceParts.get(0);
		EdmEntitySet edmEntitySet = uriEntityset.getEntitySet();
		List<UriParameter> keyPredicates = uriEntityset.getKeyPredicates();

		UriResourceProperty uriProperty = (UriResourceProperty) resourceParts.get(resourceParts.size() - 1);
		EdmProperty edmProperty = uriProperty.getProperty();
		String edmPropertyName = edmProperty.getName();
		EdmPrimitiveType edmPropertyType = (EdmPrimitiveType) edmProperty.getType();

		// retrieve data from entitydatabase

		Entity entity = entityDatabase.readEntityData(edmEntitySet, keyPredicates);
		if (entity == null) { // Wrong request
			throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ENGLISH);
		}

		Property property = entity.getProperty(edmPropertyName);
		if (property == null) {
			throw new ODataApplicationException("Property not found", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ENGLISH);
		}

		Object value = property.getValue();
		if (value != null) {
			ODataSerializer serializer = odata.createSerializer(responseFormat);

			ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).navOrPropertyPath(edmPropertyName)
					.build();
			PrimitiveSerializerOptions options = PrimitiveSerializerOptions.with().contextURL(contextUrl).build();
			SerializerResult serializerResult = serializer.primitive(serviceMetadata, edmPropertyType, property,
					options);
			InputStream propertyStream = serializerResult.getContent();

			response.setContent(propertyStream);
			response.setStatusCode(HttpStatusCode.OK.getStatusCode());
			response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
		} else {
			response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
		}
	}

	public void updatePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType requestFormat, ContentType responseFormat)
			throws ODataApplicationException, DeserializerException, SerializerException {
		throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ROOT);
	}

}
