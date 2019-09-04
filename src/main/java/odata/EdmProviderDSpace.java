package odata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

import entitys.EntityRegister;
import entitys.Publication;

public class EdmProviderDSpace extends CsdlAbstractEdmProvider {

	// Service Namespace
	public final static String NAMESPACE = "dspace";

	// EDM Container
	public final static String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);
	
	//Function
	public static final String FUNCTION_CSL = "csl";
	public static final FullQualifiedName FUNCTION_CSL_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL);
	
	public EntityRegister entityRegister;
	
	public EdmProviderDSpace() {
		entityRegister = new EntityRegister();
	}


	@Override
	public CsdlEntityContainer getEntityContainer() throws ODataException {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		for(String entitySetName: entityRegister.getEntitySetNameList()) {
			entitySets.add(getEntitySet(CONTAINER, entitySetName));		
		}
		
		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);
		
		//create function
		List<CsdlFunctionImport> functionImports = new LinkedList<CsdlFunctionImport>();
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL));
		entityContainer.setFunctionImports(functionImports);
		
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
			for(CsdlEntitySet item:entityRegister.getEntitySet() )
				if(entitySetName.equals(item.getName())) {
					entitySet = item;
				}
		}
		return entitySet;
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		CsdlEntityType entityType = null;
		for(CsdlEntityType item:entityRegister.getEntityTypeList()) {
			if(entityTypeName.equals(new FullQualifiedName(NAMESPACE, item.getName()))) {
				entityType = item;			
			}
		}
		
		return entityType;
	}

	@Override
	public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
		CsdlComplexType complexType = null;
		for(CsdlComplexType item:entityRegister.getComplexTypeList()) {
			if(complexTypeName.equals(new FullQualifiedName(NAMESPACE, item.getName()))) {
				complexType = item;
			}
		}
		return complexType;
	}

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {
		// creation of OData Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);
		schema.setEntityTypes(entityRegister.getEntityTypeList());
		schema.setComplexTypes(entityRegister.getComplexTypeList());
		
		//add function
		List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
		functions.addAll(getFunctions(FUNCTION_CSL_FQN));
		schema.setFunctions(functions);
		
		// adding EntityContainer
		schema.setEntityContainer(getEntityContainer());
		
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}
	
	@Override 
	public List<CsdlFunction> getFunctions(final FullQualifiedName functionName){
		if(functionName.equals(FUNCTION_CSL_FQN)) {
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new LinkedList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("Style");
			parameterStyle.setNullable(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}
		return null;
	}
	
	
	@Override
	public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer, String functionImportName) {
		if(entityContainer.equals(CONTAINER)) {
			if(functionImportName.equals(FUNCTION_CSL_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);
			}
		}
		return null;
	}
}
