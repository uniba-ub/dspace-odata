package web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edmx.EdmxReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.TestDatabase;
import service.EdmProviderDSpace;
import service.EntityCollectionProcessor;
import service.EntityProcessor;
import service.PrimitiveProcessor;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Servlet.class);

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			HttpSession session = req.getSession(true);
			TestDatabase entityDatabase = (TestDatabase) session.getAttribute(TestDatabase.class.getName());
			if (entityDatabase == null) {
				entityDatabase = new TestDatabase();
				session.setAttribute(TestDatabase.class.getName(), entityDatabase);

			}
			OData odata = OData.newInstance();
			ServiceMetadata edm = odata.createServiceMetadata(new EdmProviderDSpace(), new ArrayList<EdmxReference>());
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
