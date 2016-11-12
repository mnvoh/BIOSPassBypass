package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Generates 5 kind of password based on the following hashes:<br>
 * <b>OLD HDD SERIAL</b>: a 12 symbol serial associated with old dell HDDs<br>
 * <b>TAG-BIOS TYPE</b>: a 7 symbol tag + dash + 4 symbol BIOS type identifier
 * (one of 595B, D35B, 2A7B and A95B)<br>
 * <b>NEW HDD SERIAL-BIOS TYPE</b>: a 11 symbol tag + dash + 4 symbol BIOS type identifier
 * (one of 595B, D35B, 2A7B and A95B)<br>
 * <b>TAG</b>: a 7 symbol tag<br>
 * <b>NEW HDD SERIAL</b>: a 11 symbol tag<br>
 * @author mnvoh
 */
public class Dell {
	private final String DELL_TAG = "DELL BY TAG";
	private final String DELL_HDD_NEW = "DELL BY HDD SERIAL NEW";
	private final String DELL_HDD_OLD = "DELL BY HDD SERIAL OLD";
	
	
	@SuppressWarnings("unused")
	private final short T_595B = 0;
	private final short T_D35B = 1;
	private final short T_2A7B = 2;
	private final short T_A95B = 3;
	
    private final short ENCF1 = 0;
    private final short ENCF1N = 1;
    private final short ENCF2 = 2;
    private final short ENCF2N = 3;
    private final short ENCF3 = 4;
    private final short ENCF4 = 5;
    private final short ENCF4N = 6;
    private final short ENCF5 = 7;
    private final short ENCF5N = 8;
    
	private final String[] DELL_SERIES_PREFIX = {"595B", "D35B", "2A7B", "A95B", ""};

	private final char[] encscans = {
		0x05, 0x10, 0x13, 0x09, 0x32, 0x03, 0x25, 0x11, 0x1F, 0x17, 0x06, 
        0x15, 0x30, 0x19, 0x26, 0x22, 0x0A, 0x02, 0x2C, 0x2F, 0x16, 0x14, 0x07, 
        0x18, 0x24, 0x23, 0x31, 0x20, 0x1E, 0x08, 0x2D, 0x21, 0x04, 0x0B, 0x12, 
        0x2E
    };

	private final String chartabl2A7B = "012345679abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0";

	private final String scancods = "\00\0331234567890-=\010\011qwertyuiop[]\015\377asdfghjkl;'`\377\\zxcvbnm,./";

	private final int[] MD5magic = {
	    0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
	    0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
	    0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
	    0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
	    0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
	    0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
	    0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
	    0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
	    0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
	    0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
	    0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x4881d05,
	    0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
	    0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
	    0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
	    0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
	    0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
	};
	
	int[] encData = {
            0x67452301,
            0xEFCDAB89,
            0x98BADCFE,
            0x10325476
	};
	
	public Dell() {
	}
	
	/*****************************************/
	private short dell_get_serial_line(String serial) {
	    String type = serial.substring(serial.length() - 4, serial.length()).toUpperCase(Locale.ENGLISH);
	    
	    for(short i = 0; i < DELL_SERIES_PREFIX.length; i++) {
	    	if(type.equals(DELL_SERIES_PREFIX[i]))
	    		return i;
	    }
	    
	    return ( short )( DELL_SERIES_PREFIX.length - 1 );
	}
	
//	private short dell_get_serial_line(char[] serial) {
//		String sSerial = String.valueOf(serial);
//		return(dell_get_serial_line(sSerial));
//	}
	/*****************************************/
	

	/*****************************************/
	/* Depends only on the first two chars */
	private int[] calc_suffix_hdd_old(String serial) {
	    // encscans[26], enscans[0xAA % enscans.length]
	    int[] ret_arr = {49, 49, 49, 49, 49, 0, 0, 0}; 
	    char[] serial_arr = serial.toCharArray();
	    int calc_in[] = DellUtilities.calc_in(serial_arr);
	    ret_arr[5] = calc_in[0];
	    ret_arr[6] = calc_in[1];
	    ret_arr[7] = calc_in[2];
	    // lower bits than 5 are never change
	    for(int i=5; i < 8; i++) {
	        int r = 0xAA;
	        if((ret_arr[i] & 8) > 0) r ^= serial_arr[1];
	        if((ret_arr[i] & 16) > 0) r ^= serial_arr[0];
	        ret_arr[i] = encscans[ r % encscans.length ];
	    }
	    return ret_arr;
	}
	/*****************************************/
	
