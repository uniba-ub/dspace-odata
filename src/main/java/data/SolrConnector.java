package data;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import service.SolrQueryMaker;


public class SolrConnector {

	SolrClient solr;
	
	public void connectToDSpaceSolr() {
		String urlString = System.getenv("SOLR_URL");
		solr = new HttpSolrClient.Builder(urlString).build();
	}
	
	public SolrDocumentList getData(SolrQueryMaker queryMaker) throws IOException {
		try {
			connectToDSpaceSolr();
			QueryResponse response = solr.query(queryMaker.getQuery());
			SolrDocumentList list = response.getResults();
			queryMaker.resetQuery();
			solr.close();
			return list;
		} catch(Exception e) {
			e.printStackTrace();
			//Close connection when Error occurs
			queryMaker.resetQuery();
			solr.close();
			return null;
		}
		
	}



}
