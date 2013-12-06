package us.chotchki.springWeb;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import us.chotchki.springWeb.init.FixedAnnotationConfig;

public class Main {
	private static int httpPort = 9000;

	public static void main(String[] args) {
		// Setup Jetty
		Server server = new Server(httpPort);
		org.eclipse.jetty.util.log.Log.getRootLogger().setDebugEnabled(true);

		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Handler createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");
		_ctx.setBaseResource(Resource.newClassPathResource("META-INF/webapp"));
		// _ctx.getMetaData().addContainerResource(Resource.newClassPathResource());

		_ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*\\.class");
		_ctx.setConfigurations(new Configuration[] { new FixedAnnotationConfig() });

		//ContainerInitializer ci = (ContainerInitializer) _ctx.getAttribute(AnnotationConfiguration.CONTAINER_INITIALIZERS);
		//ci.addApplicableTypeName("us.chotchki.springWeb.init.WebAppInit");
		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts });

		return _result;
	}
}
