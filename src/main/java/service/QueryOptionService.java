package service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.edm.EdmElement;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByItem;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;

import data.TestDatabase;

public class QueryOptionService {

	private TestDatabase entityDatabase;

	public QueryOptionService(TestDatabase entityDatabase) {

		this.entityDatabase = entityDatabase;

	}

	public EntityCollection applyCountOption(CountOption countOption, List<Entity> entityList) {
		EntityCollection responseEntityCollection = new EntityCollection();
		if (countOption != null) {
			boolean isCount = countOption.getValue();
			if (isCount) {
				responseEntityCollection.setCount(entityList.size());

			}
		}

		return responseEntityCollection;
	}

	public List<Entity> applySkipOption(SkipOption skipOption, List<Entity> entityList)
			throws ODataApplicationException {
		if (skipOption != null) {
			int skipNumber = skipOption.getValue();
			if (skipNumber >= 0) {
				if (skipNumber <= entityList.size()) {
					entityList = entityList.subList(skipNumber, entityList.size());
				} else {
					entityList.clear();
				}
			} else {
				throw new ODataApplicationException("Invalid value for $skip",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
			}
		}

		return entityList;
	}

	public List<Entity> applyTopOption(TopOption topOption, List<Entity> entityList) throws ODataApplicationException {

		if (topOption != null) {
			int topNumber = topOption.getValue();
			if (topNumber >= 0) {
				if (topNumber <= entityList.size()) {
					entityList = entityList.subList(0, topNumber);
				}
			} else {
				throw new ODataApplicationException("Invalid value for $top",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
			}
		}

		return entityList;

	}
//TODO: wieder einfügen
	/*
	public EdmEntitySet applyExpandOptionOnCollection(ExpandOption expandOption, EdmEntitySet responseEdmEntitySet,
			EntityCollection entityCollection) throws ODataApplicationException {
		List<EdmNavigationProperty> edmNavigationPropertyList = new LinkedList<EdmNavigationProperty>();
		if (expandOption != null) {
			EdmNavigationProperty edmNavigationProperty = null;
			for (ExpandItem expandItem : expandOption.getExpandItems()) {

				if (expandItem.isStar()) {
					List<EdmNavigationPropertyBinding> bindings = responseEdmEntitySet.getNavigationPropertyBindings();
					if (!bindings.isEmpty()) {
						for (EdmNavigationPropertyBinding binding : bindings) {
							EdmElement property = responseEdmEntitySet.getEntityType().getProperty(binding.getPath());
							if (property instanceof EdmNavigationProperty) {
								edmNavigationProperty = (EdmNavigationProperty) property;
								edmNavigationPropertyList.add(edmNavigationProperty);
							}
						}
					}
				} else {
					UriResource navUriResource = expandItem.getResourcePath().getUriResourceParts().get(0);
					if (navUriResource instanceof UriResourceNavigation) {
						edmNavigationProperty = ((UriResourceNavigation) navUriResource).getProperty();
						edmNavigationPropertyList.add(edmNavigationProperty);

					}
				}
				if (edmNavigationPropertyList != null) {
					for (EdmNavigationProperty navProp : edmNavigationPropertyList) {

						String navPropName = navProp.getName();
						EdmEntityType expandEdmEntityType = navProp.getType();

						List<Entity> entityList = entityCollection.getEntities();

						for (Entity entity : entityList) {

							Link link = new Link();
							link.setTitle(navPropName);
							link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
							link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);

							if (navProp.isCollection()) {
								EntityCollection expandEntityCollection = entityDatabase
										.getRelatedEntityCollection(entity, expandEdmEntityType);
								FilterOption filterItemOption = expandItem.getFilterOption();
								if (filterItemOption != null) {
									List<Entity> expandedEntityList = expandEntityCollection.getEntities();
									expandedEntityList = applyFilterOption(expandedEntityList, filterItemOption);
								}

								link.setInlineEntitySet(expandEntityCollection);
								ExpandOption expandItemOption = expandItem.getExpandOption();
								if (expandItemOption != null) {
									responseEdmEntitySet = applyExpandOptionOnCollection(expandItemOption,
											responseEdmEntitySet, expandEntityCollection);
								}

							} else {
								Entity expandEntity = entityDatabase.getRelatedEntity(entity, expandEdmEntityType);
								link.setInlineEntity(expandEntity);

							}

							entity.getNavigationLinks().add(link);

						}

					}
				}
			}
		}
		return responseEdmEntitySet;

	}
	
	*/
	//TODO: wieder einfügen
/*
	public Entity applyExpandOptionOnEntity(ExpandOption expandOption, Entity responseEntity,
			EdmEntitySet responseEdmEntitySet) throws ODataApplicationException {

		if (expandOption != null) {
			EdmNavigationProperty edmNavigationProperty = null;

			List<EdmNavigationProperty> edmNavigationPropertyList = new LinkedList<EdmNavigationProperty>();
			for (ExpandItem expandItem : expandOption.getExpandItems()) {
				if (expandItem.isStar()) {
					List<EdmNavigationPropertyBinding> bindings = responseEdmEntitySet.getNavigationPropertyBindings();
					if (!bindings.isEmpty()) {
						for (EdmNavigationPropertyBinding binding : bindings) {
							EdmElement property = responseEdmEntitySet.getEntityType().getProperty(binding.getPath());
							if (property instanceof EdmNavigationProperty) {
								edmNavigationProperty = (EdmNavigationProperty) property;
								edmNavigationPropertyList.add(edmNavigationProperty);

							}

						}

					}

				} else {
					UriResource navUriResource = expandItem.getResourcePath().getUriResourceParts().get(0);
					if (navUriResource instanceof UriResourceNavigation) {
						edmNavigationProperty = ((UriResourceNavigation) navUriResource).getProperty();
						edmNavigationPropertyList.add(edmNavigationProperty);

					}

				}

				if (edmNavigationPropertyList != null) {

					for (EdmNavigationProperty navProp : edmNavigationPropertyList) {
						Link link = new Link();

						EdmEntityType expandEdmEntityType = navProp.getType();
						String navPropName = navProp.getName();
						link.setTitle(navPropName);
						link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
						link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);

						if (navProp.isCollection()) {
							EntityCollection expandEntityCollection = entityDatabase
									.getRelatedEntityCollection(responseEntity, expandEdmEntityType);
							ExpandOption expandItemOption = expandItem.getExpandOption();

							FilterOption filterItemOption = expandItem.getFilterOption();
							if (filterItemOption != null) {
								List<Entity> expandedEntityList = expandEntityCollection.getEntities();
								expandedEntityList = applyFilterOption(expandedEntityList, filterItemOption);
							}

							if (expandItemOption != null) {
								responseEdmEntitySet = applyExpandOptionOnCollection(expandItemOption,
										responseEdmEntitySet, expandEntityCollection);
							}

							link.setInlineEntitySet(expandEntityCollection);

						} else {
							Entity expandEntity = entityDatabase.getRelatedEntity(responseEntity, expandEdmEntityType);
							link.setInlineEntity(expandEntity);
							link.setHref(expandEntity.getId().toASCIIString());

						}

						responseEntity.getNavigationLinks().add(link);

					}

				}

			}

		}

		return responseEntity;
	}
*/
	public List<Entity> applyOrderByOption(OrderByOption orderByOption, List<Entity> entityList) {
		if (orderByOption != null) {
			List<OrderByItem> orderItemList = orderByOption.getOrders();
			final OrderByItem orderByItem = orderItemList.get(0);
			Expression expression = orderByItem.getExpression();
			if (expression instanceof Member) {
				UriInfoResource resourcePath = ((Member) expression).getResourcePath();
				UriResource uriResource = resourcePath.getUriResourceParts().get(0);
				if (uriResource instanceof UriResourcePrimitiveProperty) {
					EdmProperty edmProperty = ((UriResourcePrimitiveProperty) uriResource).getProperty();
					final String sortPropertyName = edmProperty.getName();
					final String type = edmProperty.getType().toString();

					Collections.sort(entityList, new Comparator<Entity>() {

						public int compare(Entity entity1, Entity entity2) {
							int compareResult = 0;
							if (type.equals("Edm.Int32")) {
								Integer integer1 = (Integer) entity1.getProperty(sortPropertyName).getValue();
								Integer integer2 = (Integer) entity2.getProperty(sortPropertyName).getValue();

								compareResult = integer1.compareTo(integer2);
							} else if (type.equals("Edm.String")) {
								String propertyValue1 = (String) entity1.getProperty(sortPropertyName).getValue();
								String propertyValue2 = (String) entity2.getProperty(sortPropertyName).getValue();

								compareResult = propertyValue1.compareTo(propertyValue2);
							}
							if (orderByItem.isDescending()) {
								return -compareResult;
							}

							return compareResult;
						}
					});

				}

			}

		}

		return entityList;

	}

	public List<Entity> applyFilterOption(List<Entity> entityList, FilterOption filterOption)
			throws ODataApplicationException {
		if (filterOption != null) {
			Expression expression = filterOption.getExpression();
			try {
				Iterator<Entity> iterator = entityList.iterator();
				while (iterator.hasNext()) {
					Entity entity = iterator.next();
					FilterExpressionVisitor expressionVisitor = new FilterExpressionVisitor(entity);

					Object visitorResult = expression.accept(expressionVisitor);

					if (visitorResult instanceof Boolean) {
						if (!Boolean.TRUE.equals(visitorResult)) {
							iterator.remove();
						}

					} else {

						throw new ODataApplicationException("A filter expression must evaulate to type Edm.Boolean",
								HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);

					}
				}
			} catch (ExpressionVisitException e) {
				throw new ODataApplicationException("Exception in filter evaluation",
						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);

			}
		}
		return entityList;

	}

}