package entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;

import odata.EdmProviderDSpace;

public class EntityRegister {
	
		public List<CsdlEntityType> entityTypList;
		public List<String> entitySetNameList;
		public List<FullQualifiedName> entityFQNList;
		public List<CsdlEntitySet> entitySetList;
	
		public EntityRegister() {		
			registerEntitys();
						
		}
		
		public void registerEntitys() {
			List<EntityModel> entityList = new LinkedList<EntityModel>();
			List<EntityModel> navEntityList = new LinkedList<EntityModel>();

			Researcher researcher = new Researcher();
			entityList.add(researcher);
			
			Orgunit orgunit = new Orgunit();
		    entityList.add(orgunit);
		    
		    Project project = new Project();
		    entityList.add(project);
		    
		    navEntityList.add(orgunit);
		    setNavigationPropertyForEntity(researcher, navEntityList);
		    
		    fillList(entityList);

			
		}
		

		public List<CsdlEntityType> getEntityTypList() {
			return entityTypList;
		}

		public List<String> getEntitySetNameList() {
			return entitySetNameList;
		}

		public List<FullQualifiedName> getFQNList() {
			return entityFQNList;
		}
		
		public List<CsdlEntitySet> getEntitySet(){			
			return entitySetList;
			
		}
		
		public void setNavigationPropertyForEntity (EntityModel entity, List<EntityModel> entitiyList) {
			
			List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
			CsdlNavigationPropertyBinding navPropBinding= new CsdlNavigationPropertyBinding();
			
			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			CsdlNavigationProperty navProp = new CsdlNavigationProperty();

			
			for(EntityModel item: entitiyList) {
				
				navPropBinding.setTarget(item.getEntitySetName());
				navPropBinding.setPath(item.getEntitySetName());
				navPropBindingList.add(navPropBinding);
				
				navProp.setName(item.getEntitySetName());
				navProp.setType(item.getFullQualifiedName());
				navProp.setCollection(true);
				navPropList.add(navProp);
				
			}
			entity.getEntitySet().setNavigationPropertyBindings(navPropBindingList);
			
		}
		
		private void fillList(List<EntityModel> entityList) {
			
			entityTypList = new LinkedList<CsdlEntityType>();
			entitySetNameList = new LinkedList<String>();
			entityFQNList = new LinkedList<FullQualifiedName>();
			entitySetList = new LinkedList<CsdlEntitySet>();
			
			for(EntityModel item: entityList) {
				entityTypList.add(item.getEntityType());
				entityFQNList.add(item.getFullQualifiedName());
				entitySetNameList.add(item.getEntitySetName());
				entitySetList.add(item.getEntitySet());			
			}
			
		}
		
		public Entity createEntity(List<Property> propertyList, String entitySetName) {
			Entity entity = new Entity();
			String type = (EdmProviderDSpace.NAMESPACE + "." + entitySetName.substring(0, entitySetName.length()-1));
			for(Property item: propertyList) {
				entity.addProperty(item);
				entity.setType(type);
				entity.setId(createId(entity, "id"));
			}
			return entity;
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
			for(CsdlEntitySet item: entitySetList) {
				if(item.getTypeFQN().getFullQualifiedNameAsString().equals(entity.getType())){
					result = item.getName();
				} 
			}
			return result;
		}
			
		
}
