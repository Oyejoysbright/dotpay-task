package org.dotpay.challenge.utils;

public class Helper {
    
	public static String randomString(int length) {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwxyz";
		String res = "";
		for (int i = 0; i < length; i++) {
			int pos = (int) Math.floor(Math.random() * chars.length());
			res += chars.substring(pos, pos + 1);
		}
		return res;
	}
}
