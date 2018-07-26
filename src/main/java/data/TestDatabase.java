package data;

import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.server.api.uri.UriParameter;

import entitys.EntityRegister;
import util.Util;

public class TestDatabase {
	
	private EntityRegister entityRegister;
	private List<Property> propertyList;
	
	
	public TestDatabase() {
		entityRegister = new EntityRegister();

		
	}
	
	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
		EntityCollection entitySet = new EntityCollection();
		propertyList = new LinkedList<Property>();
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
	
	
	private void initializeList(int x) {
		Property property1 = new Property(null, "id", ValueType.PRIMITIVE, x);
		propertyList.add(property1);
		
		Property property2 = new Property(null, "Name", ValueType.PRIMITIVE, x);
		propertyList.add(property2);
		
		Property property3 = new Property(null, "Anzeigename", ValueType.PRIMITIVE, x);
		propertyList.add(property3);

		Property property4 = new Property(null, "Forschungsinteressen", ValueType.PRIMITIVE, x);
		propertyList.add(property4);

		Property property5 = new Property(null, "Beschreibung", ValueType.PRIMITIVE, x);
		propertyList.add(property5);

		Property property6 = new Property(null, "Titel", ValueType.PRIMITIVE, x);
		propertyList.add(property6);

		Property property7 = new Property(null, "E-Mail", ValueType.PRIMITIVE, x);
		propertyList.add(property7);

		Property property8 = new Property(null, "Position", ValueType.PRIMITIVE, x);
		propertyList.add(property8);

		Property property9 = new Property(null, "Transfer Abstract", ValueType.PRIMITIVE, x);
		propertyList.add(property9);

		Property property10 = new Property(null, "Transfer Stichwörter", ValueType.PRIMITIVE, x);
		propertyList.add(property10);		
		
	}
	
	
}