	/*****************************************/
	private char[] begin_calc(String serial, char[] s_arr){
	    int[] ret_arr = new int[8];
        char[] bSerial = serial.toCharArray();
	    
	    ret_arr[0] = bSerial[ s_arr[3] ];
	    ret_arr[1] = (bSerial[ s_arr[3] ] >> 5) | (((bSerial[ s_arr[2] ] >> 5) | (bSerial[ s_arr[2] ] << 3)) & 0xF1);
	    ret_arr[2] = (bSerial[ s_arr[2] ] >> 2);
	    ret_arr[3] = (bSerial[ s_arr[2] ] >> 7) | (bSerial[ s_arr[1] ] << 1);
	    ret_arr[4] = (bSerial[ s_arr[1] ] >> 4) | (bSerial[ s_arr[0] ] << 4);

	    int[] calcin = DellUtilities.calc_in(bSerial);
	    ret_arr[5] = calcin[0];
	    ret_arr[6] = calcin[1];
	    ret_arr[7] = calcin[2];

	    return DellUtilities.toByte(ret_arr);
	}
	/*****************************************/
	
	
	/*****************************************/
	private char[] end_calc(String serial, char[] calced_arr, char[] s_arr, char[] table) {
	    char[] ret_arr = new char[8];
	    char[] bSerial = serial.toCharArray();
	    
	    for (int i = 0; i < 8; i++) {
	        int r = 0xAA;
	        if((calced_arr[i] & 1) > 0) r ^= bSerial[ s_arr[0] ];
	        if((calced_arr[i] & 2) > 0) r ^= bSerial[ s_arr[1] ];
	        if((calced_arr[i] & 4) > 0) r ^= bSerial[ s_arr[2] ];
	        if((calced_arr[i] & 8) > 0) r ^= bSerial[ 1 ];
	        if((calced_arr[i] & 16) > 0) r ^= bSerial[ 0 ];
	        
	        ret_arr[i] = table[r % table.length];
	    }
	    return ret_arr;
	}
	/*****************************************/
	
	/*****************************************/
	private char[] calc_suffix_shortcut(String serial, char[] s_arr1, char[] s_arr2){
	    char[] ret_arr = begin_calc(serial, s_arr1);
	    if(dell_get_serial_line(serial) == T_2A7B){
	        return end_calc(serial, ret_arr, s_arr2, chartabl2A7B.toCharArray()); 
	    } 
	    return end_calc(serial, ret_arr, s_arr2, encscans); 
	}
	/*****************************************/
	
	/*****************************************/
	private char[] calc_suffix_hdd_new(String serial) {
		char[] s_arr1 = {1,10,9,8};
		char[] s_arr2 = {8,9,10};
	    return calc_suffix_shortcut(serial, s_arr1, s_arr2);
	}
	/*****************************************/
	
	/*****************************************/
	private String dell_get_serial_main(String serial){
	    return serial.substring(0,serial.length() - 4);
	}
	/*****************************************/
	
	/*****************************************/
	private char[] calc_suffix_tag(String serial) {
		char[] s_arr1 = {1,2,3,4};
		char[] s_arr2 = {4,3,2};
	    return calc_suffix_shortcut(serial, s_arr1, s_arr2);
	}
	/*****************************************/
	
	/*****************************************/
	
	/*****************************************/
	private String answerToString(char[] b_arr, String serial) {
	    int r = b_arr[0] % 9;
	    String ret_str = "";
	    for(int i = 0; i < 16; i++) {
	        if(dell_get_serial_line(serial) == T_2A7B) {
	            ret_str += chartabl2A7B.charAt( b_arr[i] % chartabl2A7B.length());
	        } else if( ( r <= i) && (ret_str.length() < 8) ) {
	            ret_str += scancods.charAt(encscans[b_arr[i] % encscans.length]);
	        }
	    }
	    return ret_str;       
	}
	/*****************************************/
	
