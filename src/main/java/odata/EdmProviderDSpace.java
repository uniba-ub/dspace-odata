package odata;

import java.util.ArrayList;

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
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

import entitys.EntityRegister;
import entitys.Project;
import entitys.Publication;

public class EdmProviderDSpace extends CsdlAbstractEdmProvider {

	// Service Namespace
	public final static String NAMESPACE = "dspace";

	// EDM Container
	public final static String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);
	
	//Functions
	public static final String FUNCTION_CSL_FOR_RESEARCHER = "cslforresearcher";
	public static final FullQualifiedName FUNCTION_CSL_FOR_RESEARCHER_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_RESEARCHER);

	public static final String FUNCTION_CSL_FOR_RESEARCHER_SELECTED = "cslforresearcherselected";
	public static final FullQualifiedName FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_RESEARCHER_SELECTED);
	
	public static final String FUNCTION_CSL_FOR_ORGUNIT = "cslfororgunit";
	public static final FullQualifiedName FUNCTION_CSL_FOR_ORGUNIT_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_ORGUNIT);
	
	public static final String FUNCTION_CSL_FOR_PROJECT = "cslforproject";
	public static final FullQualifiedName FUNCTION_CSL_FOR_PROJECT_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_PROJECT);
	
	public static final String FUNCTION_CSL_FOR_PUBLICATION = "cslforitem";
	public static final FullQualifiedName FUNCTION_CSL_FOR_PUBLICATION_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_PUBLICATION);	
	
	public static final String FUNCTION_CSL_FOR_JOURNAL = "cslforjournal";
	public static final FullQualifiedName FUNCTION_CSL_FOR_JOURNAL_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_JOURNAL);
	
	public static final String FUNCTION_CSL_FOR_SERIES = "cslforseries";
	public static final FullQualifiedName FUNCTION_CSL_FOR_SERIES_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_SERIES);

	public static final String FUNCTION_CSL_FOR_SUPERVISOR = "cslforsupervisor";
	public static final FullQualifiedName FUNCTION_CSL_FOR_SUPERVISOR_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_SUPERVISOR);

	public static final String FUNCTION_CSL_FOR_AUTHOR = "cslforauthor";
	public static final FullQualifiedName FUNCTION_CSL_FOR_AUTHOR_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_CSL_FOR_AUTHOR);
	
	public static final String FUNCTION_PJ_FOR_OU = "pjforouchild";
	public static final FullQualifiedName FUNCTION_PJ_FOR_OU_FQN = new FullQualifiedName(NAMESPACE, FUNCTION_PJ_FOR_OU);
	
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
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_RESEARCHER));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_RESEARCHER_SELECTED));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_SUPERVISOR));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_AUTHOR));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_PUBLICATION));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_PROJECT));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_ORGUNIT));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_JOURNAL));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_CSL_FOR_SERIES));
		functionImports.add(getFunctionImport(CONTAINER, FUNCTION_PJ_FOR_OU));
		
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
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_RESEARCHER_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_ORGUNIT_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_PROJECT_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_JOURNAL_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_SERIES_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_SUPERVISOR_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_AUTHOR_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_PUBLICATION_FQN));
		functions.addAll(getFunctions(FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN));
		functions.addAll(getFunctions(FUNCTION_PJ_FOR_OU_FQN));


		schema.setFunctions(functions);
		
		
		// adding EntityContainer
		schema.setEntityContainer(getEntityContainer());
		
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}
	
	@Override 
	public List<CsdlFunction> getFunctions(final FullQualifiedName functionName){
		if(functionName.equals(FUNCTION_CSL_FOR_RESEARCHER_FQN)) {
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterAuthorid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterAuthorid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_RESEARCHER_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		} else if(functionName.equals(FUNCTION_CSL_FOR_ORGUNIT_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterOrgunitid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOrgunitid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_ORGUNIT_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		} else if(functionName.equals(FUNCTION_CSL_FOR_PUBLICATION_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterOrgunitid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOrgunitid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_PUBLICATION_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_PROJECT_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterOrgunitid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOrgunitid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_PROJECT_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_JOURNAL_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterOrgunitid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOrgunitid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_JOURNAL_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_SERIES_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterOrgunitid = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOrgunitid);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_SERIES_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_SUPERVISOR_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterSupervId = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterSupervId);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_SUPERVISOR_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_AUTHOR_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterSupervId = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterSupervId);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_AUTHOR_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterStyle = new CsdlParameter();
			parameterStyle.setName("style");
			parameterStyle.setNullable(false);
			parameterStyle.setCollection(false);
			parameterStyle.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			parameterList.add(parameterStyle);
			
			final CsdlParameter parameterSupervId = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterSupervId);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Publication.ET_PUBLICATION_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN.getName())
					.setParameters(parameterList)
					.setReturnType(returnType)
					.setComposable(true);
			functions.add(function);
			
			return functions;
		}else if(functionName.equals(FUNCTION_PJ_FOR_OU_FQN)){
			
			final List<CsdlFunction> functions = new LinkedList<CsdlFunction>();
			final List<CsdlParameter> parameterList = new ArrayList<CsdlParameter>();
			
			final CsdlParameter parameterOuId = new CsdlParameter()
							.setName("id")
							.setNullable(false)
							.setCollection(false)
							.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			parameterList.add(parameterOuId);
			
			final CsdlReturnType returnType = new CsdlReturnType();
			returnType.setCollection(true);
			returnType.setType(Project.ET_PROJECT_FQN);
			
			final CsdlFunction function = new CsdlFunction();
			function.setName(FUNCTION_PJ_FOR_OU_FQN.getName())
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
			if(functionImportName.equals(FUNCTION_CSL_FOR_RESEARCHER_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_RESEARCHER_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);
			} else if(functionImportName.equals(FUNCTION_CSL_FOR_ORGUNIT_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_ORGUNIT_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_PROJECT_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_PROJECT_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_JOURNAL_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_JOURNAL_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_SERIES_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_SERIES_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_PUBLICATION_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_PUBLICATION_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_SUPERVISOR_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_SUPERVISOR_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_AUTHOR_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_AUTHOR_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_CSL_FOR_RESEARCHER_SELECTED_FQN)
						.setEntitySet(Publication.ES_PUBLICATIONS_NAME)
						.setIncludeInServiceDocument(true);		
			}else if(functionImportName.equals(FUNCTION_PJ_FOR_OU_FQN.getName())) {
				return new CsdlFunctionImport()
						.setName(functionImportName)
						.setFunction(FUNCTION_PJ_FOR_OU_FQN)
						.setEntitySet(Project.ES_PROJECTS_NAME)
						.setIncludeInServiceDocument(true);		
			}
		}
		return null;
	}
}
