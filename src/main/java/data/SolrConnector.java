package data;

import java.io.IOException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import entitys.EntityRegister;

public class SolrConnector {

	SolrClient solr;
	
	public void connectToDSpaceSolr() throws SolrServerException, IOException {
		
		String urlString = "http://dspace-bamberg-725.fis-dev1.rz.uni-bamberg.de/solr/search";
		solr = new HttpSolrClient.Builder(urlString).build();
		System.out.println(solr.ping());
		
	}
	
	public SolrDocumentList getData(SolrQuery query) throws SolrServerException, IOException {
		System.out.println(query.toQueryString());
    	QueryResponse response = solr.query(query);

		System.out.println("hallo2:solrgetData");

		SolrDocumentList list = response.getResults();	
		System.out.println("hallo3:solrgetData");

		return list;

	}


}
