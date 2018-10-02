package entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		List<EntityModel> entityList;
	

		public EntityRegister() {		
			registerEntitys();
						
		}
		
		public void registerEntitys() {
			entityList = new LinkedList<EntityModel>();
			List<EntityModel> navEntityList = new LinkedList<EntityModel>();

			Researcher researcher = new Researcher();
			entityList.add(researcher);
			
			Orgunit orgunit = new Orgunit();
		    entityList.add(orgunit);
		    
		    Project project = new Project();
		    entityList.add(project);
		    
		    Publication publication = new Publication();
		    entityList.add(publication);
		    
		    navEntityList.add(publication);
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
		
		public List<EntityModel> getEntityList() {
			return entityList;
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
			entity.getEntityType().setNavigationProperties(navPropList);
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
		
		
}
