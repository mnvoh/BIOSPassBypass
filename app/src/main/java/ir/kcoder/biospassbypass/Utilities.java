package ir.kcoder.biospassbypass;

import java.util.Random;

public class Utilities {
	
	public static String randomString( int length ) {
		if( length <= 0 ) return "";
		String retval = "";
		String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		for( int i = 0; i < length; i++ ) {
			int index = Math.abs( random.nextInt() ) % charset.length();
			retval += charset.substring( index, index + 1);
		}
		
		return retval;
	}
	public static String randomString( int length, String charset ) {
		if( length <= 0 ) return "";
		String retval = "";
		Random random = new Random();
		for( int i = 0; i < length; i++ ) {
			int index = Math.abs( random.nextInt() ) % charset.length();
			retval += charset.substring( index, index + 1);
		}
		
		return retval;
	}
}
