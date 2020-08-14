package entitys;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;


public class EntityRegister {
	
		private List<CsdlEntityType> entityTypList;
		private List<CsdlComplexType> complexTypeList;
		private List<String> entitySetNameList;
		private List<String> complexTypeNameList;
		private List<FullQualifiedName> entityFQNList;
		private List<FullQualifiedName> complexTypeFQNList;
		private List<CsdlEntitySet> entitySetList;
		List<EntityModel> entityList;
		List<ComplexModel> complexPropertyList;
	

		public EntityRegister() {
			registerComplexTypes();
			registerEntitys();
			fillList();
						
		}

		public void registerEntitys() {
			entityList = new LinkedList<EntityModel>();
			List<EntityModel> navEntityList = new LinkedList<EntityModel>();
			Person person = new Person();
		    entityList.add(person);
		    
		    Place place = new Place();
		    entityList.add(place);
		    
		    Corporation corp = new Corporation();
		    entityList.add(corp);
		    
		    Family family = new Family();
		    entityList.add(family);
		    
		    navEntityList.add(person);
		    navEntityList.add(place);
		    setNavigationPropertyForEntity(corp, navEntityList);
		    navEntityList = new LinkedList<EntityModel>();
		    
		    navEntityList.add(corp);
		    navEntityList.add(person);
		    setNavigationPropertyForEntity(place, navEntityList);
		    navEntityList = new LinkedList<EntityModel>();
		    
		    navEntityList.add(corp);
		    navEntityList.add(place);
		    navEntityList.add(family);
		    setNavigationPropertyForEntity(person, navEntityList);
		    navEntityList = new LinkedList<EntityModel>();
		    
		    navEntityList.add(person);
		    setNavigationPropertyForEntity(family, navEntityList);
		    
		    
		    
			/*
			 *
			 

			Publication publication = new Publication();
		    entityList.add(publication);
		    
			Researcher researcher = new Researcher();
			entityList.add(researcher);
			
			Orgunit orgunit = new Orgunit();
		    entityList.add(orgunit);
		    
		    Project project = new Project();
		    entityList.add(project);
		    
		    Journal journal = new Journal();
		    entityList.add(journal);
		    
		    Award award = new Award();
		    entityList.add(award);
		    
		    Awardseries awardseries = new Awardseries();
		    entityList.add(awardseries);
		    
		    Series series = new Series();
		    entityList.add(series);
		    
		    navEntityList.add(publication);
		    navEntityList.add(project);
		    navEntityList.add(orgunit);
		    setNavigationPropertyForEntity(researcher, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(project);
		    navEntityList.add(researcher);
		    navEntityList.add(publication);
		    navEntityList.add(orgunit);
		    setNavigationPropertyForEntity(orgunit, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(orgunit);
		    navEntityList.add(researcher);
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(project, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(journal, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(series, navEntityList);
		    
		    navEntityList = new LinkedList<EntityModel>();
		    navEntityList.add(series);
		    navEntityList.add(journal);
		    navEntityList.add(researcher);
		    navEntityList.add(orgunit);
		    navEntityList.add(project);
		    setNavigationPropertyForEntity(publication, navEntityList);
		    */
		}

		private void registerComplexTypes() {
			complexPropertyList = new LinkedList<ComplexModel>();
			//TODO: register comlex types for profkat
			/*
			Funding funding = new Funding();
			Partnership partnership = new Partnership();
			complexPropertyList.add(funding);
			complexPropertyList.add(partnership)
			*/
		}
		
		public List<CsdlEntityType> getEntityTypeList() {
			return entityTypList;
		}

		public List<String> getEntitySetNameList() {
			return entitySetNameList;
		}

		public List<FullQualifiedName> getEntityFQNList() {
			return entityFQNList;
		}
		
		public List<CsdlEntitySet> getEntitySet(){			
			return entitySetList;
			
		}
		
		public CsdlEntitySet getEntitySetByName(String entitySetName){
			for(CsdlEntitySet item:entitySetList) {
				if(item.getName().equals(entitySetName)) {
					return item;
				}
			}
			return null;
		}
		
		public List<EntityModel> getEntityList() {
			return entityList;
		}
		

		public List<FullQualifiedName> getComplexFQNList(){
			return complexTypeFQNList;
		}

		public List<CsdlComplexType> getComplexTypeList(){
			return complexTypeList;
		}

		public List<ComplexModel> getComplexProperties(){
			return complexPropertyList;
		}

		public List<String> getComplexTypeNameList(){
			return complexTypeNameList;
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
		
		private void fillList() {
			
			entityTypList = new LinkedList<CsdlEntityType>();
			entitySetNameList = new LinkedList<String>();
			entityFQNList = new LinkedList<FullQualifiedName>();
			entitySetList = new LinkedList<CsdlEntitySet>();
			complexTypeList = new LinkedList<CsdlComplexType>();
			complexTypeFQNList = new LinkedList<FullQualifiedName>();
			complexTypeNameList = new LinkedList<String>();

			for(ComplexModel item: complexPropertyList) {
				complexTypeList.add(item.getComplexType());
				complexTypeFQNList.add(item.getFullQualifiedName());
				complexTypeNameList.add(item.getName());
			}
			
			for(EntityModel item: entityList) {
				entityTypList.add(item.getEntityType());
				entityFQNList.add(item.getFullQualifiedName());
				entitySetNameList.add(item.getEntitySetName());
				entitySetList.add(item.getEntitySet());			
			}
		}
}
