package odata;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.solr.client.solrj.SolrServerException;

import data.DataHandler;
import service.QueryOptionService;
import util.Util;

public class EntityProcessor implements org.apache.olingo.server.api.processor.EntityProcessor {

	private OData odata;
	private ServiceMetadata serviceMetadata;
	private DataHandler datahandler;
	private QueryOptionService queryOptionService;

	public EntityProcessor(DataHandler entityDatabase) {

		this.datahandler = entityDatabase;
		queryOptionService = new QueryOptionService(this.datahandler);
	}

	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	public void createEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		throw new ODataApplicationException("Create is not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ROOT);
	}

	public void deleteEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2)
			throws ODataApplicationException, ODataLibraryException {
		throw new ODataApplicationException("Delete is not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ROOT);

	}

	public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, SerializerException {
		EdmEntityType responseEdmEntityType = null;
		Entity responseEntity = null;
		EdmEntitySet responseEdmEntitySet = null;

		// Retrieve the requested Entity

		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();

		UriResource uriResource = resourceParts.get(0); // in our example, the first segment is the EntitySet
		if (!(uriResource instanceof UriResourceEntitySet)) {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}

		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

		// Analyzation of URI segments
		if (segmentCount == 1) {
			responseEdmEntityType = startEdmEntitySet.getEntityType();
			responseEdmEntitySet = startEdmEntitySet; // since we have only one segment

			// Retrieve the data from entityDatabase
			List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
			try {
				responseEntity = datahandler.readEntityData(startEdmEntitySet, keyPredicates);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
		else if (segmentCount == 2) {
			UriResource navSegment = resourceParts.get(1);
			if (navSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) navSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
				responseEdmEntityType = edmNavigationProperty.getType();
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

				List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
				Entity sourceEntity;
				try {
					sourceEntity = datahandler.readEntityData(startEdmEntitySet, keyPredicates);
					List<UriParameter> navKeyPredicates = uriResourceNavigation.getKeyPredicates();
					if (navKeyPredicates.isEmpty()) {
						responseEntity = datahandler.getRelatedEntity(sourceEntity, responseEdmEntityType);
					} else {
						responseEntity = datahandler.getRelatedEntity(sourceEntity, responseEdmEntityType,
								navKeyPredicates);
					}
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}else {
			throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ROOT);
		}

		if (responseEntity == null) {
			throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ROOT);
		}

		// Create System Query Options
		SelectOption selectOption = uriInfo.getSelectOption();
		ExpandOption expandOption = uriInfo.getExpandOption();

		try {
			responseEntity = queryOptionService.applyExpandOptionOnEntity(expandOption, responseEntity,
					responseEdmEntitySet);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ODataSerializer serializer = this.odata.createSerializer(responseFormat);

		EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();

		String selectList = odata.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption,
				selectOption);

		ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList)
				.suffix(Suffix.ENTITY).build();
		EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextUrl).select(selectOption)
				.expand(expandOption).build();
		SerializerResult serializerResult = serializer.entity(this.serviceMetadata, responseEdmEntityType,
				responseEntity, opts);

		// configuration of response object
		response.setContent(serializerResult.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	public void updateEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		throw new ODataApplicationException("U is not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
				Locale.ROOT);

	}

}