	/*****************************************/
	private char[] dell_encode(char[] in_str, int cnt, String serial) {
		char[] cinstr = new char[Math.max(in_str.length, cnt + 1)];
        System.arraycopy(in_str, 0, cinstr, 0, in_str.length);
		cinstr[cnt] = 0x80;
	    int[] encBlock = new int[16];
        int[] temp = DellUtilities.ByteArrToIntArr(cinstr);
        System.arraycopy(temp, 0, encBlock, 0, temp.length);
	    encBlock = DellUtilities.fill_zero(encBlock,6,16);
	    encBlock[14] = (cnt << 3);
        char[] ret = DellUtilities.IntArrToByteArr(chooseEncode(encBlock, serial));
	    return ret;
	}
	/*****************************************/
	private String dell_serial_normalize(String serial) {
        return dell_get_serial_main( serial ) + 
                DELL_SERIES_PREFIX[ dell_get_serial_line( serial ) ];
	}
    /*****************************************/
    private int encF2(int num1, int num2, int num3) {
        return (((num3 ^ num2) & num1) ^ num3); 
	}
    private int encF2N(int num1, int num2, int num3) {
        return (((~num3 ^ num2) & num1) ^ ~num3);
	}
    private int encF3(int num1, int num2, int num3) {
        return (((num1 ^ num2) & num3) ^ num2); 
	}
    private int encF4(int num1, int num2, int num3) {
        return ((num2 ^ num1) ^ num3);
	}
    private int encF4N(int num1, int num2, int num3) {
        return ((~num2 ^ num1) ^ num3); 
	}
    private int encF5(int num1, int num2, int num3) {
        return ((num1 | ~num3) ^ num2);
	}
    private int encF5N(int num1, int num2, int num3) {
        return ((~num1 | ~num3) ^ num2); 
	}
    private int chooseEncF(short enc, int num1, int num2, int num3) {
        int result = 0;
        switch(enc) {
            case ENCF2:
                result = encF2(num1, num2, num3);
                break;
            case ENCF2N:
                result = encF2N(num1, num2, num3);
                break;
            case ENCF3:
                result = encF3(num1, num2, num3);
                break;
            case ENCF4:
                result = encF4(num1, num2, num3);
                break;
            case ENCF4N:
                result = encF4N(num1, num2, num3);
                break;
            case ENCF5:
                result = encF5(num1, num2, num3);
                break;
            case ENCF5N:
                result = encF5N(num1, num2, num3);
                break;
        }
        return result;
    }
    private int encF1(short enc, int num1, int num2, int num3, int key) {
        return chooseEncF(enc, num1, num2, num3) + key; 
	}
    public int encF1N(short enc, int num1, int num2, int num3, int key) {
		return chooseEncF(enc, num1, num2, num3) - key; 
	}
	/*****************************************/
	private int[] blockEncode(int[] encBlock, short f1, short f2, short f3, 
            short f4 , short f5) {
	    int A = encData[0];
	    int B = encData[1];
	    int C = encData[2];
	    int D = encData[3];
        int t = 0;

	    int[][] S = {{ 7, 12, 17, 22 },{ 5, 9, 14, 20 },{ 4, 11, 16, 23 },{ 6, 10, 15, 21 }};
        
	    for(int i = 0; i < 64; i++) {
            t = MD5magic[i];
	        switch(i >> 4) {
	            case 0:
                    if(f1 == ENCF1)
                        t = A + encF1(f2, B, C , D, t + encBlock[i & 15]);
                    else
                        t = A + encF1N(f2, B, C , D, t + encBlock[i & 15]);
                    break;
	            case 1:
                    if(f1 == ENCF1)
                        t = A + encF1(f3, B, C , D, t + encBlock[(i * 5 + 1) & 15]);
                    else
                        t = A + encF1N(f3, B, C , D, t + encBlock[(i * 5 + 1) & 15]);
                    break;
	            case 2:
                    if(f1 == ENCF1)
                        t = A + encF1(f4, B, C , D, t + encBlock[(i * 3 + 5) & 15]);
                    else
                        t = A + encF1N(f4, B, C , D, t + encBlock[(i * 3 + 5) & 15]);
                    break;
	            case 3:
                    if(f1 == ENCF1)
                        t = A + encF1(f5, B, C , D, t + encBlock[(i * 7) & 15]);
                    else
                        t = A + encF1N(f5, B, C , D, t + encBlock[(i * 7) & 15]);
                    break;
	        }
            
	        A = D;
            D = C;
	        C = B;
	        B = B + DellUtilities.rol(t, S[i>>4][i&3]);
	    }

	    int[] retval = {
    		A + encData[0],
            B + encData[1],
            C + encData[2],
            D + encData[3]
		};
	    return retval;
	}
	/*****************************************/
	private boolean dellChecker(String serial, int len_arr[], String[] series_arr) {
		for(int i = 0; i < len_arr.length; i++) {
		    if(len_arr[i] == serial.length()) {
		        if(series_arr == null) {
		            return true;
		        }
		        
		        for(int j = 0; j < series_arr.length; j++) {
		        	try {
			            if(series_arr[dell_get_serial_line(serial)].equals(series_arr[j])){
			                return true;
			            }
		        	} catch(IndexOutOfBoundsException ex) {
		        		return false;
		        	}
		        }
		    }
		}
	    return false;
	}
	/*****************************************/
	public int[] chooseEncode(int[] encBlock, String serial) {
	    /* Main part */
	    short type = dell_get_serial_line(serial);
	    if(type == T_D35B) {
	        return blockEncode(encBlock, ENCF1, ENCF2, ENCF3, ENCF4, ENCF5);
	    } else {
	        return blockEncode(encBlock, ENCF1N, ENCF2N, ENCF3, ENCF4N, ENCF5N);
	    }
	}
	/*****************************************/
	
