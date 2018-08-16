package data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
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
import service.SolrQueryMaker;

public class DataHandler {

	private EntityRegister entityRegister;
	private List<Property> propertyList;
	private SolrConnector solr;
	private SolrQueryMaker queryMaker;

	public DataHandler() {

		entityRegister = new EntityRegister();
		solr = new SolrConnector();
		queryMaker = new SolrQueryMaker();

	}

	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) throws SolrServerException, IOException {
		EntityCollection entitySet = new EntityCollection();
		SolrDocumentList responseDocuments;
		for (String item : entityRegister.getEntitySetNameList()) {
			if (edmEntitySet.getName().equals(item)) {
				responseDocuments = getQuerriedDataFromSolr(item);
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
				// unklar ob richtige Stelle
				responseDocuments = getQuerriedDataFromSolr(edmEntityType.getName());
				entitySet = createEntitySet(responseDocuments, item.getName());
				entity = entitySet.getEntities().get(0);
			}

		}

		return entity;
	}

	public SolrDocumentList getQuerriedDataFromSolr(String entitySetName) throws SolrServerException, IOException {
		if (entitySetName.equals("Projects")) {
			queryMaker.setQuerySearchToProjects();
			queryMaker.setResponseLimitToMax();
		} else if (entitySetName.equals("Project")) {
			queryMaker.setQuerySearchToProjects();
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
		int idIndex = 1;
		for (SolrDocument solrDocument : documentList) {
			entitySet.getEntities()
					.add(createEntity(createPropertyList(solrDocument, entitySetName, idIndex), entitySetName));
			idIndex++;
		}

		return entitySet;
	}

	public Entity createEntity(List<Property> propertyList, String entitySetName) {
		Entity entity = new Entity();
		String type = (EdmProviderDSpace.NAMESPACE + "." + entitySetName.substring(0, entitySetName.length() - 1));
		for (Property item : propertyList) {
			entity.addProperty(item);
		}
		entity.setType(type);
		entity.setId(createId(entity, "id"));
		return entity;
	}

	public List<Property> createPropertyList(SolrDocument solrDocument, String entitySetName, int idIndex) {
		propertyList = new LinkedList<Property>();
		Property property;
		Property propertyId = new Property(null, "id", ValueType.PRIMITIVE, idIndex);
		propertyList.add(propertyId);

		for (String item : solrDocument.getFieldNames()) {
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
