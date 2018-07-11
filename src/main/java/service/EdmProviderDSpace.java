package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

public class EdmProviderDSpace extends CsdlAbstractEdmProvider {

	// Service Namespace
	public final static String NAMESPACE = "rz.CourseService";

	// EDM Container
	public final static String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_RESEARCHER_NAME = "Researcher";
	public static final FullQualifiedName ET_RESEARCHER_FQN = new FullQualifiedName(NAMESPACE, ET_RESEARCHER_NAME);

	public static final String ET_ORGUNIT_NAME = "Orgunit";
	public static final FullQualifiedName ET_ORGUNIT_FQN = new FullQualifiedName(NAMESPACE, ET_ORGUNIT_NAME);

	// Entity Set Names
	public static final String ES_RESEARCHERS_NAME = "Researchers";
	public static final String ES_ORGUNITS_NAME = "Orgunits";

	@Override
	public CsdlEntityContainer getEntityContainer() throws ODataException {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(CONTAINER, ES_RESEARCHERS_NAME));
		entitySets.add(getEntitySet(CONTAINER, ES_ORGUNITS_NAME));
		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
		// Method is triggered when requesting the Service Document
		if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}

		return null;

	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		CsdlEntitySet entitySet = null;

		if (entityContainer.equals(CONTAINER)) {
			if (entitySetName.equals(ES_RESEARCHERS_NAME)) {
				entitySet = new CsdlEntitySet();
				entitySet.setName(ES_RESEARCHERS_NAME);
				entitySet.setType(ET_RESEARCHER_FQN);

				// creation of the navigationPropertyBindings
				List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();

				CsdlNavigationPropertyBinding navPropBindingPerformer = new CsdlNavigationPropertyBinding();
				navPropBindingPerformer.setTarget("Orgunits");
				navPropBindingPerformer.setPath("Orgunits");
				navPropBindingList.add(navPropBindingPerformer);

				entitySet.setNavigationPropertyBindings(navPropBindingList);

			} else if (entitySetName.equals(ES_ORGUNITS_NAME)) {

				entitySet = new CsdlEntitySet();
				entitySet.setName(ES_ORGUNITS_NAME);
				entitySet.setType(ET_ORGUNIT_FQN);

			}

		}
		return entitySet;

	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		CsdlEntityType entityType = null;

		// if PropertyTypName equals FQN Name, then EntityProperty will be created
		if (entityTypeName.equals(ET_RESEARCHER_FQN)) {

			// create EntityProperty
			CsdlProperty id = new CsdlProperty().setName("id")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty fullName = new CsdlProperty().setName("Name")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty  creditName= new CsdlProperty().setName("Anzeigename")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty researchInterests = new CsdlProperty().setName("Forschungsinteressen")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty().setName("Beschreibung")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty title = new CsdlProperty().setName("Titel")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty email = new CsdlProperty().setName("E-Mail")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty position = new CsdlProperty().setName("Position")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty transferabstract = new CsdlProperty().setName("Transfer Abstract")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty transferkeywords = new CsdlProperty().setName("Transfer Stichwörter")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// creation of PropertyRef for the key Element

			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("id");

			// creation of the navigation properties
			CsdlNavigationProperty navPropOrgunits = new CsdlNavigationProperty().setName("Orgunits")
					.setType(ET_ORGUNIT_FQN).setCollection(true);

			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			navPropList.add(navPropOrgunits);


			// configuration of the Entity Type and adding of properties

			entityType = new CsdlEntityType();
			entityType.setName(ET_RESEARCHER_NAME);
			entityType.setProperties(Arrays.asList(id, fullName, creditName, researchInterests, description, title, email, position, transferabstract, transferkeywords));
			entityType.setKey(Collections.singletonList(propertyRef));
			entityType.setNavigationProperties(navPropList);

		} else if (entityTypeName.equals(ET_PERFORMER_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("id")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty familyName = new CsdlProperty().setName("familyName")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty givenName = new CsdlProperty().setName("givenName")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("name")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty uid = new CsdlProperty().setName("uid")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty type = new CsdlProperty().setName("type")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("id");

			entityType = new CsdlEntityType();
			entityType.setName(ET_PERFORMER_NAME);
			entityType.setProperties(Arrays.asList(id, familyName, givenName, name, uid, type));
			entityType.setKey(Arrays.asList(propertyRef));

		} else if (entityTypeName.equals(ET_PLACE_FQN)) {

			CsdlProperty id = new CsdlProperty().setName("id")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty alternateNames = new CsdlProperty().setName("alternateNames")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty categories = new CsdlProperty().setName("categories")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty().setName("description")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("name")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty subType = new CsdlProperty().setName("subType")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty uid = new CsdlProperty().setName("uid")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty type = new CsdlProperty().setName("type")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty url = new CsdlProperty().setName("url")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("id");

			CsdlNavigationProperty navPropPlaceSubProperties = new CsdlNavigationProperty()
					.setName("PlaceSubProperties").setType(ET_PLACE_SUB_PROPERTIES_FQN).setCollection(true);

			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			navPropList.add(navPropPlaceSubProperties);

			entityType = new CsdlEntityType();
			entityType.setName(ET_PLACE_NAME);
			entityType.setProperties(
					Arrays.asList(id, alternateNames, categories, description, name, subType, type, url, uid));
			entityType.setKey(Arrays.asList(propertyRef));
			entityType.setNavigationProperties(navPropList);

		}

		else if (entityTypeName.equals(ET_PLACE_SUB_PROPERTIES_FQN)) {
			CsdlProperty id = new CsdlProperty().setName("id")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty floors = new CsdlProperty().setName("floors")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("id");

			entityType = new CsdlEntityType();
			entityType.setName(ET_PLACE_SUB_PROPERTIES_NAME);
			entityType.setProperties(Arrays.asList(id, floors));
			entityType.setKey(Arrays.asList(propertyRef));

		}

		else if (entityTypeName.equals(ET_EVENT_SUB_PROPERTY_FQN)) {

			CsdlProperty id = new CsdlProperty().setName("id")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty semester = new CsdlProperty().setName("semester")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty originalCategory = new CsdlProperty().setName("originalCategory")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("id");

			entityType = new CsdlEntityType();
			entityType.setName(ET_EVENT_SUB_PROPERTY_NAME);
			entityType.setProperties(Arrays.asList(id, semester, originalCategory));
			entityType.setKey(Arrays.asList(propertyRef));

		}

		return entityType;
	}

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {
		// creation of OData Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);

		// adding EntityTypes to schema
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		entityTypes.add(getEntityType(ET_COURSE_FQN));
		entityTypes.add(getEntityType(ET_PERFORMER_FQN));
		entityTypes.add(getEntityType(ET_PLACE_FQN));
		entityTypes.add(getEntityType(ET_PLACE_SUB_PROPERTIES_FQN));
		entityTypes.add(getEntityType(ET_EVENT_SUB_PROPERTY_FQN));

		schema.setEntityTypes(entityTypes);

		// adding EntityContainer
		schema.setEntityContainer(getEntityContainer());

		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

}
