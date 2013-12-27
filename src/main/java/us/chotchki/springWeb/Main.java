package us.chotchki.springWeb;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eluder.jetty.server.EmbeddedJetty;
import org.eluder.jetty.server.ServerConfig;
import org.hsqldb.jdbc.JDBCPool;

public class Main {
	private static int httpPort = 9000;

	public static void main(String[] args) {
		// Setup Jetty
		try {
            ServerConfig config = new ServerConfig()
                    .setPort(httpPort)
                    .setBaseResource("classpath:webapp")
                    .setClassPath(true);
            new EmbeddedJetty(config) {
                @Override
                protected void start(final Server server) throws Exception {
                    setupDb(server);
                    super.start(server);
                }
            }.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setupDb(Object scope) throws NamingException {
		JDBCPool pool = new JDBCPool();
		pool.setUrl("jdbc:hsqldb:mem:mymemdb");
		pool.setUser("SA");
		pool.setPassword("");
		Resource resource = new Resource(null, "jdbc/DB", pool); //Argh! Null must be passed or it will not work!
	}
}
