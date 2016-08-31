package com.ahancer.rr.utils;

public final class EncodeUtil {
	
	private final static String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static long decode(String s) {
		final int B = SYMBOLS.length();
		long num = 0;
		for (char ch : s.toCharArray()) {
			num *= B;
			num += SYMBOLS.indexOf(ch);
		}
		return num;
	}
	
	public static String encode(long num) {
		final int B = SYMBOLS.length();
		StringBuilder sb = new StringBuilder();
		while (num != 0) {
			sb.append(SYMBOLS.charAt((int) (num % B)));
			num /= B;
		}
		return sb.reverse().toString();
	}
	
}
