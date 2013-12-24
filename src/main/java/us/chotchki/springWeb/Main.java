package us.chotchki.springWeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hsqldb.jdbc.JDBCPool;
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
			setupDb(server);
			
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Handler createHandlers() throws IOException, NamingException {
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
	
	private static void setupDb(Object scope) throws NamingException {
		JDBCPool pool = new JDBCPool();
		
		String dbDir = System.getProperty("db");
		if(dbDir == null) {
			pool.setUrl("jdbc:hsqldb:mem:mymemdb");
		} else {
			pool.setUrl("jdbc:hsqldb:file:" + dbDir + ";shutdown=true;hsqldb.default_table_type=cached");
		}
		pool.setUser("SA");
		pool.setPassword("");
		Resource resource = new Resource(null, "jdbc/DB", pool); //Argh! Null must be passed or it will not work!
	}
}
