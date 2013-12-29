package us.chotchki.springWeb.db.service;

import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
	private static final Logger log = LoggerFactory.getLogger(TokenService.class);
	
	private final String token;
	
	public TokenService(){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[16];
		random.nextBytes(bytes);
		
		token = DatatypeConverter.printBase64Binary(bytes);
		log.warn("The security token is {}", token);
	}
	
	public String getToken() {
		return token;
	}
}
