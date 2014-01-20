package us.chotchki.springWeb.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;

import javax.servlet.ServletContext;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import us.chotchki.springWeb.init.Config.Keys;

@Service
public class TokenService implements ServletContextAware {
	private static final Logger log = LoggerFactory.getLogger(TokenService.class);
	
	private static final String tokenFile = "token.txt";
	
	private final String token;
	
	public TokenService(){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[16];
		random.nextBytes(bytes);
		
		token = DatatypeConverter.printBase64Binary(bytes);
	}
	
	public String getToken() {
		return token;
	}

	@Override
	public void setServletContext(ServletContext sc) {
		try {
			Files.write(new File(sc.getInitParameter(Keys.dataPath.toString()) + File.separator + tokenFile).toPath(), token.getBytes());
		} catch (IOException e) {
			log.error("Unable to write the current security token to file! Users will not able to register.");
		}
	}
}
