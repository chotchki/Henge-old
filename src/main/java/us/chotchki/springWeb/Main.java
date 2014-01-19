package us.chotchki.springWeb;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hsqldb.jdbc.JDBCPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import us.chotchki.springWeb.init.Config;
import us.chotchki.springWeb.init.Config.Keys;
import us.chotchki.springWeb.init.SetupLogging;
import us.chotchki.springWeb.init.jetty.FixedAnnotationConfig;

public class Main {
	private static int httpPort = 9000;

	public static void main(String[] args) {
		
		SetupLogging sl = new SetupLogging();
		sl.startConsole();
		
		Logger mainLog = LoggerFactory.getLogger(Main.class);
		if(args.length != 1){
			mainLog.error("Data directory was not supplied.");
			return;
		}
		
		File dataDir = new File(args[0]);
		if(!dataDir.exists()){
			mainLog.error("The data directory {} does not exist", dataDir.getPath());
			return;
		}
		
		Config conf;
		try {
			conf = new Config(dataDir);
		} catch (Exception e1) {
			mainLog.error("Unable to start up due to an error", e1);
			return;
		}
		
		sl.startFile(conf.getParentPath());
		
		// Setup Jetty
		Server server = new Server(httpPort);

		try {
			server.setHandler(createHandlers(conf.getSettings()));
			server.setStopAtShutdown(true);
			setupDb(conf.getParentPath() + File.separator + "db");
		
			KeyStore ks = conf.getKeyStore();
			if(ks != null){
				SslContextFactory scf = new SslContextFactory();
				scf.setKeyStore(ks);
				scf.setCertAlias("jetty");
				scf.addExcludeCipherSuites("TLS_DHE_.*");
				scf.setSessionCachingEnabled(true);
				scf.setIncludeProtocols("TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3");
				scf.setKeyStorePassword(conf.getSettings().getProperty(Keys.password.toString()));
				
				HTTPSPDYServerConnector hssc = new HTTPSPDYServerConnector(server, scf);
				hssc.setPort(9443);
				server.addConnector(hssc);
			}
			
			//Disable Server Signature
			for(Connector y : server.getConnectors()) {
			    for(ConnectionFactory x  : y.getConnectionFactories()) {
			        if(x instanceof HttpConnectionFactory) {
			            ((HttpConnectionFactory)x).getHttpConfiguration().setSendServerVersion(false);
			        }
			    }
			}
			
			server.start();
			server.join();
		} catch (Exception e) {
			mainLog.error("Encountered an error", e);
		}
	}

	private static Handler createHandlers(Properties settings) throws IOException, NamingException {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");
		_ctx.setResourceBase(new ClassPathResource("webapp").getURI().toString());
		_ctx.setConfigurations(new Configuration[] { new FixedAnnotationConfig()});
		
		for(Entry<Object, Object> e : settings.entrySet()){
			_ctx.setInitParameter((String) e.getKey(), (String) e.getValue());
		}
		return _ctx;
	}
	
	private static void setupDb(String dbDir) throws NamingException {
		JDBCPool pool = new JDBCPool();
		pool.setUrl("jdbc:hsqldb:file:" + dbDir + File.separator + "db" + ";shutdown=true;hsqldb.default_table_type=cached");
		pool.setUser("SA");
		pool.setPassword("");
		new Resource(null, "jdbc/DB", pool); //Argh! Null must be passed or it will not work!
	}
}
