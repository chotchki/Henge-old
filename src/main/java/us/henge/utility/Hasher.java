package us.henge.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Hasher {
	public static byte[] hash(File file) throws NoSuchAlgorithmException, IOException{
		MessageDigest hash = MessageDigest.getInstance("SHA-256");
		try (FileInputStream fis = new FileInputStream(file);
			 DigestInputStream dis = new DigestInputStream(fis, hash)){
			byte[] buffer = new byte[1024 * 1024 * 1024];
			int length = -1;
			do {
				length = dis.read(buffer);
			} while (length != -1);
		}
		
		return hash.digest();
	}
}
