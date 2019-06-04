package data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Linked;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.processor.ComplexProcessor;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import entitys.ComplexModel;
import entitys.EntityModel;
import entitys.EntityRegister;
import odata.EdmProviderDSpace;
import service.IdConverter;
import service.SolrQueryMaker;
import util.Util;

public class DataHandler {

	private EntityRegister entityRegister;
	private List<Property> propertyList;
	private SolrConnector solr;
	private SolrQueryMaker queryMaker;
	private IdConverter converter;
	private List<Property> complexProperties;

	public DataHandler() {

		entityRegister = new EntityRegister();
		solr = new SolrConnector();
		queryMaker = new SolrQueryMaker();
		converter = new IdConverter();

	}

	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		SolrDocumentList responseDocuments;
		for (EntityModel item : entityRegister.getEntityList()) {
			if (edmEntitySet.getName().equals(item.getEntitySetName())) {
				List<UriParameter> keyParams = null;
				boolean isEntityCollection=true;
				List<String> filterList = new LinkedList<String>();
				responseDocuments = getQuerriedDataFromSolr(item.getEntitySetName(), keyParams, isEntityCollection, filterList);
				entitySet = createEntitySet(responseDocuments, item);
			}
		}
		return entitySet;
	}

	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams)
			throws SolrServerException, IOException {
		Entity entity = null;
		EntityCollection entitySet;
		SolrDocumentList responseDocuments;
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		for (EntityModel item : entityRegister.getEntityList()) {
			if (edmEntityType.getName().equals(item.getEntityType().getName())) {
				boolean isEntityCollection = false;
				List<String> filterList = new LinkedList<String>();
				responseDocuments = getQuerriedDataFromSolr(item.getEntitySetName(), keyParams, isEntityCollection, filterList);
				entitySet = createEntitySet(responseDocuments, item);
				entity = entitySet.getEntities().get(0);
				}

		}

		return entity;
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams, boolean isEntityCollection, List<String> filterList) throws SolrServerException, IOException {
		for(EntityModel item: entityRegister.getEntityList()) {
			if(item.getEntitySetName().equals(entitySetName)) {
				if(isEntityCollection) {
					queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
					if(!filterList.isEmpty()) {
						for(String filter: filterList) {
							queryMaker.addSearchFilter(filter);	
						}
					}
				queryMaker.setResponseLimitToMax();
				}
				else {
					queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
					String id = keyParams.get(0).getText();				
					String dspaceId = converter.convertODataIDToDSpaceID(id, item.getIDConverterTyp());
					queryMaker.addSearchFilterForAttribute(converter.getIDSolrFilter(item.getIDConverterTyp()), dspaceId);
				}
			}		
		}		

		SolrDocumentList responseDocuments = solr.getData(queryMaker);
		queryMaker.resetQuery();
		return responseDocuments;
	}

	public EntityCollection createEntitySet(SolrDocumentList documentList, EntityModel entity) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		for (SolrDocument solrDocument : documentList) {
			entitySet.getEntities()
					.add(createEntity(createPropertyList(solrDocument, entity), entity.getEntitySetName()));
		}
		return entitySet;
	}

	public Entity createEntity(List<Property> propertyList, String entitySetName) {
		Entity entity = new Entity();
		String type = (EdmProviderDSpace.NAMESPACE + "." + entitySetName.replaceAll("s$", ""));
		for (Property item : propertyList) {
			entity.addProperty(item);
		}
		entity.setType(type);
		entity.setId(createId(entity, "id"));
		return entity;
	}

	public List<Property> createPropertyList(SolrDocument solrDocument, EntityModel entity) throws SolrServerException, IOException {
		propertyList = new LinkedList<Property>();
		Property property;
		HashMap<String, String> mapping = entity.getMapping();
		StringBuilder builder = new StringBuilder();
		complexProperties = new LinkedList<Property>();
		String itemType;
		for(CsdlProperty item: entity.getEntityType().getProperties()) {
			if(item.getName().equals("id")) {
				if(entity.getEntityType().getName().toString().equals("Publication")) {
					String currentId = (String) solrDocument.getFieldValue("handle");
					int convertedId = converter.convertHandleToId(currentId);
					property = new Property(null, "id", ValueType.PRIMITIVE, convertedId);
					propertyList.add(property);
				} else {
					String currentId = (String) solrDocument.getFieldValue("cris-id");
					int convertedId = converter.convertCrisToId(currentId);
					property = new Property(null, "id", ValueType.PRIMITIVE, convertedId);
					propertyList.add(property);	
				}
			
			} else {
				itemType = item.getTypeAsFQNObject().getName();
				if(solrDocument.getFieldValue(mapping.get(item.getName()))!=null) {
					if(itemType.equals("String")) {
						for(Object value: solrDocument.getFieldValues(mapping.get(item.getName()))) {
							if(builder.toString().length()!=0) {
								if(item.getName().equals("author")) {
									builder.append("; ");
								} else {
									builder.append(", ");
								}			
							}
							builder.append(value.toString());
						} 
						property = new Property(null, item.getName(), ValueType.PRIMITIVE, builder.toString());
						propertyList.add(property);
						builder = new StringBuilder();
					} else if(itemType.equals("Int32")|itemType.equals("Int16")| itemType.equals("Boolean")) {
						property = new Property(null, item.getName(), ValueType.PRIMITIVE, solrDocument.getFirstValue(item.getName()));
						propertyList.add(property);
					} 
				} else if(entityRegister.getComplexTypeNameList().contains(itemType)){
					int idOfSolrObject = (Integer) solrDocument.getFieldValue("search.resourceid");
					for (ComplexModel complexProperty:entityRegister.getComplexProperties()) {
						if(complexProperty.getName().equals(item.getName())) {
							loadComplexPropertyFromSolr(complexProperty, idOfSolrObject, propertyList);
						}
					}
				}
			}	
		}	

		return propertyList;
	}

	private void loadComplexPropertyFromSolr(ComplexModel complexProperty, int idOfSolrObject, List<Property> propertyList) throws SolrServerException, IOException {
		queryMaker.setQuerySearchTerm(complexProperty.getRecourceTypeFilter());
		queryMaker.setSearchFilterForComplexProperty(idOfSolrObject, complexProperty.getParentFK());
		queryMaker.setResponseLimitToMax();
		SolrDocumentList responseDocumentsForComplexProperty = solr.getData(queryMaker);
		HashMap<String, String> mapping = complexProperty.getMapping();
		for(SolrDocument solrDocument: responseDocumentsForComplexProperty) {
			ComplexValue complexvalue = new ComplexValue();
			List <Property> complexSubProperties = complexvalue.getValue();
			for(CsdlProperty item: complexProperty.getComplexType().getProperties()) {
				Property complexSubProperty = new Property(null, item.getName(), ValueType.PRIMITIVE, solrDocument.getFirstValue(mapping.get(item.getName())));
				complexSubProperties.add(complexSubProperty);
			}
			Property propertyComplex = new Property(null, complexProperty.getName(), ValueType.COMPLEX, complexvalue);
			propertyList.add(propertyComplex);
		}
		queryMaker.resetQuery();
	}

	private URI createId(Entity entity, String idPropertyName, String navigationName) {
		try {
			StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");
			final Property property = entity.getProperty(idPropertyName);
			sb.append(property.asPrimitive()).append(")");
			if (navigationName != null) {
				sb.append("/").append(navigationName);
			}
			return new URI(sb.toString());
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
		}
	}

	private URI createId(Entity entity, String idPropertyName) {
		return createId(entity, idPropertyName, null);
	}

	private String getEntitySetName(Entity entity) {
		String result = new String();
		for (CsdlEntitySet item : entityRegister.getEntitySet()) {
			if (item.getTypeFQN().getFullQualifiedNameAsString().equals(entity.getType())) {
				result = item.getName();
			}
		}
		return result;
	}
	
	// Navigation from EntityCollection to corresponding Entity
	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) throws SolrServerException, IOException {
		EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType);
		if (collection.getEntities().isEmpty()) {
			return null;
		}
		return collection.getEntities().get(0);
	}

	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) throws SolrServerException, IOException {

		EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType);
		return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
	}

	public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) throws SolrServerException, IOException {
		EntityCollection navigationTargetEntityCollection = new EntityCollection();

			// get ID from Entitiy Source
			String entityID = sourceEntity.getProperty("id").getValue().toString();
			List<UriParameter> keyParams = null;
			SolrDocumentList responseDocuments;
			boolean isEntityCollection = true;
			List<String> filterList = new LinkedList<String>();
			EntityModel sourceModel = null;
			EntityModel targetModel = null;
			for(EntityModel item: entityRegister.getEntityList()) {
				if(item.getFullQualifiedName().getFullQualifiedNameAsString().equals(sourceEntity.getType())){					
					sourceModel = item;
				}		
				if(item.getFullQualifiedName().equals(targetEntityType.getFullQualifiedName())) {
					targetModel= item;	
				}
			}
			
			String dspaceId = converter.convertODataIDToDSpaceID(entityID, sourceModel.getIDConverterTyp());
			queryMaker.addSearchFilter((targetModel.getNavigationFilter(sourceModel.getEntitySetName(), dspaceId)));
			responseDocuments = getQuerriedDataFromSolr(targetModel.getEntitySetName(), keyParams, isEntityCollection, filterList);	
			navigationTargetEntityCollection = createEntitySet(responseDocuments, targetModel);
			
		if (navigationTargetEntityCollection.getEntities().isEmpty()) {
			return null;
		}
		return navigationTargetEntityCollection;
	}
	
}
