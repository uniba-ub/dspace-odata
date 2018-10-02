package service;

import java.util.List;

import org.apache.commons.math3.transform.RealTransformer;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.solr.client.solrj.SolrQuery;

import entitys.EntityRegister;
import entitys.Publication;

public class SolrQueryMaker {
	
	private SolrQuery query;
	
	public SolrQueryMaker(){
		
		query = new SolrQuery();
	}
		

	public void setResponseLimit(int limit) {		
			query.setRows(limit);
	}
	
	public void setResponseLimitToMax() {
		
			query.setRows(9999);
	}

	public void setQuerySearchTerm(String term) {
		
		query.setQuery(term);
	}


	public void limitResultAttributes(List<String> attributeList) {
		String attributes = "";
		for(String item:attributeList) {
			if(attributes.isEmpty()) {
				attributes=item;
			} else {
				
				attributes=(attributes + "," + item);
			}	
		}	
		query.setFields(attributes);			

	}
	
	public void limitResultToAttribute(String attribute) {
		query.setFields(attribute);	
	}
	
	public void addSearchFilter(String filter) {
		query.setFilterQueries(filter);
	}
	
	public void addSearchFilters(List<String> filters) {
		for(String item: filters) {
			query.addFilterQuery(item);
		}	
	}
	
	public void addSearchFilterForAttribute(String attributeName, String filter) {
		query.addFilterQuery(attributeName+":"+filter);
	}
	
	
	public SolrQuery getQuery() {
		return query;
	}
	
	
	public void setFilterForRelation(FullQualifiedName relatedEntityFqn, int crisID) {
		if(relatedEntityFqn.equals(Publication.ET_PUBLICATION_FQN)) {
			query.addFilterQuery("dc.contributor.author_authority:\'"+crisID+"\'");
		}

		
	}
	
	
}
