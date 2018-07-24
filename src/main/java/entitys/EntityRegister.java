package entitys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;

public class EntityRegister {
	
		public List<CsdlEntityType> entityTypList;
		public List<String> entitySetNameList;
		public List<FullQualifiedName> entityTypNameList;
		public List<CsdlEntitySet> entitySetList;
	
		public EntityRegister() {		
			registerEntitys();
						
		}
		
		public void registerEntitys() {
			List<Entity> entityList = new LinkedList<Entity>();

			Researcher researcher = new Researcher();
			entityList.add(researcher);
			
			Orgunit orgunit = new Orgunit();
		    entityList.add(orgunit);
		    
		    fillList(entityList);

			
		}
		

		public List<CsdlEntityType> getEntityTypList() {
			return entityTypList;
		}

		public List<String> getEntitySetNameList() {
			return entitySetNameList;
		}

		public List<FullQualifiedName> getEntityTypNameList() {
			return entityTypNameList;
		}
		
		public List<CsdlEntitySet> getEntitySet(){			
			return entitySetList;
			
		}
		
		public void setNavigationPropertyForEntity (Entity entity, List<Entity> entitiyList) {
			
			List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
			CsdlNavigationPropertyBinding navPropBinding= new CsdlNavigationPropertyBinding();
			
			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			CsdlNavigationProperty navProp = new CsdlNavigationProperty();

			
			for(Entity item: entitiyList) {
				
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
		
		private void fillList(List<Entity> entityList) {
			
			entityTypList = new LinkedList<CsdlEntityType>();
			entitySetNameList = new LinkedList<String>();
			entityTypNameList = new LinkedList<FullQualifiedName>();
			entitySetList = new LinkedList<CsdlEntitySet>();
			
			for(Entity item: entityList) {
				entityTypList.add(item.getEntityType());
				entityTypNameList.add(item.getFullQualifiedName());
				entitySetNameList.add(item.getEntitySetName());
				entitySetList.add(item.getEntitySet());			
			}
			
		}
		
}
