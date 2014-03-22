package us.henge.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.henge.utility.Hasher;

@Service
public class ContentService {
	private static final Logger log = LoggerFactory.getLogger(ContentService.class);
	
	@Autowired
	private InitParamService initParamService = null;
	
	/**
	 * Adds content to the store via filesystem copy
	 * @param source
	 * @return hash of the 
	 * @throws Exception 
	 */
	public byte[] copyContent(File source) throws Exception{
		byte[] hash = Hasher.hash(source);
		
		File target = getTargetPath(hash);
		
		Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		return hash;
	}
	
	private File getTargetPath(byte[] hash) throws Exception{
		String hash64 = Base64.encodeBase64URLSafeString(hash);
		
		String first = hash64.substring(0, 1);
		String second = hash64.substring(1, 3);
		
		File targetDir = new File(initParamService.getDataDirectory() + File.separator + "photos" + File.separator + first + File.separator + second);
		if(!targetDir.isDirectory()){
			targetDir.mkdirs();
		}
		if(!targetDir.isDirectory()){
			throw new Exception(targetDir.getPath() + " could not be created!");
		}
		
		return new File(targetDir.getPath() + File.separator + hash64);
	}
}
