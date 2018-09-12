package data;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;


public class SolrConnector {

	SolrClient solr;
	
	public void connectToDSpaceSolr() throws SolrServerException, IOException {
		
		String urlString = "http://dspace-bamberg-725.fis-dev1.rz.uni-bamberg.de/solr/search";
		solr = new HttpSolrClient.Builder(urlString).build();
	}
	
	public SolrDocumentList getData(SolrQuery query) throws SolrServerException, IOException {
		connectToDSpaceSolr();
    	QueryResponse response = solr.query(query);
		SolrDocumentList list = response.getResults();	
		solr.close();
		return list;

		
	}



}
