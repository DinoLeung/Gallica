package com.commonsense.hkgalden.security;

import java.security.*;



import javax.crypto.*;

import javax.crypto.spec.SecretKeySpec;



public class AESClientEncodeDecode {



	private static final String key = "v@!#1SF5~6A5XZE3";

	private static final String method = "AES";



	public static String changeByteToHex(byte[] a) {

		StringBuilder sb = new StringBuilder();

		for (byte d : a) {

			sb.append(String.format("%02X", d));

		}

		return sb.toString();

	}



	public static byte[] changeHexToByte(String a) {

		byte[] tmpb = new byte[a.length() / 2];

		for (int i = 0; i < a.length(); i += 2) {

			tmpb[i / 2] = (byte) ((Character.digit(a.charAt(i), 16) << 4) + Character.digit(a.charAt(i + 1), 16));

		}

		return tmpb;

	}



	public static String decrypt(String input) {

		String result = "";

		try {



			Cipher cipher = Cipher.getInstance(method);



			cipher.init(Cipher.DECRYPT_MODE, keyGen(key));

			byte[] original = cipher.doFinal(changeHexToByte(input));

			String originalString = new String(original);

			result = originalString;

		} catch (NoSuchAlgorithmException ex) {

			ex.printStackTrace();

		} catch (NoSuchPaddingException ex) {

			ex.printStackTrace();

		} catch (InvalidKeyException ex) {

			ex.printStackTrace();

		} catch (IllegalBlockSizeException ex) {

			ex.printStackTrace();

		} catch (BadPaddingException ex) {

			ex.printStackTrace();

		}

		return result;



	}



	public static String encrypt(String input) {

		String encypr = "";

		try {



			Cipher cipher = Cipher.getInstance(method);



			cipher.init(Cipher.ENCRYPT_MODE, keyGen(key));



			byte[] encrypted = cipher.doFinal(input.getBytes());

			encypr = changeByteToHex(encrypted);



		} catch (NoSuchAlgorithmException ex) {

			ex.printStackTrace();

		} catch (NoSuchPaddingException ex) {

			ex.printStackTrace();

		} catch (InvalidKeyException ex) {

			ex.printStackTrace();

		} catch (IllegalBlockSizeException ex) {

			ex.printStackTrace();

		} catch (BadPaddingException ex) {

			ex.printStackTrace();

		}

		return encypr;

	}



	public static SecretKeySpec keyGen(String s) throws NoSuchAlgorithmException {

		KeyGenerator kgen = KeyGenerator.getInstance(method);

		kgen.init(128); // 192 and 256 bits may not be available

		byte[] raw = s.getBytes();

		SecretKeySpec result = new SecretKeySpec(raw, method);

		// Instantiate the cipher

		return result;

	}



}