	/* Depends only in first two chars
	 *  12 symbols                      */
	private String getBiosPwdForDellHddOld(String serial) {
	    int[] t_arr = calc_suffix_hdd_old(serial);
	    String ret_str = "";
	    for(int i = 0; i < t_arr.length; i++) {
	        ret_str += scancods.charAt( t_arr[i] );
	    }
	    return ret_str;
	}
	
	/* 7 symbols + 4 symbols ( 595B, D35B, 2A7B, A95B ) */
	private String getBiosPwdForDellTag(String serial) {
	    if(dell_get_serial_line(serial) == T_A95B) { // A95B
	        serial = dell_get_serial_main(serial) + "595B";
	    }
	    char[] serial_arr1 = serial.toCharArray();
	    char[] serial_arr2 = calc_suffix_tag(serial);
	    int newLength = serial_arr1.length + serial_arr2.length;
	    char[] serial_arr = new char[newLength];
	    for(int i = 0; i < newLength; i++) {
	    	serial_arr[i] = (i < serial_arr1.length) ? serial_arr1[i] : 
                    serial_arr2[i - serial_arr1.length];
	    }
        char[] dell_encoded = dell_encode(serial_arr, 23, serial);
        String answered = answerToString( dell_encoded, serial );
	    return answered;
	}
	
	/* 12 symbols + 4 symbols ( 595B, D35B, 2A7B, A95B ) */
	private String getBiosPwdForDellHddNew(String serial) {
	    if(dell_get_serial_line(serial) == T_A95B) { // A95B
	        String zero_chars = Character.toString((char)0) + Character.toString((char)0) + Character.toString((char)0);
	        serial = serial.substring(3, serial.length() - 4) + zero_chars + "595B";
	    }
        char[] suff = calc_suffix_hdd_new( serial );
        char[] suffSerial = new char[serial.length() + suff.length];
	    for(int i = 0; i < suffSerial.length; i++) {
            suffSerial[i] = (i < serial.length()) ? serial.charAt(i) :
                    suff[i - serial.length()];
        }

	    return answerToString( dell_encode( suffSerial, 23, serial ), serial );
	}
	
	private ArrayList<String[]> metaDellManyTag(String serial, int len, String key, SeriesFunction func) {
	    if(serial.length() == len) {
	    	ArrayList<String[]> answ_arr = new ArrayList<String[]>();  
	        for( int i = 0; i < DELL_SERIES_PREFIX.length; i++ ) {
	        	String[] item = {key, func.exec(serial + DELL_SERIES_PREFIX[i])};
	            answ_arr.add(item);
	        }
	        return answ_arr;
	    } else {
	    	ArrayList<String[]> empty = new ArrayList<String[]>();
	        return empty;
	    }
	}
	
