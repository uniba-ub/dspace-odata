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
		List<EntityModel> entityList = new LinkedList<>();
		List<ComplexModel> complexPropertyList;
	

		public EntityRegister() {
			registerComplexTypes();
			registerEntitys();
			fillList();
						
		}

		public void registerEntitys() {
			List<EntityModel> navEntityList = new LinkedList<>();

			Publication publication = new Publication();
		    entityList.add(publication);
		    
		    Product product = new Product();
		    entityList.add(product);
		    
			Researcher researcher = new Researcher();
			entityList.add(researcher);
			
			Orgunit orgunit = new Orgunit();
		    entityList.add(orgunit);
		    
		    Project project = new Project();
		    entityList.add(project);
		    
		    Journal journal = new Journal();
		    entityList.add(journal);
		    
		    Awards awards = new Awards();
		    entityList.add(awards);
		    
		    Awardseries awardseries = new Awardseries();
		    entityList.add(awardseries);
		    
		    Series series = new Series();
		    entityList.add(series);
		    
		    Funder funder = new Funder();
		    entityList.add(funder);
		    
		    navEntityList.add(publication);
		    navEntityList.add(product);
		    navEntityList.add(project);
		    navEntityList.add(orgunit);
		    navEntityList.add(awards);
		    setNavigationPropertyForEntity(researcher, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(project);
		    navEntityList.add(researcher);
		    navEntityList.add(publication);
		    navEntityList.add(product);
		    navEntityList.add(orgunit);
		    setNavigationPropertyForEntity(orgunit, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(orgunit);
		    navEntityList.add(researcher);
		    navEntityList.add(awards);
		    navEntityList.add(project);
		    navEntityList.add(publication);
		    navEntityList.add(product);
		    setNavigationPropertyForEntity(project, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(journal, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(publication);
		    setNavigationPropertyForEntity(series, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(series);
		    navEntityList.add(journal);
		    navEntityList.add(researcher);
		    navEntityList.add(orgunit);
		    navEntityList.add(project);
		    navEntityList.add(awards);
		    setNavigationPropertyForEntity(publication, navEntityList);
	
		    navEntityList = new LinkedList<>();
		    navEntityList.add(researcher);
		    navEntityList.add(orgunit);
		    navEntityList.add(project);
		    navEntityList.add(awards);
		    setNavigationPropertyForEntity(product, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(researcher);
		    navEntityList.add(awardseries);
		    navEntityList.add(project);
		    navEntityList.add(publication);
		    navEntityList.add(product);
		    setNavigationPropertyForEntity(awards, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(awards);
		    navEntityList.add(funder);
		    setNavigationPropertyForEntity(awardseries, navEntityList);
		    
		    navEntityList = new LinkedList<>();
		    navEntityList.add(awardseries);
		    navEntityList.add(project);
		    navEntityList.add(funder);
		    setNavigationPropertyForEntity(funder, navEntityList);
		    
		}

		private void registerComplexTypes() {
			complexPropertyList = new LinkedList<>();
			Funding funding = new Funding();
			Partnership partnership = new Partnership();
			complexPropertyList.add(funding);
			complexPropertyList.add(partnership);
			Affiliation affiliation = new Affiliation();
			Achievement achievement = new Achievement();
			Activity activity = new Activity();
			Career career = new Career();
			Education education = new Education();
			complexPropertyList.add(affiliation);
			complexPropertyList.add(achievement);
			complexPropertyList.add(activity);
			complexPropertyList.add(career);
			complexPropertyList.add(education);
		}
		
		public List<CsdlEntityType> getEntityTypeList() {
			return entityTypList;
		}

		public List<String> getEntitySetNameList() {
			return entitySetNameList;
		}
		
		public List<CsdlEntitySet> getEntitySet() {
			return entitySetList;
		}
		
		public List<EntityModel> getEntityList() {
			return entityList;
		}

		public List<CsdlComplexType> getComplexTypeList() {
			return complexTypeList;
		}

		public List<ComplexModel> getComplexProperties() {
			return complexPropertyList;
		}

		public List<String> getComplexTypeNameList() {
			return complexTypeNameList;
		}

		public void setNavigationPropertyForEntity (EntityModel entity, List<EntityModel> entitiyList) {

			List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>();
			List<CsdlNavigationProperty> navPropList = new ArrayList<>();

			for (EntityModel item: entitiyList) {
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
			
			entityTypList = new LinkedList<>();
			entitySetNameList = new LinkedList<>();
			entityFQNList = new LinkedList<>();
			entitySetList = new LinkedList<>();
			complexTypeList = new LinkedList<>();
			complexTypeFQNList = new LinkedList<>();
			complexTypeNameList = new LinkedList<>();

			for (ComplexModel item: complexPropertyList) {
				complexTypeList.add(item.getComplexType());
				complexTypeFQNList.add(item.getFullQualifiedName());
				complexTypeNameList.add(item.getName());
			}
			
			for (EntityModel item: entityList) {
				entityTypList.add(item.getEntityType());
				entityFQNList.add(item.getFullQualifiedName());
				entitySetNameList.add(item.getEntitySetName());
				entitySetList.add(item.getEntitySet());			
			}
		}
}
