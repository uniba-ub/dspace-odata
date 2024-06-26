package data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import entitys.ComplexModel;
import entitys.EntityModel;
import entitys.EntityRegister;
import entitys.Product;
import entitys.Project;
import entitys.Publication;
import odata.EdmProviderDSpace;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.queryoption.SearchOption;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import service.IdConverter;
import service.SolrQueryMaker;
import util.Util;

public class DataHandler {

	private final EntityRegister entityRegister;
	private List<Property> propertyList;
	private final SolrConnector solr;
	private final SolrQueryMaker queryMaker;
	private final IdConverter converter;
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
				List<String> filterList = new LinkedList<>(item.getEntityFilter());
				responseDocuments = getQuerriedDataFromSolr(item.getEntitySetName(), keyParams, isEntityCollection, filterList);
				entitySet = createEntitySet(responseDocuments, item);
			}
		}
		return entitySet;
	}
	
	public EntityCollection searchEntitySetData(EdmEntitySet edmEntitySet, SearchOption search) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		SolrDocumentList responseDocuments;
		for (EntityModel item : entityRegister.getEntityList()) {
			if (edmEntitySet.getName().equals(item.getEntitySetName())) {
				List<UriParameter> keyParams = null;
				boolean isEntityCollection=true;
				List<String> filterList = new LinkedList<>(item.getEntityFilter());
				responseDocuments = getQuerriedDataFromSolr(item.getEntitySetName(), keyParams, isEntityCollection, filterList, search);
				entitySet = createEntitySet(responseDocuments, item);
			}
		}
		return entitySet;
	}

	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams)
			throws SolrServerException, IOException {
		return readEntityData(edmEntitySet, keyParams, false);
	}
	
	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, boolean ignoreprivacy)
			throws SolrServerException, IOException {
		Entity entity = null;
		EntityCollection entitySet;
		SolrDocumentList responseDocuments;
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		for (EntityModel item : entityRegister.getEntityList()) {
			if (edmEntityType.getName().equals(item.getEntityType().getName())) {
				boolean isEntityCollection = false;
				List<String> filterList = new LinkedList<>();
				responseDocuments = getQuerriedDataFromSolr(item.getEntitySetName(), keyParams, isEntityCollection, filterList);
				entitySet = createEntitySet(responseDocuments, item, ignoreprivacy);
					if (entitySet.getCount() > 0) {
						entity = entitySet.getEntities().get(0);
					}
				}

		}
		
		return entity;
	}
	
	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams, boolean isEntityCollection, List<String> filterList)
		throws SolrServerException, IOException {
		return getQuerriedDataFromSolr(entitySetName, keyParams, isEntityCollection, filterList, null);
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams, boolean isEntityCollection, List<String> filterList, SearchOption search)
		throws SolrServerException, IOException {
		return getQuerriedDataFromSolr(entitySetName, keyParams, isEntityCollection, filterList, search, false);
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams, boolean isEntityCollection, List<String> filterList, SearchOption search, boolean ignoreprivacy)
		throws SolrServerException, IOException {
		try {
		for (EntityModel item: entityRegister.getEntityList()) {
			if (item.getEntitySetName().equals(entitySetName)) {
				if (isEntityCollection) {
					//enable searchOption for entityset only.
					if (search != null) {
						queryMaker.setODataSearchTerm(search.getText());
						queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());

					} else {
					queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
					}
					if (!filterList.isEmpty()) {
						for (String filter: filterList) {
							queryMaker.addSearchFilter(filter);	
						}
					}
				queryMaker.setResponseLimitToMax();
				}
				else {
					queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
					if (!filterList.isEmpty()) {
						for (String filter: filterList) {
							queryMaker.addSearchFilter(filter);	
						}
					}
					String id = keyParams.get(0).getText();				
					String dspaceId = converter.convertODataIDToDSpaceID(id);
					String field = converter.getIdField(dspaceId,item.getIdConverter());
					dspaceId = converter.addIdentifierPrefix(dspaceId,  field, item.getLegacyPrefix());
					queryMaker.addSearchFilterForAttribute(field, dspaceId);
				}
			}		
		}

		if(!ignoreprivacy) {
			addReadableByAnonymousFilter();
		}

		SolrDocumentList responseDocuments = solr.getData(queryMaker);
		queryMaker.resetQuery();
		return responseDocuments;
		} catch (Exception e) {
			//reset query, if queryMaker is false or not found
			//e.printStackTrace();
			queryMaker.resetQuery();
			throw e;
		}
	}

	public EntityCollection createEntitySet(SolrDocumentList documentList, EntityModel entity) throws SolrServerException, IOException {
		return createEntitySet(documentList, entity, false);	
	}

	public EntityCollection createEntitySet(SolrDocumentList documentList, EntityModel entity, boolean ignoreprivacy) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		for (SolrDocument solrDocument : documentList) {
			if (solrDocument.getFirstValue("withdrawn").equals("false")) {
				entitySet.getEntities()
				.add(createEntity(createPropertyList(solrDocument, entity), entity.getEntitySetName(), false));
			} else if (ignoreprivacy) {
				//ignore privacystatus for entity set
				entitySet.getEntities()
				.add(createEntity(createPropertyList(solrDocument, entity), entity.getEntitySetName(), true));
			}
		}
		entitySet.setCount(entitySet.getEntities().size());
		return entitySet;
	}
	
	public Entity createEntity(List<Property> propertyList, String entitySetName) {
		return createEntity(propertyList, entitySetName, false);
	}
	
	public Entity createEntity(List<Property> propertyList, String entitySetName, boolean anonymized) {
		Entity entity = new Entity();
		String type = (EdmProviderDSpace.NAMESPACE + "." + entitySetName.replaceAll("s$", ""));
		if (!anonymized) {
			for (Property item : propertyList) {
				entity.addProperty(item);
			}
		} else {
			for (Property item : propertyList) {
				if (item.getName().contentEquals("id")) {
				entity.addProperty(item);
				break;
				}
			}
		}
		entity.setType(type);
		entity.setId(createId(entity, "id"));
		return entity;
	}

	public List<Property> createPropertyList(SolrDocument solrDocument, EntityModel entity) throws SolrServerException, IOException {
		propertyList = new LinkedList<>();
		Property property;
		HashMap<String, List<String>> mapping = entity.getMapping();
		if (solrDocument.getFieldValue("discoverable") != null && ((String) solrDocument.getFieldValue("discoverable")).contentEquals("false")) {
			// Check if there is some special mapping for non discoverable instances of this entityModel
			if (entity.getNonDiscoverableMapping() != null) {
				mapping = entity.getNonDiscoverableMapping();
			}
		}

		StringBuilder builder = new StringBuilder();
		String itemType;
		for (CsdlProperty item: entity.getEntityType().getProperties()) {
			if (item.getName().equals("id")) {
				String currentId = (String) solrDocument.getFieldValue("handle");
				int convertedId = converter.convertHandleToId(currentId);
				property = new Property(null, "id", ValueType.PRIMITIVE, convertedId);
				propertyList.add(property);
			
			} else {
				itemType = item.getTypeAsFQNObject().getName();
				if (mapping.get(item.getName()) != null){
					if (itemType.equals("String")) {
						for (String val : mapping.get(item.getName())) {
							if (solrDocument.getFieldValue(val) != null) {
								for (Object value : solrDocument.getFieldValues(val)) {
									if (builder.toString().length() != 0) {
										if (item.getName().equals("author") ||
											item.getName().equals("articlecollectionEditor") ||
											item.getName().contentEquals("editor") ||
											item.getName().contentEquals("creator") ||
											item.getName().contentEquals("contributor") ||
											item.getName().contentEquals("creatorcontributor")) {
											builder.append("; ");
										} else {
											builder.append(", ");
										}
									}
									builder.append(value.toString());
								}
							}
						}
						if (builder.toString().length() != 0) {
							property = new Property(null, item.getName(), ValueType.PRIMITIVE, builder.toString());
						} else {
							property = new Property(null, item.getName(), ValueType.PRIMITIVE, null);
						}
						propertyList.add(property);
						builder = new StringBuilder();
					} else if (itemType.equals("Int32")|itemType.equals("Int16")| itemType.equals("Boolean")) {
						property = new Property(null, item.getName(), ValueType.PRIMITIVE, solrDocument.getFirstValue(mapping.get(item.getName()).get(0)));
						propertyList.add(property);
					} else if (itemType.equals("DateTimeOffset") | itemType.contentEquals("DateTime")) {
						//transform from Solr-value to datetime
						try {
							DateFormat dateFormat = new SimpleDateFormat(
					            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
							Date date = dateFormat.parse((solrDocument.getFirstValue(mapping.get(item.getName()).get(0))).toString());
							property = new Property(null, item.getName(), ValueType.PRIMITIVE, date);
							propertyList.add(property);
						} catch(Exception e) {
							//
						}
					}
				} else if (entityRegister.getComplexTypeNameList().contains(itemType)){
					if (solrDocument.getFieldValue("discoverable") != null && ((String) solrDocument.getFieldValue("discoverable")).contentEquals("false")) {
						// Check if there is some special mapping for non discoverable instances of this entityModel
						if (entity.hasNonDiscoverableComplexProperties() == false) {
							continue;
						}
					}
					for (ComplexModel complexProperty : entityRegister.getComplexProperties()) {
						if (complexProperty.getName().contentEquals(item.getName())) {
							loadComplexPropertyFromMetadata(complexProperty, solrDocument, propertyList);
						}
					}
				}
			}	
		}	

		return propertyList;
	}

	private void loadComplexPropertyFromMetadata(ComplexModel complexProperty, SolrDocument solrDocument, List<Property> propertyList) throws IOException {

		HashMap<String, String> mapping = complexProperty.getMapping();
		List<ComplexValue> complexValueList = new LinkedList<ComplexValue>();


			// Loop through results in metadata fields list and ignore Placeholder Values
			// Assume all lists have the same length. Otherwise the size of every property must be checked.
			CsdlProperty item = complexProperty.getComplexType().getProperties().get(0);
			if (item == null) return;
			if (solrDocument.getFieldValues(mapping.get(item.getName())) == null) return;
			//the "parent" field being iterated
			for (int i = 0; i < solrDocument.getFieldValues(mapping.get(item.getName())).size(); i++) {
				ComplexValue complexvalue = new ComplexValue();
				List <Property> complexSubProperties = complexvalue.getValue();
				List<String> vals = solrDocument.getFieldValues(mapping.get(item.getName())).stream()
						   .map(object -> Objects.toString(object, null))
						   .collect(Collectors.toList());
				String val = vals.get(i);
				if (val == null) continue;
				if (val.equalsIgnoreCase("#PLACEHOLDER_PARENT_METADATA_VALUE#")) val = null;
				Property complexSubProperty = new Property(null, item.getName(), ValueType.PRIMITIVE, val);
				complexSubProperties.add(complexSubProperty);

				//loop through all other values on this position
				for (CsdlProperty otheritem : complexProperty.getComplexType().getProperties().subList( 1, complexProperty.getComplexType().getProperties().size())) {
					if (solrDocument.getFirstValue(mapping.get(otheritem.getName()))== null){
					Property othercomplexSubProperty = new Property(null, otheritem.getName(), ValueType.PRIMITIVE, null);
					complexSubProperties.add(othercomplexSubProperty);
					} else {
					List<String> othervals = solrDocument.getFieldValues(mapping.get(otheritem.getName())).stream()
							   .map(object -> Objects.toString(object, null))
							   .collect(Collectors.toList());
					String otherval = othervals.get(i);
					if (otherval == null) continue;
					if (otherval.equalsIgnoreCase("#PLACEHOLDER_PARENT_METADATA_VALUE#")) otherval = null;
					Property othercomplexSubProperty = new Property(null, otheritem.getName(), ValueType.PRIMITIVE, otherval);
					complexSubProperties.add(othercomplexSubProperty);
					}

				}
				complexValueList.add(complexvalue);
			}
		Property propertyComplex = new Property(null, complexProperty.getName(), ValueType.COLLECTION_COMPLEX, complexValueList);
		propertyList.add(propertyComplex);
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
		String result = "";
		for (CsdlEntitySet item : entityRegister.getEntitySet()) {
			if (item.getTypeFQN().getFullQualifiedNameAsString().equals(entity.getType())) {
				result = item.getName();
			}
		}
		return result;
	}
	
	// Navigation from EntityCollection to corresponding Entity
	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) throws SolrServerException, IOException {
		EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType, "");
		if (collection.getEntities().isEmpty()) {
			return null;
		}
		return collection.getEntities().get(0);
	}

	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) throws SolrServerException, IOException {

		EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType, "");
		return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
	}

	public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType, String relation) throws SolrServerException, IOException {

			// get uuid from entity, not it's primary id. This is usally being used as the values containing the links
			String entityID = sourceEntity.getProperty("uuid").getValue().toString();
			List<UriParameter> keyParams = null;
			SolrDocumentList responseDocuments;
			boolean isEntityCollection = true;
			List<String> filterList = new LinkedList<>();
			EntityModel sourceModel = null;
			EntityModel targetModel = null;
			for (EntityModel item: entityRegister.getEntityList()) {
				if (item.getFullQualifiedName().getFullQualifiedNameAsString().equals(sourceEntity.getType())) {
					sourceModel = item;
				}		
				if (item.getFullQualifiedName().equals(targetEntityType.getFullQualifiedName())) {
					targetModel= item;	
				}
			}

			String dspaceId = entityID;
			String searchfilter = targetModel.getNavigationFilter(sourceModel.getEntitySetName()+relation, dspaceId);
			queryMaker.addSearchFilter(searchfilter);
			String[] reverseRelationArr = Util.calculatereverseRelation(sourceModel, targetModel, sourceEntity,
				"reverse");
			if (reverseRelationArr != null && reverseRelationArr.length > 0 && reverseRelationArr[0] != null && !reverseRelationArr[0].contentEquals("")) {
				List<String> reverseRelation = Arrays.asList(reverseRelationArr);
				filterList.addAll(reverseRelation);
			}
			filterList.addAll(targetModel.getEntityFilter());
			if ((searchfilter == null || searchfilter.isBlank()) && filterList.isEmpty()) {
				return null;
			}
			responseDocuments = getQuerriedDataFromSolr(targetModel.getEntitySetName(), keyParams, isEntityCollection, filterList);
		EntityCollection navigationTargetEntityCollection = createEntitySet(responseDocuments, targetModel);
			
		if (navigationTargetEntityCollection.getEntities().isEmpty()) {
			return null;
		}
		return navigationTargetEntityCollection;
	}
	
	public List<Entity> getRelatedSelectedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType,
			String relation) throws SolrServerException, IOException {
			/* Returns an already ordered List for this EntityCollection
			 * The List is ordered after the amount of the occurence of the dspaceId (here uuid) in some  solr-field.
			 * This selected preferences field is not part of the odata entity, only of the solrdocument. 
			 * Combines Logic from other functions here.
			 * */
		if (relation.isEmpty() || !relation.endsWith("_SELECTED")) {
			//For Selected Lists, we have to use the uuid value instead of the id
			return getRelatedEntityCollection(sourceEntity, targetEntityType, relation).getEntities();
		}
			// get ID from  Entity Source
			String entityID = sourceEntity.getProperty("id").getValue().toString();
			List<UriParameter> keyParams = null;
			SolrDocumentList responseDocuments;
			boolean isEntityCollection = true;
		EntityModel sourceModel = null;
			EntityModel targetModel = null;
			for (EntityModel item: entityRegister.getEntityList()) {
				if (item.getFullQualifiedName().getFullQualifiedNameAsString().equals(sourceEntity.getType())) {
					sourceModel = item;
				}		
				if (item.getFullQualifiedName().equals(targetEntityType.getFullQualifiedName())) {
					targetModel= item;	
				}
			}
			
			String dspaceId = converter.convertODataIDToDSpaceID(entityID);
			dspaceId = sourceEntity.getProperty("uuid").getValue().toString();
			if (dspaceId == null) return null;
			String searchfilter = targetModel.getNavigationFilter(sourceModel.getEntitySetName()+relation, dspaceId);
			queryMaker.addSearchFilter(searchfilter);
			List<String> filterList = new LinkedList<>(targetModel.getEntityFilter());
			if ((searchfilter == null || searchfilter.isBlank()) && filterList.isEmpty()) {
				return null;
			}
			responseDocuments = getQuerriedDataFromSolr(targetModel.getEntitySetName(), keyParams, isEntityCollection, filterList);
	
			if (responseDocuments == null || responseDocuments.isEmpty()) return null;

			String selectedfield = "";
			//This Relation is also configured in Publication. It's used here for sorting
			if ((sourceModel.getEntitySetName()+relation).contentEquals("Researchers_SELECTED")) {
				selectedfield = "relation.isPublicationsSelectedFor";
			}
			Map<String, Integer> priority = new HashMap<>(); // Map holding primary key of entity and Priority Value

			String idproperty = targetEntityType.getKeyPredicateNames().get(0);
			EntityCollection navigationTargetEntityCollection = new EntityCollection();
			for (SolrDocument solrDocument : responseDocuments) {
				if (solrDocument.getFirstValue("withdrawn").equals("false")) {
					try {
						int cnt = 0;
						Collection<Object> fieldvals = solrDocument.getFieldValues(selectedfield);
						//Determine Position of selected fields
						for (Object v : fieldvals) {
							if (((String) v).contentEquals(dspaceId)) {
								cnt++;
							}
						}

						Entity ent = createEntity(createPropertyList(solrDocument, targetModel), targetModel.getEntitySetName());
						navigationTargetEntityCollection.getEntities().add(ent);
						priority.put(ent.getProperty(idproperty).getValue().toString(), cnt);
					} catch(Exception ignored) {

					}
				}
			}
			if (navigationTargetEntityCollection.getEntities().isEmpty()) {
				return null;
			}
			
			//sort entitySet by priority value determined in Map priority. higher priorityvalue = higher Position in List
			//NOT YET IMPLEMENTED: The sorting/place of the relation selection is not yet implemented in dspace-cris7
			List<Entity> result = navigationTargetEntityCollection.getEntities();
			result.sort((entity1, entity2) -> {
				int compareResult = 0;
				try {
					//Issue with Priority and Relation Selected/Hidden Funktionality in Dspace-Cris:
					//first Element has Priority 1, following 100 desc
					Integer prio1 = priority.get(entity1.getProperty(idproperty).getValue().toString());
					Integer prio2 = priority.get(entity2.getProperty(idproperty).getValue().toString());
					if (prio1 == 1) {
						return -1;
					}
					if (prio2 == 1) {
						return 1;
					}
					compareResult = prio2.compareTo(prio1);
				} catch (Exception e) {
					//
				}
				return compareResult;
			});
		return result;
	}
	
	//returns a entity collection with the source entity
	public EntityCollection getEntityCollectionWithSourceEntity(Entity sourceEntity) throws SolrServerException, IOException {
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.getEntities().add(sourceEntity);
		entityCollection.setCount(entityCollection.getEntities().size());

		if (entityCollection.getEntities().isEmpty()) {
			return null;
		}
		return entityCollection;
	}
	
	public String readFunctionImportStyle(final UriResourceFunction uriResourceFunction) {
				List<UriParameter> parameters = uriResourceFunction.getParameters();
				if (!parameters.isEmpty()) {
					return parameters.get(0).getText().replace("'", "");
				}
				return null;			
	}	
	
	public EdmEntitySet readFunctionImportEntitySet(final UriResourceFunction uriResourceFunction, final ServiceMetadata serviceMetadata) {
		if (EdmProviderDSpace.FUNCTION_CSL_FOR_RESEARCHER.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_ORGUNIT.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		}  else if (EdmProviderDSpace.FUNCTION_CSL_FOR_ORGUNIT_CHILD.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PROJECT.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PUBLICATION.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_JOURNAL.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_SERIES.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_SUPERVISOR.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_AUTHOR.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_RESEARCHER_SELECTED.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_PJ_FOR_OU.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Project.ES_PROJECTS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PRODUCT.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Product.ES_PRODUCTS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PRODUCTPERSON.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Product.ES_PRODUCTS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PRODUCTPROJECT.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Product.ES_PRODUCTS_NAME);
		} else if (EdmProviderDSpace.FUNCTION_CSL_FOR_PRODUCTORGUNIT.equals(uriResourceFunction.getFunctionImport().getName())) {
			return serviceMetadata.getEdm().getEntityContainer().getEntitySet(Product.ES_PRODUCTS_NAME);
		}
		return null;
	}	
	
	public List<UriParameter> readFunctionImportId(final UriResourceFunction uriResourceFunction){
		List<UriParameter> parameters = new LinkedList<>();
		if (uriResourceFunction.getParameters().size() == 2) {
			//csl functions with two parameters
		parameters.add(uriResourceFunction.getParameters().get(1));
		} else {
		parameters.add(uriResourceFunction.getParameters().get(0));
		}
		return parameters;
	}

	private void addReadableByAnonymousFilter() {
		String groupfilter = System.getenv("SOLR_ANONYMOUS_GROUP_UUID");
		if(groupfilter != null && !groupfilter.isBlank()) {
			if (groupfilter.contains(":")) {
				// it's some filter'
			queryMaker.addSearchFilter(groupfilter);
			} else {
				//it's an uuid
			queryMaker.addSearchFilterForAttribute("read", groupfilter);
			}
		}

	}
}
