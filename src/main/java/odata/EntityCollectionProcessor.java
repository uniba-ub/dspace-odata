package odata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
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
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.solr.client.solrj.SolrServerException;

import util.Util;
import data.DataHandler;
import service.CslService;
import service.QueryOptionService;

public class EntityCollectionProcessor implements org.apache.olingo.server.api.processor.EntityCollectionProcessor {

	private OData odata;
	private ServiceMetadata serviceMetadata;
	private DataHandler datahandler;
	private QueryOptionService queryOptionService;
	private CslService cslService;
	private String cslstyle;

	public EntityCollectionProcessor(DataHandler datahandler) {

		this.datahandler = datahandler;
		queryOptionService = new QueryOptionService(this.datahandler);
		cslService = new CslService();

	}

	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;

	}

	// Method is triggered when HTTP-GET Request on Collection
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		EdmEntitySet responseEdmEntitySet = null;
		EntityCollection responseEntityCollection = null;
		UriResourceEntitySet uriResourceEntitySet = null;
		// get the requested EntitySet from the uriInfo object
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();

		UriResource firstUriResourceSegment = resourceParts.get(0);
		if(firstUriResourceSegment instanceof UriResourceEntitySet) {
			uriResourceEntitySet = (UriResourceEntitySet) firstUriResourceSegment;
		}
		else if(firstUriResourceSegment instanceof UriResourceFunction) {
			final UriResourceFunction uriResourceFunction = (UriResourceFunction) firstUriResourceSegment;	
			datahandler.readFunctionImportCollection(uriResourceFunction, serviceMetadata);	
			
		}
		else {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
		}

		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

		// Creation of different Option which the Client might selected
		CountOption countOption = uriInfo.getCountOption();
		SkipOption skipOption = uriInfo.getSkipOption();
		TopOption topOption = uriInfo.getTopOption();
		SelectOption selectOption = uriInfo.getSelectOption();
		ExpandOption expandOption = uriInfo.getExpandOption();
		OrderByOption orderByOption = uriInfo.getOrderByOption();
		FilterOption filterOption = uriInfo.getFilterOption();
		EntityCollection entityCollection = new EntityCollection();

		if (segmentCount == 1) { 
			responseEdmEntitySet = startEdmEntitySet;

			// get the data from EntityDatabase for this requested EntitySetName and deliver
			// as EntitySet
			try {
				entityCollection = datahandler.readEntitySetData(startEdmEntitySet);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Entity> entityList = entityCollection.getEntities();
			responseEntityCollection = queryOptionService.applyCountOption(countOption, entityList);
			entityList = queryOptionService.applySkipOption(skipOption, entityList);
			entityList = queryOptionService.applyTopOption(topOption, entityList);
			entityList = queryOptionService.applyOrderByOption(orderByOption, entityList);
			entityList = queryOptionService.applyFilterOption(entityList, filterOption);

			for (Entity entity : entityList) {
				responseEntityCollection.getEntities().add(entity);
			}

		} else if (segmentCount == 2) { 
			UriResource lastSegment = resourceParts.get(1);
			if (lastSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) lastSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
				EdmEntityType targetEntityType = edmNavigationProperty.getType();
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

				List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
				Entity sourceEntity;
				try {
					sourceEntity = datahandler.readEntityData(startEdmEntitySet, keyPredicates);
					entityCollection = datahandler.getRelatedEntityCollection(sourceEntity, targetEntityType);

				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				List<Entity> entityList = entityCollection.getEntities();
				responseEntityCollection = queryOptionService.applyCountOption(countOption, entityList);
				entityList = queryOptionService.applySkipOption(skipOption, entityList);
				entityList = queryOptionService.applyTopOption(topOption, entityList);
				entityList = queryOptionService.applyOrderByOption(orderByOption, entityList);
				entityList = queryOptionService.applyFilterOption(entityList, filterOption);

				for (Entity entity : entityList) {
					responseEntityCollection.getEntities().add(entity);
				}
			}
		} else {
			throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ROOT);
		}
		
		try {
			responseEntityCollection = cslService.enhanceCollection(responseEntityCollection, "ieee");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 try {
			responseEdmEntitySet =
			 queryOptionService.applyExpandOptionOnCollection(expandOption,
			 responseEdmEntitySet, entityCollection);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 

		EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();
		String selectList = odata.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption,
				selectOption);
		ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList).build();
		final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().contextURL(contextUrl)
				.select(selectOption).expand(expandOption).id(id).count(countOption).build();
		//check if requestedResponseFormat is null, then use json
		if(uriInfo.getFormatOption()==null) {
			responseFormat = ContentType.APPLICATION_JSON;	
		}
		ODataSerializer serializer = odata.createSerializer(responseFormat);
		SerializerResult serializerResult = serializer.entityCollection(this.serviceMetadata, edmEntityType,
				responseEntityCollection, opts);

		InputStream serializedContent = serializerResult.getContent();
		// configuration of the response object
		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());

	}

}
