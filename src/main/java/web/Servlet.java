package web;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.DataHandler;
import odata.EdmProviderDSpace;
import odata.EntityCollectionProcessor;
import odata.EntityProcessor;
import odata.PrimitiveProcessor;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Servlet.class);

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			HttpSession session = req.getSession(true);
			DataHandler entityDatabase = (DataHandler) session.getAttribute(DataHandler.class.getName());
			if (entityDatabase == null) {
				entityDatabase = new DataHandler();
				session.setAttribute(DataHandler.class.getName(), entityDatabase);

			}
			OData odata = OData.newInstance();
			ServiceMetadata edm = odata.createServiceMetadata(new EdmProviderDSpace(), new LinkedList<EdmxReference>());
			ODataHttpHandler handler = odata.createHandler(edm);
			handler.register(new EntityCollectionProcessor(entityDatabase));
			handler.register(new EntityProcessor(entityDatabase));
			handler.register(new PrimitiveProcessor(entityDatabase));

			handler.process(req, resp);
		} catch (RuntimeException e) {
			LOG.error("Server Error occurred in Servlet", e);
			throw new ServletException(e);
		}
	}

}
