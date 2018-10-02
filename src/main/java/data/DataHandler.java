package data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

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

	public DataHandler() {

		entityRegister = new EntityRegister();
		solr = new SolrConnector();
		queryMaker = new SolrQueryMaker();
		converter = new IdConverter();

	}

	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		SolrDocumentList responseDocuments;
		for (String item : entityRegister.getEntitySetNameList()) {
			if (edmEntitySet.getName().equals(item)) {
				List<UriParameter> keyParams = null;
				boolean isEntityCollection=true;
				responseDocuments = getQuerriedDataFromSolr(item, keyParams, isEntityCollection);
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
		for (CsdlEntityType item : entityRegister.getEntityTypList()) {
			if (edmEntityType.getName().equals(item.getName())) {
				boolean isEntityCollection = false;
				responseDocuments = getQuerriedDataFromSolr(edmEntityType.getName(), keyParams, isEntityCollection);
				entitySet = createEntitySet(responseDocuments, item.getName());
				entity = entitySet.getEntities().get(0);
				}

		}

		return entity;
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams, boolean isEntityCollection) throws SolrServerException, IOException {
		for(EntityModel item: entityRegister.getEntityList()) {
			if(item.getEntitySetName().equals(entitySetName)) {
				if(isEntityCollection) {
				queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
				queryMaker.setResponseLimitToMax();
				}
				else {
					queryMaker.setQuerySearchTerm(item.getRecourceTypeFilter());
					String type = item.getIDConverterTyp();
					String id = keyParams.get(0).getText();
					String crisId = converter.convertToCrisID(id, type);
					queryMaker.addSearchFilterForAttribute("cris-id", crisId);
				}
			}		
		}		

		SolrDocumentList responseDocuments = solr.getData(queryMaker.getQuery());
		return responseDocuments;
	}

	public EntityCollection createEntitySet(SolrDocumentList documentList, String entitySetName) {
		EntityCollection entitySet = new EntityCollection();
		for (SolrDocument solrDocument : documentList) {
			entitySet.getEntities()
					.add(createEntity(createPropertyList(solrDocument, entitySetName), entitySetName));
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

	public List<Property> createPropertyList(SolrDocument solrDocument, String entitySetName) {
		propertyList = new LinkedList<Property>();
		Property property;
		for (String item : solrDocument.getFieldNames()) {
			if(item.equals("cris-id")) {
				//change item value
				String currentId = (String) solrDocument.getFieldValue(item);
				int convertedId = converter.convertCrisToId(currentId);
				property = new Property(null, "id", ValueType.PRIMITIVE, convertedId);
				propertyList.add(property);
			
			} else if(item.equals("handle")) {
				String currentId = (String) solrDocument.getFieldValue(item);
				int convertedId = converter.convertHandleToId(currentId);
				property = new Property(null, "id", ValueType.PRIMITIVE, convertedId);
				propertyList.add(property);
				
			}
			
			property = new Property(null, item, ValueType.PRIMITIVE, solrDocument.getFieldValue(item));
			propertyList.add(property);
			

		}
		
		return propertyList;

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
	/*
	// Navigation from EntityCollection to corresponding Entity
	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) {
		EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType);
		if (collection.getEntities().isEmpty()) {
			return null;
		}
		return collection.getEntities().get(0);
	}

	public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) {

		EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType);
		return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
	}

	public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) {
		EntityCollection navigationTargetEntityCollection = new EntityCollection();

		FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
		String sourceEntityFqn = sourceEntity.getType();
		
			// relation Course->Performer (result all Performers)
			// get ID from Entitiy Source
		
			int entityID = (Integer) sourceEntity.getProperty("id").getValue();
			String crisID = converter.convertToCrisID(entityID, sourceEntityFqn);
			
			// Get relating PerformerKeys
			if (course.getDozsKeys().contains(";")) {
				String[] dozRefs = course.getDozsKeys().split(";");
				for (int i = 0; i < dozRefs.length; i++) {
					// Get Performers from database
					List<VCPerson> persons = databaseConnector
							.getPerformers(sqlCreater.createPerformersOfCourseStatement(dozRefs[i]));
					if(!persons.isEmpty()) {
						personList.add(persons.get(0));
					}


				}
				if (!personList.isEmpty()) {
					// init Entitys out of VCPerson Java Objects
					List<Entity> entityPerformerList = initPerformerOfCourseList(personList);
					// iterate over all performer entitys
					for (Entity entityPerformer : entityPerformerList) {
						// add all Performer Entitys to navigationTargetEntityCollection
						navigationTargetEntityCollection.getEntities().add(entityPerformer);
					}
				}

			} else {
				// if only Course only has one Performer, get Performer from database
				List<VCPerson> persons = databaseConnector
						.getPerformers(sqlCreater.createPerformersOfCourseStatement(course.getDozsKeys()));
				if (!persons.isEmpty()) {
					personList.add(persons.get(0));
					// init Entitys out of VCPerson Java Objects
					List<Entity> entityPerformerList = initPerformerOfCourseList(personList);
					// iterate over all performer entitys
					for (Entity entityPerformer : entityPerformerList) {
						// add all Performer Entitys to navigationTargetEntityCollection
						navigationTargetEntityCollection.getEntities().add(entityPerformer);
					}
				}



		} else if (sourceEntityFqn.equals(EdmProvider.ET_COURSE_FQN.getFullQualifiedNameAsString())
				&& relatedEntityFqn.equals(EdmProvider.ET_PLACE_FQN)) {
			// relation Course->Place (result all Places )
			int courseID = (Integer) sourceEntity.getProperty("id").getValue();
			// Get Course from database
			List<VCEvent> courses = databaseConnector.getCourses(sqlCreater.createCourseStatement(courseID));
			VCEvent course = courses.get(0);
			List<VCPlace> placeList = new LinkedList<VCPlace>();
			// Get relating PlaceKeys
			if (course.getPlaceKeys().contains(";")) {
				String[] placeRefs = course.getPlaceKeys().split(";");
				for (int i = 0; i < placeRefs.length; i++) {
					// get Places from database
					List<VCPlace> places = databaseConnector
							.getPlaces(sqlCreater.createPlacesOfCourseStatement(placeRefs[i]));
					placeList.add(places.get(0));
				}
				if (!placeList.isEmpty()) {
					// init Entitys out of VCPlace Java Objects
					List<Entity> entityPlaceList = initPlaceOfCourseList(placeList);
					// iterate over all place entitys
					for (Entity entityPlace : entityPlaceList) {
						// add all Place Entitys to navigationTargetEntityCollection
						navigationTargetEntityCollection.getEntities().add(entityPlace);

					}
				}
			} else {
				// if only Course only has one Place, get Place from database
				List<VCPlace> places = databaseConnector
						.getPlaces(sqlCreater.createPlacesOfCourseStatement(course.getPlaceKeys()));
				if (!places.isEmpty()) {
					placeList.add(places.get(0));
					// init Entitys out of VCPlace Java Objects
					List<Entity> entityPlaceList = initPlaceOfCourseList(placeList);
					// iterate over all place entitys
					for (Entity entityPlace : entityPlaceList) {
						// add all Place Entitys to navigationTargetEntityCollection
						navigationTargetEntityCollection.getEntities().add(entityPlace);

					}
				} else {
					Entity entity = new Entity();
					entity.addProperty(new Property(null, "id", ValueType.PRIMITIVE, 99999999));
					entity.addProperty(new Property(null, "name", ValueType.PRIMITIVE, "No place added"));
					entity.addProperty(new Property(null, "type", ValueType.PRIMITIVE, ThingTypes.Place));	
					navigationTargetEntityCollection.getEntities().add(entity);
					entity.setType(EdmProvider.ET_PLACE_FQN.getFullQualifiedNameAsString());
					entity.setId(createId(entity, "id"));

				}
			}

		} else if (sourceEntityFqn.equals(EdmProvider.ET_PLACE_FQN.getFullQualifiedNameAsString())
				&& relatedEntityFqn.equals(EdmProvider.ET_PLACE_SUB_PROPERTIES_FQN)) {
			// relation Place->PostalAdress
			int placeID = (Integer) sourceEntity.getProperty("id").getValue();
			List<VCPlace> places = databaseConnector.getPlaces((sqlCreater.createPlaceByIDStatement(placeID)));
			if(!places.isEmpty()) {
				Entity entityPlaceSUbPropertiesList = initPlaceSubPropertiesOfPlace(places);
					navigationTargetEntityCollection.getEntities().add(entityPlaceSUbPropertiesList);
			} else {
				Entity entity = new Entity();
				entity.addProperty(new Property(null, "id", ValueType.PRIMITIVE, placeID));
				entity.addProperty(new Property(null, "floors", ValueType.PRIMITIVE, "No floor added"));
				navigationTargetEntityCollection.getEntities().add(entity);
				
				
			}

		}

		else if (sourceEntityFqn.equals(EdmProvider.ET_COURSE_FQN.getFullQualifiedNameAsString())
				&& relatedEntityFqn.equals(EdmProvider.ET_COURSE_FQN)) {
			// relation Course->superEvent (result Place)
			int courseID = (Integer) sourceEntity.getProperty("id").getValue();
			String superEventRef="";
			for (VCEvent course : dataCourseList) {
				if (course.getId() == courseID) {
					superEventRef = course.getSuperEvent();
				}
			}
			if (!superEventRef.equals("")) {
				for(VCEvent course: dataCourseList) {
					if(course.getUid().equals(superEventRef)) {
						Entity entitySuperEvent = initSuperEvent(course);
						navigationTargetEntityCollection.getEntities().add(entitySuperEvent);
					}
					
				}

			}
		}


		else if (sourceEntityFqn.equals(EdmProvider.ET_COURSE_FQN.getFullQualifiedNameAsString())
				&& relatedEntityFqn.equals(EdmProvider.ET_EVENT_SUB_PROPERTY_FQN)) {
			// relation Course->Event_Sub_Properties
			int courseID = (Integer) sourceEntity.getProperty("id").getValue();
			String semester = "";
			String originalCategory = "";
			for (VCEvent course : dataCourseList) {
				if (course.getId() == courseID) {
					semester = course.getSemester();
					originalCategory = course.getOriginalCategory();
					break;
				}
			}

			Entity entityEventSubProperties = initEventSubProperties(courseID, semester, originalCategory);
			navigationTargetEntityCollection.getEntities().add(entityEventSubProperties);
		}

		if (navigationTargetEntityCollection.getEntities().isEmpty()) {
			return null;
		}
		return navigationTargetEntityCollection;
	}
	*/
}
