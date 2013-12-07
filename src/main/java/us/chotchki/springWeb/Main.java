package us.chotchki.springWeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;

import us.chotchki.springWeb.init.FixedAnnotationConfig;

public class Main {
	private static int httpPort = 9000;

	public static void main(String[] args) {
		// Setup Jetty
		Server server = new Server(httpPort);

		try {
			server.setHandler(createHandlers());
			server.setStopAtShutdown(true);
			
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Handler createHandlers() throws IOException {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");

		_ctx.setResourceBase(new ClassPathResource("webapp").getURI().toString());
		_ctx.setConfigurations(new Configuration[] { new FixedAnnotationConfig() });
		
		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts });

		return _result;
	}
}
