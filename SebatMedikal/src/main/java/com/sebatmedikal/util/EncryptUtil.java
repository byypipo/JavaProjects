package com.sebatmedikal.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptUtil {
	private static String DEFAULT_ALGORITHM = "DES";

	public static String encode(String text) throws Exception {
		return encode(text, DEFAULT_ALGORITHM);
	}

	public static String encode(String text, String algorithm) throws Exception {
		if (CompareUtil.equal(algorithm, "AES")) {
			return encodeAES(text);
		} else if (CompareUtil.equal(algorithm, "DES")) {
			return encodeDES(text);
		}

		return "UNKNOWN_ALGORITHM";
	}

	public static String decode(String text) throws Exception {
		return decode(text, DEFAULT_ALGORITHM);
	}

	public static String decode(String text, String algorithm) throws Exception {
		if (CompareUtil.equal(algorithm, "AES")) {
			return decodeAES(text);
		} else if (CompareUtil.equal(algorithm, "DES")) {
			return decodeDES(text);
		}

		return "UNKNOWN_ALGORITHM";
	}

	public static String encodeDES(String text) throws Exception {
		if (NullUtil.isNull(text)) {
			return "";
		}

		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		keyGenerator.init(56);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] encoded = secretKey.getEncoded();

		SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, "DES");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encrypted = cipher.doFinal(text.getBytes());

		return Base64.encodeBase64String(encoded) + Base64.encodeBase64String(encrypted);
	}

	public static String decodeDES(String text) throws Exception {
		if (NullUtil.isNull(text)) {
			return "";
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(text.substring(0, 12)), "DES");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] original = cipher.doFinal(Base64.decodeBase64(text.substring(12)));
		return new String(original);
	}

	public static String encodeAES(String text) throws Exception {
		if (NullUtil.isNull(text)) {
			return "";
		}

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128, new SecureRandom());
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] encoded = secretKey.getEncoded();

		SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encrypted = cipher.doFinal(text.getBytes());

		return Base64.encodeBase64String(encoded) + Base64.encodeBase64String(encrypted);
	}

	public static String decodeAES(String text) throws Exception {
		if (NullUtil.isNull(text)) {
			return "";
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(text.substring(0, 24)), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] original = cipher.doFinal(Base64.decodeBase64(text.substring(24)));
		return new String(original);
	}

	public static void main(final String[] args) throws Exception {
		String text = "/KcPAuUqiquRdMAhERsSTw==f8K3AsK50dxUvogHyKsxEj98tiJXuy+bnd7vcVYmK9spVh8WSk1qUOFqQq+WsGgt86hhmWK5Rwe7lU2VVEBWRw==";

		System.out.println("encoded:\t" + text);
		text = decodeAES(text);
		System.out.println("decoded:\t" + text);

	}
}
