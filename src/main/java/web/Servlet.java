package web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;

import data.DataHandler;
import odata.EdmProviderDSpace;
import odata.EntityCollectionProcessor;
import odata.EntityProcessor;
import odata.PrimitiveProcessor;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Servlet.class);

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			HttpSession session = req.getSession(true);
			DataHandler entityDatabase = (DataHandler) session.getAttribute(DataHandler.class.getName());
			if (entityDatabase == null) {
				entityDatabase = new DataHandler();
				session.setAttribute(DataHandler.class.getName(), entityDatabase);

			}
			OData odata = OData.newInstance();
			ServiceMetadata edm = odata.createServiceMetadata(new EdmProviderDSpace(), new LinkedList<>());
			ODataHttpHandler handler = odata.createHandler(edm);
			handler.register(new EntityCollectionProcessor(entityDatabase));
			handler.register(new EntityProcessor(entityDatabase));
			handler.register(new PrimitiveProcessor(entityDatabase));

			handler.process(req, resp);
		} catch (RuntimeException e) {
			LOG.error("Server Error occurred in Servlet", e);
			throw new IOException(e);
		}
	}

}
