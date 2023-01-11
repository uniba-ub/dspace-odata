package service;

import org.apache.solr.client.solrj.SolrQuery;

public class SolrQueryMaker {
	
	private final SolrQuery query;
	
	public SolrQueryMaker() {
		query = new SolrQuery();
	}

	public void setResponseLimitToMax() {
		query.setRows(Integer.MAX_VALUE);
	}

	public void setQuerySearchTerm(String term) {
		query.setQuery(term);
	}
	
	public void setODataSearchTerm(String search) {
		//Transform OData $search Expression to Solr Syntax
		//see $search in https://docs.oasis-open.org/odata/odata/v4.01/os/abnf/odata-abnf-construction-rules.txt
		query.addFilterQuery(search);
	}
	
	public void addSearchFilter(String filter) {
		if (filter != null) {
		query.addFilterQuery(filter);
		}
	}
	
	public void addSearchFilterForAttribute(String attributeName, String filter) {
		if (filter != null && attributeName != null) {
			query.addFilterQuery(attributeName+":"+filter);
		}
	}
	
	public SolrQuery getQuery() {
		return query;
	}
	
	public void resetQuery() {
		query.clear();
	}
	
	public void setSearchFilterForComplexProperty(int idOfSolrObject, int parentfk, String schema) {
		setQuerySearchTerm("resourcetype_filter:\"nobjects\n" + "|||\n" + "N-Object###default\"");
		String parentFKWithId = (parentfk+"-"+idOfSolrObject);
		query.addFilterQuery("search.parentfk:"+parentFKWithId);
		query.addFilterQuery("search.schema_s:"+schema);
	}
	
}
