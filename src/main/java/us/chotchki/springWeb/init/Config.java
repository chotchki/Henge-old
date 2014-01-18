package us.chotchki.springWeb.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * data/
 * data/settings.properties
 * 
 * data/keystore.jks
 * 
 * data/cache
 * data/db
 * data/log
 * data/photos
 * 
 * @author chotchki
 *
 */
public class Config {
	private static final Logger log = LoggerFactory.getLogger(Config.class);
	
	private final File parentPath;
	
	private final Properties settings = new Properties();
	private KeyStore keyStore = null;
	
	public Config(File parentPath) throws Exception{
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		this.parentPath = parentPath;
		
		File sPath = new File(parentPath.getPath() + File.separator + "settings.properties");
		try (FileReader reader = new FileReader(sPath)) {
			settings.load(reader);
		} catch (IOException e) {
			settings.clear();
		}
		
		File kPath = new File(parentPath.getPath() + File.separator + "keystore.jks");
		try(FileInputStream fis = new FileInputStream(kPath)){
			String password = settings.getProperty(Keys.password.toString(), "");
			keyStore.load(fis, password.toCharArray());
		} catch (IOException | NoSuchAlgorithmException | CertificateException e) {
			keyStore = null;
			log.error("Had an error loading the keystore {}, ssl is disabled", kPath);
			
			if(settings.containsKey(Keys.forceSSL.toString())){
				log.error("Force SSL requested, stopping startup");
				throw new Exception("SSL unable to startup.");
			}
		}
		
		String[] subDirs = {"cache", "db", "log", "photos"};
		for(String dir : subDirs){
			File sDir = new File(parentPath.getPath() + File.separator + dir);
			sDir.mkdir();
			
			if(!sDir.isDirectory()){
				throw new Exception("Directory does not exist " + sDir.getPath());
			}
		}
	}
	
	public File getParentPath() {
		return parentPath;
	}
	
	public Properties getSettings() {
		return settings;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}
	
	public enum Keys {
		password,
		forceSSL,
		googleApiKey
	}
}
