package com.ahancer.rr.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public final class EncodeUtil {

	private final static String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static long decodeLong(String s) {
		final int B = SYMBOLS.length();
		long num = 0;
		for (char ch : s.toCharArray()) {
			num *= B;
			num += SYMBOLS.indexOf(ch);
		}
		return num;
	}

	public static String encodeLong(long num) {
		final int B = SYMBOLS.length();
		StringBuilder sb = new StringBuilder();
		while (num != 0) {
			sb.append(SYMBOLS.charAt((int) (num % B)));
			num /= B;
		}
		return sb.reverse().toString();
	}

	public static Object decodeObject( String s ) throws IOException , ClassNotFoundException {
		byte [] data = Base64.getDecoder().decode( s );
		ObjectInputStream ois = new ObjectInputStream( 
				new ByteArrayInputStream(  data ) );
		Object o  = ois.readObject();
		ois.close();
		return o;
	}

	public static String encodeObject( Serializable o ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( o );
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}

}
