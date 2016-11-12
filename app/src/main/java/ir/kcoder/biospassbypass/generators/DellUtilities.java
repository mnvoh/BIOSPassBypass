package ir.kcoder.biospassbypass.generators;

public class DellUtilities {
	public static char[] toByte(long[] arr) {
		char[] newarr = new char[arr.length];
	    for(int i = 0; i < arr.length; i++) {
	    	newarr[i] = (char)arr[i];
	    }
	    return newarr;
	}
	public static char[] toByte(int[] arr) {
		char[] newarr = new char[arr.length];
	    for(int i = 0; i < arr.length; i++) {
	    	newarr[i] = (char)arr[i];
	    }
	    return newarr;
	}
	
	/************************************/
	
	public static int[] toInt(long[] arr) {
		int[] newarr = new int[arr.length];
	    for(int i = 0; i < arr.length; i++) {
	    	newarr[i] = (int)arr[i];
	    }
	    return newarr;
	}
	public static int[] toInt(char[] arr) {
		int[] newarr = new int[arr.length];
	    for(int i = 0; i < arr.length; i++) {
	    	newarr[i] = (int)arr[i];
	    }
	    return newarr;
	}
	public static int[] toInt(short[] arr) {
		int[] newarr = new int[arr.length];
	    for(int i = 0; i < arr.length; i++) {
	    	newarr[i] = (int)arr[i];
	    }
	    return newarr;
	}
	
	/************************************/
	
	public static char[] IntToByteArr(int num) {
	    char[] ret_arr = new char[4];
	    ret_arr[0] = (char)(num & 0xFF);
	    ret_arr[1] = (char)((num >> 8) & 0xFF);
	    ret_arr[2] = (char)((num >> 16) & 0xFF);
	    ret_arr[3] = (char)((num >> 24) & 0xFF);
	    return ret_arr;
	}
    public static char[] IntToByteArr(long num) {
	    char[] ret_arr = new char[4];
	    ret_arr[0] = (char)(num & 0xFF);
	    ret_arr[1] = (char)((num >> 8) & 0xFF);
	    ret_arr[2] = (char)((num >> 16) & 0xFF);
	    ret_arr[3] = (char)((num >> 24) & 0xFF);
	    return ret_arr;
	}
	
	/************************************/
	
	public static int[] ByteArrToIntArr(char[] b_arr) {
	    int k = b_arr.length >> 2;
	    int[] ret_arr = new int[k];
	    for(int i = 0; i < k; i++) {
	        ret_arr[i] = b_arr[i*4] | (b_arr[i*4+1] << 8) |
                     (b_arr[i*4+2] << 16) |
                     (b_arr[i*4+3] << 24);
	    }
	    return ret_arr;
	}
	
	/************************************/
	
	public static char[] IntArrToByteArr(int[] int_arr) {
	    char[] ret_arr = new char[int_arr.length * 4];
	    for(int i = 0; i < int_arr.length; i++) {
	        char[] temp = IntToByteArr(int_arr[i]);
	        ret_arr[i * 4] = temp[0];
	        ret_arr[i * 4 + 1] = temp[1];
	        ret_arr[i * 4 + 2] = temp[2];
	        ret_arr[i * 4 + 3] = temp[3];
	    }
	    return ret_arr;
	}
    public static char[] IntArrToByteArr(long[] int_arr) {
	    char[] ret_arr = new char[int_arr.length * 4];
	    for(int i = 0; i < int_arr.length; i++) {
	        char[] temp = IntToByteArr(int_arr[i]);
	        ret_arr[i * 4] = temp[0];
	        ret_arr[i * 4 + 1] = temp[1];
	        ret_arr[i * 4 + 2] = temp[2];
	        ret_arr[i * 4 + 3] = temp[3];
	    }
	    return ret_arr;
	}
	
	/************************************/
	
	public static char[] fill_zero(char[] arr, int from, int to) {
	    for(int i = from; i < to; i++) {
	        arr[i] = 0;
	    }
	    return arr;
	}
	public static int[] fill_zero(int[] arr, int from, int to) {
	    for(int i = from; i < to; i++) {
	        arr[i] = 0;
	    }
	    return arr;
	}
	public static long[] fill_zero(long[] arr, int from, int to) {
	    for(int i = from; i < to; i++) {
	        arr[i] = 0;
	    }
	    return arr;
	}
	
	/************************************/
	
	public static int[] StringToIntArr(String str) {
		return(StringToIntArr(str.toCharArray()));
	}
	public static int[] StringToIntArr(char[] str) {
		int[] ret = new int[str.length];
		for(int i = 0; i < str.length; i++) {
			ret[i] = str[i];
		}
		return ret;
	}
	
	/************************************/
	
	public static String ArrayToString(char[] arr) {
	    String str = "";
	    for(int i = 0; i < arr.length; i++) {
	        str += Character.toString((char)arr[i]);
	    }
	    return str;
	}
	public static String ArrayToString(int[] arr) {
	    String str = "";
	    for(int i = 0; i < arr.length; i++) {
	        str += Character.toString((char)(arr[i] & 0xFF));
	    }
	    return str;
	}
	public static String ArrayToString(long[] arr) {
	    String str = "";
	    for(int i = 0; i < arr.length; i++) {
	        str += Character.toString((char)(arr[i] & 0xFF));
	    }
	    return str;
	}
	
	/************************************/
	
	public static char ord(String str) {
	    return (char)str.charAt(0);
	}
	
	/************************************/
	
	public static int rol(int t, int bitsrot) {
        return (t >>> (32 - bitsrot)) | (t << bitsrot);
    }
	
	/************************************/

	/*****************************************/
	public static int[] calc_in(char[] l_arr) {
		int[] ret = { 
			(l_arr[1] >> 1),
            ((l_arr[1] >> 6) | (l_arr[0] << 2)), 
	        (l_arr[0] >> 3) 
		};
		return ret;
	}
	public static int[] calc_in(int[] l_arr) {
		int[] ret = { 
			(l_arr[1] >> 1),
            ((l_arr[1] >> 6) | (l_arr[0] << 2)), 
	        (l_arr[0] >> 3) 
		};
		return ret;
	}
	/*****************************************/
}
