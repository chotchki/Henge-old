package us.henge.utility.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.context.VariablesMap;

public class KeyCreation implements HttpSessionListener {
	private static final SecureRandom random = new SecureRandom();
	
	private static final String enc = "AES/CBC/PKCS5Padding";
	private static final String hash = "HmacSHA256";
	
	public static final int ivSize = 16;
	public static final int hashSize;
	
	static {
		final byte[] testKey = new byte[16];
		random.nextBytes(testKey);
		try {
			Mac hmac = Mac.getInstance(hash);
			hmac.init(new SecretKeySpec(testKey, hash));
			hashSize = hmac.doFinal(" ".getBytes()).length;
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Unable to determine hash size " + hash);
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Invalid cipher algorithum");
		}
		keyGen.init(128, random);
		session.setAttribute(enc, keyGen.generateKey());
		
		final byte[] hashBytes = new byte[16];
		random.nextBytes(hashBytes);
		session.setAttribute(hash, new SecretKeySpec(hashBytes, hash));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {}
	
	public static String decrypt(HttpSession session, String cipherString) throws Exception{
		SecretKeySpec cipherKey = (SecretKeySpec) session.getAttribute(enc);
		SecretKeySpec hmacKey = (SecretKeySpec) session.getAttribute(hash);
		
		if(cipherKey == null || hmacKey == null){
			throw new Exception("Missing required session variables!");
		}
		
		if(!Base64.isBase64(cipherString)){
			throw new Exception("Not a valid Base64 String");
		}
		
		byte[] encBytes = Base64.decodeBase64(cipherString);
		if(encBytes.length < ivSize + hashSize + ivSize){
			throw new Exception("Not a long enough encoded length");
		}
		
		byte[] iv = new byte[ivSize];
		System.arraycopy(encBytes, 0, iv, 0, iv.length);
		
		byte[] hmacOutput = new byte[hashSize];
		System.arraycopy(encBytes, ivSize, hmacOutput, 0, hmacOutput.length);
		
		byte[] cipherText = new byte[encBytes.length - iv.length - hmacOutput.length];
		System.arraycopy(encBytes, iv.length + hmacOutput.length, cipherText, 0, cipherText.length);
		
		final Mac hmac = Mac.getInstance(hash);
		hmac.init(hmacKey);
		hmac.update(iv);
		byte[] calcHmac = hmac.doFinal(cipherText);
		
		if(!Arrays.equals(hmacOutput, calcHmac)){
			throw new Exception("Hmac is invalid");
		}
		
		final Cipher cipher = Cipher.getInstance(enc);
		cipher.init(Cipher.DECRYPT_MODE, cipherKey, new IvParameterSpec(iv));
		byte[] clearText = cipher.doFinal(cipherText);
		
		return new String(clearText, StandardCharsets.UTF_8);
	}
	
	public static String encrypt(VariablesMap<String, Object> vm, String plain) throws Exception{
		SecretKeySpec cipherKey = (SecretKeySpec) vm.get(enc);
		SecretKeySpec hmacKey = (SecretKeySpec) vm.get(hash);
		
		return encryptInner(cipherKey, hmacKey, plain);
	}
	
	public static String encrypt(HttpSession session, String plain) throws Exception {
		SecretKeySpec cipherKey = (SecretKeySpec) session.getAttribute(enc);
		SecretKeySpec hmacKey = (SecretKeySpec) session.getAttribute(hash);
		
		return encryptInner(cipherKey, hmacKey, plain);
	}
	
	private static String encryptInner(SecretKeySpec cipherKey, SecretKeySpec hmacKey, String plain) throws Exception{
		if(cipherKey == null || hmacKey == null){
			throw new Exception("Missing required session variables!");
		}
		
		byte[] iv = new byte[ivSize];
		random.nextBytes(iv);
		
		final Cipher cipher = Cipher.getInstance(enc);
		cipher.init(Cipher.ENCRYPT_MODE, cipherKey, new IvParameterSpec(iv));
		byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
		
		final Mac hmac = Mac.getInstance(hash);
		hmac.init(hmacKey);
		hmac.update(iv);
		byte[] hmacOutput = hmac.doFinal(cipherText);
		
		byte[] encBytes = Arrays.copyOf(iv, iv.length + hmacOutput.length + cipherText.length);
		System.arraycopy(hmacOutput, 0, encBytes, iv.length, hmacOutput.length);
		System.arraycopy(cipherText, 0, encBytes, iv.length + hmacOutput.length, cipherText.length);
		
		return Base64.encodeBase64URLSafeString(encBytes);
	}
}