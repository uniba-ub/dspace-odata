package data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import entitys.ComplexModel;
import entitys.EntityModel;
import entitys.EntityRegister;
import entitys.Project;
import entitys.Publication;
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
		try {
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
		}catch(Exception e) {
			//reset query, if queryMaker is false or not found
			e.printStackTrace();
			queryMaker.resetQuery();
		}
		return null;
		}

	public EntityCollection createEntitySet(SolrDocumentList documentList, EntityModel entity) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		for (SolrDocument solrDocument : documentList) {
			if(solrDocument.getFirstValue("withdrawn").equals("false")) {
				entitySet.getEntities()
				.add(createEntity(createPropertyList(solrDocument, entity), entity.getEntitySetName()));
			}
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
								if(item.getName().equals("author") || item.getName().equals("articlecollectionEditor") || item.getName().contentEquals("editor")) {
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
						property = new Property(null, item.getName(), ValueType.PRIMITIVE, solrDocument.getFirstValue(mapping.get(item.getName())));
						propertyList.add(property);
					} else if(itemType.equals("DateTimeOffset") | itemType.contentEquals("DateTime")) {
						//transform from Solr-value to datetime
						try {
						DateFormat dateFormat = new SimpleDateFormat(
					            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
						Date date = dateFormat.parse((solrDocument.getFirstValue(mapping.get(item.getName()))).toString());
						property = new Property(null, item.getName(), ValueType.PRIMITIVE, date);
						propertyList.add(property);
						}catch (ParseException e) {
							// TODO: handle exception
						}catch(Exception e) {
							
						}
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
		queryMaker.setSearchFilterForComplexProperty(idOfSolrObject, complexProperty.getParentFK(), complexProperty.getSchema());
		queryMaker.setResponseLimitToMax();
		SolrDocumentList responseDocumentsForComplexProperty = solr.getData(queryMaker);
		HashMap<String, String> mapping = complexProperty.getMapping();
		List<ComplexValue> complexValueList = new LinkedList<ComplexValue>();
		for(SolrDocument solrDocument: responseDocumentsForComplexProperty) {
			ComplexValue complexvalue = new ComplexValue();
			List <Property> complexSubProperties = complexvalue.getValue();
			for(CsdlProperty item: complexProperty.getComplexType().getProperties()) {
				Property complexSubProperty = new Property(null, item.getName(), ValueType.PRIMITIVE, solrDocument.getFirstValue(mapping.get(item.getName())));
				complexSubProperties.add(complexSubProperty);
			}
			complexValueList.add(complexvalue);
		}
		Property propertyComplex = new Property(null, complexProperty.getName(), ValueType.COLLECTION_COMPLEX, complexValueList);
		propertyList.add(propertyComplex);
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
			queryMaker.addSearchFilter((targetModel.getNavigationFilter(sourceModel.getEntitySetName()+relation, dspaceId)));
			String[] reverseRelationArr = Util.calculatereverseRelation(sourceModel, targetModel, sourceEntity, dspaceId,  "reverse");
			if(reverseRelationArr != null && reverseRelationArr.length > 0 && reverseRelationArr[0] != null && !reverseRelationArr[0].contentEquals("")) {
				List<String> reverseRelation = Arrays.asList(reverseRelationArr);
				filterList.addAll(reverseRelation);
			}
			responseDocuments = getQuerriedDataFromSolr(targetModel.getEntitySetName(), keyParams, isEntityCollection, filterList);	
			navigationTargetEntityCollection = createEntitySet(responseDocuments, targetModel);
			
		if (navigationTargetEntityCollection.getEntities().isEmpty()) {
			return null;
		}
		return navigationTargetEntityCollection;
	}
	
	public List<Entity> getRelatedSelectedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType,
			String relation) throws SolrServerException, IOException {
			/* Returns a already ordered List for this EntityCollection
			 * The List is ordered after the amount of the occurence of the dspaceId (here uuid) in some  solr-field.
			 * This selected preferences field is not part of the odata entity, only of the solrdocument. 
			 * Combines several Logic from other functions here.
			 * */
		if(relation.isEmpty() || !relation.endsWith("_SELECTED")){
			//For Selected Lists, we have to use the uuid value instead of the id
			return getRelatedEntityCollection(sourceEntity, targetEntityType, relation).getEntities();
		}
			// get ID from from Entity Source
			String entityID = sourceEntity.getProperty("id").getValue().toString();
			List<UriParameter> keyParams = null;
			SolrDocumentList responseDocuments = null;
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
			dspaceId = sourceEntity.getProperty("uuid").getValue().toString();
			if(dspaceId == null) return null;
			queryMaker.addSearchFilter((targetModel.getNavigationFilter(sourceModel.getEntitySetName()+relation, dspaceId)));
			responseDocuments = getQuerriedDataFromSolr(targetModel.getEntitySetName(), keyParams, isEntityCollection, filterList);
	
			if(responseDocuments == null || responseDocuments.isEmpty()) return null;

			String selectedfield = "";
			//This Relation is also configured in Publication. It's used here for sorting
			if((sourceModel.getEntitySetName()+relation).contentEquals("Researchers_SELECTED")) {
				selectedfield = "relationpreferences.crisrp.publications.selected";
			}
			Map<String, Integer> priority = new HashMap<String, Integer>(); // Map holding primary key of entity and Priority Value

			String idproperty = targetEntityType.getKeyPredicateNames().get(0).toString();
			EntityCollection navigationTargetEntityCollection = new EntityCollection();
			for (SolrDocument solrDocument : responseDocuments) {
				if(solrDocument.getFirstValue("withdrawn").equals("false")) {
					try {
					int cnt = 0;
					Collection<Object> fieldvals = solrDocument.getFieldValues(selectedfield);
					//Determine Position of selected fields
					for(Object v : fieldvals) {
						if(((String) v).contentEquals(dspaceId)) {
							cnt++;
						}
					}
					
					Entity ent = createEntity(createPropertyList(solrDocument, targetModel), targetModel.getEntitySetName());
					navigationTargetEntityCollection.getEntities().add(ent);
					priority.put(ent.getProperty(idproperty).getValue().toString(), cnt);
					}catch(Exception e) {

					}
				}
			}
			if (navigationTargetEntityCollection.getEntities().isEmpty()) {
				return null;
			}
			
			//sort entitySet by priority value determined in Map priority. higher priorityvalue = higher Position in List
			List<Entity> result = navigationTargetEntityCollection.getEntities();
			Collections.sort(result, new Comparator<Entity>() {
				public int compare(Entity entity1, Entity entity2) {
					int compareResult = 0;
					try {
					//Issue with Priority and RelationPreferencesSolrIndexPlugin in Dspace-Cris: 
					//first Element has Priority 1, following 100 desc
					Integer prio1 = priority.get(entity1.getProperty(idproperty).getValue().toString());
					Integer prio2 = priority.get(entity2.getProperty(idproperty).getValue().toString());
					if(prio1 == 1) return -1;
					if(prio2 == 1) return 1;
					compareResult = prio2.compareTo(prio1);
					}catch(Exception e) {
						
					}
					return compareResult;
				}
			});
		return result;
	}
	
	public String readFunctionImportStyle(final UriResourceFunction uriResourceFunction) throws ODataApplicationException {	
				List<UriParameter> parameters = uriResourceFunction.getParameters();
				if(!parameters.isEmpty()) {
					String styleparameter = parameters.get(0).getText().replace("'", "");
					return styleparameter;
				}
				return null;			
	}	
	
	public EdmEntitySet readFunctionImportEntitySet(final UriResourceFunction uriResourceFunction, final ServiceMetadata serviceMetadata) throws ODataApplicationException {
		if(EdmProviderDSpace.FUNCTION_CSL_FOR_RESEARCHER.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		} else if(EdmProviderDSpace.FUNCTION_CSL_FOR_ORGUNIT.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		} else if(EdmProviderDSpace.FUNCTION_CSL_FOR_PROJECT.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		} else if(EdmProviderDSpace.FUNCTION_CSL_FOR_JOURNAL.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		} else if(EdmProviderDSpace.FUNCTION_CSL_FOR_SERIES.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		}else if(EdmProviderDSpace.FUNCTION_CSL_FOR_SUPERVISOR.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		}else if(EdmProviderDSpace.FUNCTION_CSL_FOR_AUTHOR.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		}else if(EdmProviderDSpace.FUNCTION_CSL_FOR_RESEARCHER_SELECTED.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Publication.ES_PUBLICATIONS_NAME);
			  return entitySet;
		}else if(EdmProviderDSpace.FUNCTION_PJ_FOR_OU.equals(uriResourceFunction.getFunctionImport().getName())){
			  EdmEntitySet entitySet = serviceMetadata.getEdm().getEntityContainer().getEntitySet(Project.ES_PROJECTS_NAME);
			  return entitySet;
		}
		return null;
	}	
	
	public List<UriParameter> readFunctionImportId(final UriResourceFunction uriResourceFunction){
		List<UriParameter> parameters = new LinkedList<UriParameter>();
		if(uriResourceFunction.getParameters().size() == 2) {
			//csl functions with two parameters
		parameters.add(uriResourceFunction.getParameters().get(1));
		}else {
		parameters.add(uriResourceFunction.getParameters().get(0));
		}
		return parameters;

	}
}
