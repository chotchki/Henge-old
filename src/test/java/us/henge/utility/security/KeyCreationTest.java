package us.henge.utility.security;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

public class KeyCreationTest {
	private KeyCreation kc = new KeyCreation();
	private String test = "TestString";

	@Test
	public void good() throws Exception {
		HttpSession session = new MockHttpSession();
		kc.sessionCreated(new HttpSessionEvent(session));
		
		
		String encrypted = KeyCreation.encrypt(session, test);
		String decrypted = KeyCreation.decrypt(session, encrypted);
		assertThat(decrypted).isEqualTo(test);
	}
	
	@Test
	public void goodLong() throws Exception {
		HttpSession session = new MockHttpSession();
		kc.sessionCreated(new HttpSessionEvent(session));
		
		String testLong = test + test + test + test; //Enough to cross the block size
		
		String encrypted = KeyCreation.encrypt(session, testLong);
		String decrypted = KeyCreation.decrypt(session, encrypted);
		assertThat(decrypted).isEqualTo(testLong);
	}

	@Test(expected=Exception.class)
	public void testEncrypt() throws Exception {
		HttpSession session = new MockHttpSession();
		kc.sessionCreated(new HttpSessionEvent(session));
		
		
		String encrypted = KeyCreation.encrypt(session, test);
		encrypted = encrypted.toLowerCase();
		KeyCreation.decrypt(session, encrypted);
	}

}