	/* Just shortcut */
	private ArrayList<String[]> dellCheckAndRunWithKey(String serial, SeriesFunction run_func, String key, int[] len_arr, String[] series) {
	    if( dellChecker( serial, len_arr, series ) ) {
	    	ArrayList<String[]> r_ob = new ArrayList<String[]>();
            String[] item = {key, run_func.exec( dell_serial_normalize( serial ) )};
	        r_ob.add( item );
	        return r_ob;
	    } else {
            ArrayList<String[]> empty = new ArrayList<String[]>();
	        return empty;
	    }
	}
	
	/******************************************/
	/******************************************/
	/******************************************/
	/******************************************/
	
	class SeriesFunction {
		public String exec(String serial) {
			return "";
		}
	}
	class SeriesFunction_getBiosPwdForDellHddOld extends SeriesFunction {
		@Override
		public String exec(String serial) {
			return getBiosPwdForDellHddOld(serial);
		}
	}
	class SeriesFunction_getBiosPwdForDellHddNew extends SeriesFunction {
		@Override
		public String exec(String serial) {
			return getBiosPwdForDellHddNew(serial);
		}
	}
	class SeriesFunction_getBiosPwdForDellTag extends SeriesFunction {
		@Override
		public String exec(String serial) {
			return getBiosPwdForDellTag(serial);
		}
	}
	
	/******************************************/
	/******************************************/
	/******************************************/
	/******************************************/
	
    /**
     * Generates BIOS password for DELL laptops based on xxxxxxxxxxxx in which 
     * @param serial the xxxxxxxxxxxx hash
     * @return an array list containing the found passwords
     */
	public ArrayList<String[]> generateBiosPasswordHddOld(String serial) {
		int[] len_arr = {11};
	    return dellCheckAndRunWithKey(serial, new SeriesFunction_getBiosPwdForDellHddOld(), 
	                                DELL_HDD_OLD, len_arr, null);
	}

    /**
     * Generates BIOS password for DELL laptops based on xxxxxxx-yyyy in which 
     * y belongs to { 595B, D35B, 2A7B, A95B }
     * @param serial the xxxxxxx-yyyy hash
     * @return an array list containing the found passwords
     */
	public ArrayList<String[]> generateBiosPassword74(String serial) {
		int[] len_arr = {11};
	    return dellCheckAndRunWithKey(serial.replace("-", ""), new SeriesFunction_getBiosPwdForDellTag(), 
	                                DELL_TAG, len_arr, DELL_SERIES_PREFIX);
	}

	/**
     * Generates BIOS password for DELL laptops based on xxxxxxxxxxx-yyyy in which 
     * y belongs to { 595B, D35B, 2A7B, A95B }
     * @param serial the xxxxxxxxxxx-yyyy hash
     * @return an array list containing the found passwords
     */
	public ArrayList<String[]> generateBiosPassword114(String serial) {
		int[] len_arr = {15};
	    return dellCheckAndRunWithKey(serial.replace("-", ""), new SeriesFunction_getBiosPwdForDellHddNew(), 
	                                DELL_HDD_NEW, len_arr, DELL_SERIES_PREFIX);
	}

	/**
     * Generates BIOS password for DELL laptops based on xxxxxxx in which 
     * @param serial the xxxxxxx hash
     * @return an array list containing the found passwords
     */
	public ArrayList<String[]> generateBiosPassword7(String serial) {
	    return metaDellManyTag( serial, 7, DELL_TAG, new SeriesFunction_getBiosPwdForDellTag() );
	}

	/**
     * Generates BIOS password for DELL laptops based on xxxxxxxxxxx in which 
     * @param serial the xxxxxxxxxxx hash
     * @return an array list containing the found passwords
     */
	public ArrayList<String[]> generateBiosPassword11(String serial) {
	    return metaDellManyTag( serial, 11, DELL_HDD_NEW, new SeriesFunction_getBiosPwdForDellHddNew() );
	}
}
