package data;

import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.server.api.uri.UriParameter;

import entitys.EntityRegister;
import service.SolrQueryMaker;

public class DataHandler {

	private EntityRegister entityRegister;
	private List<Property> propertyList;
	private SolrConnector solr;
	private SolrQueryMaker queryMaker;
	
	public DataHandler() {
		
		entityRegister = new EntityRegister();
		
	}
	
	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
		EntityCollection entitySet = new EntityCollection();
		for(String item:entityRegister.getEntitySetNameList()) {
			if(edmEntitySet.getName().equals(item)) {	
				
				initializeList(1);
				entitySet.getEntities().add(entityRegister.createEntity(propertyList, item));
				initializeList(2);
				entitySet.getEntities().add(entityRegister.createEntity(propertyList, item));
				initializeList(3);
				entitySet.getEntities().add(entityRegister.createEntity(propertyList, item));

			}
		}		
		
		return entitySet;
	}
	
	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) {
		Entity entity = null;

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		for(CsdlEntityType item:entityRegister.getEntityTypList()) {
			if(edmEntityType.getName().equals(item.getName())){
				entity= entityRegister.createEntity(propertyList, edmEntitySet.getName());
			}
			
		}

		return entity;
	}
	
	public void setPropertyList(String entitySetName) {
		propertyList = new LinkedList<Property>();

		
		
		
	}
}
