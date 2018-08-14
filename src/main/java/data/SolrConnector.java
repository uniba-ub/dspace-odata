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
	
	public void connectToDSpaceSolr() {
		
		String urlString = "http://dspace-bamberg-725.fis-dev1.rz.uni-bamberg.de/solr/search";
		solr = new HttpSolrClient.Builder(urlString).build();
		
	}
	
	public SolrDocumentList getData(SolrQuery query) throws SolrServerException, IOException {
		System.out.println("hallo1:solrgetData");
    	QueryResponse response = solr.query(query);
		System.out.println("hallo2:solrgetData");

		SolrDocumentList list = response.getResults();	
		System.out.println("hallo3:solrgetData");

		return list;

	}


}
