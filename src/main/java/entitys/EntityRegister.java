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
		    navEntityList.add(project);
		    setNavigationPropertyForEntity(researcher, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(project);
		    navEntityList.add(researcher);
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(orgunit, navEntityList);
		    
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
			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();

			
			for(EntityModel item: entitiyList) {
				
				
				CsdlNavigationPropertyBinding navPropBinding= new CsdlNavigationPropertyBinding();
				navPropBinding.setTarget(item.getEntitySetName());
				navPropBinding.setPath(item.getEntitySetName());
				navPropBindingList.add(navPropBinding);
				
				CsdlNavigationProperty navProp = new CsdlNavigationProperty();
	
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
