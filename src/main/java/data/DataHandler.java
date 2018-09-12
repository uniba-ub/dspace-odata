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
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import entitys.EntityRegister;
import odata.EdmProviderDSpace;
import service.IdConverter;
import service.SolrQueryMaker;

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
				responseDocuments = getQuerriedDataFromSolr(item, keyParams);
				entitySet = createEntitySet(responseDocuments, item);
				System.out.println(entitySet.getEntities().get(0).getProperty("fullName"));

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
				responseDocuments = getQuerriedDataFromSolr(edmEntityType.getName(), keyParams);
				entitySet = createEntitySet(responseDocuments, item.getName());
				entity = entitySet.getEntities().get(0);
				}

		}

		return entity;
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName, List<UriParameter> keyParams) throws SolrServerException, IOException {
		if (entitySetName.equals("Projects")) {
			queryMaker.setQuerySearchToProjects();
			queryMaker.setResponseLimitToMax();
		} else if (entitySetName.equals("Project")) {
			queryMaker.setQuerySearchToProjects();
			String type = "pj";
			String id = keyParams.get(0).getText();
			String crisId= converter.convertToCrisID(id, type);
			queryMaker.addSearchFilterForAttribute("cris-id", crisId);
		}

		else if (entitySetName.equals("Researchers")) {
			queryMaker.setQueryTermToResearchers();
			queryMaker.setResponseLimitToMax();
		} else if (entitySetName.equals("Publications")) {
			queryMaker.setQueryTermToPublications();
			queryMaker.setResponseLimitToMax();
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
				int convertedId = converter.convertToId(currentId);
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
}
