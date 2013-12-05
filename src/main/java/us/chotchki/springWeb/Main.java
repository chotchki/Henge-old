package us.chotchki.springWeb;

import org.eclipse.jetty.server.Server;

public class Main {
	private static int httpPort = 9000;
	
	public static void main(String[] args) {
		Server server = new Server(httpPort);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
