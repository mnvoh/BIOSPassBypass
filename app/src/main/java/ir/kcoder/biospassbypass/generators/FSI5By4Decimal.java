package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;

/**
 * Generates BIOS passwords based on 5x4 Decimal digit hashes. The hash can 
 * either be old or new
 * @author mnvoh
 */
public class FSI5By4Decimal {
    private String XORkey = ":3-v@e4i";
    private String[] keys = {
        "4798156302", "7201593846", 
        "5412367098", "6587249310", 
        "9137605284", "3974018625", 
        "8052974163"
    };
    
    private int[] codeToBytes(String code) {
        int[] numbers = {
            Integer.parseInt(code.substring(0, 5)),
            Integer.parseInt(code.substring(5, 10)),
            Integer.parseInt(code.substring(10, 15)),
            Integer.parseInt(code.substring(15, 20))
        };
        int[] bytes = new int[8];
        for(int i = 0; i < numbers.length; i++) {
            bytes[ 2 * i ] = numbers[i] % 256;
            bytes[ 2 * i + 1 ] = numbers[i] / 256;
        }
        
        return bytes;
    }
    
    private char byteToChar(int b) {
        if( b > 9 ) {
            return ( char )( 'a' + b - 10 );
        }
        return ( char )( '0' + b );
    }
    
    private int[] interleave(int[] op_arr, int[] ar1, int[] ar2 ) {
        int[] arr = new int[op_arr.length];
        System.arraycopy(op_arr, 0, arr, 0, op_arr.length);
        
        arr[ar1[0]] = ((op_arr[ar2[0]] >> 4) | (op_arr[ar2[3]] << 4)) & 0xFF;
        arr[ar1[1]] = ((op_arr[ar2[0]] & 0x0F) | (op_arr[ar2[3]] & 0xF0));
        arr[ar1[2]] = ((op_arr[ar2[1]] >> 4) | (op_arr[ar2[2]] << 4) & 0xFF);
        arr[ar1[3]] = (op_arr[ar2[1]] & 0x0F) | (op_arr[ar2[2]] & 0xF0);
        
        return arr;
    }
    
    /**
     * Generate BIOS password based on old 5x4 Decimal hash
     * @param code the hash
     * @return an array list containing the found password 
     */
    public ArrayList<String[]> generateBiosPasswordOld( String code ) {
        code = code.replace("-", "");
        int[] bytes = codeToBytes( code );
        int temp;
        for( int i = 0; i < bytes.length; i++ ) {
            bytes[i] = ( bytes[i] ^ this.XORkey.charAt(i) ) & 0xFF;
        }
        
        //# swap two bytes
        temp = bytes[2];
        bytes[2] = bytes[6];
        bytes[6] = temp;
        
        temp = bytes[7];
        bytes[7] = bytes[3];
        bytes[3] = temp;
        
        //# interleave the nibbles
        int[] interleave1 = {0, 1, 2, 3};
        int[] interleave2 = {0, 1, 2, 3};
        int[] interleave3 = {4, 5, 6, 7};
        int[] interleave4 = {6, 7, 4, 5};
        bytes = interleave( bytes, interleave1, interleave2 );
        bytes = interleave( bytes, interleave3, interleave4 );

        //# final rotations
        bytes[0] = ((bytes[0] << 3) & 0xFF) | (bytes[0] >> 5);
    	bytes[1] = ((bytes[1] << 5) & 0xFF) | (bytes[1] >> 3);
    	bytes[2] = ((bytes[2] << 7) & 0xFF) | (bytes[2] >> 1);
    	bytes[3] = ((bytes[3] << 4) & 0xFF) | (bytes[3] >> 4);
    	bytes[5] = ((bytes[5] << 6) & 0xFF) | (bytes[5] >> 2);
    	bytes[6] = ((bytes[6] << 1) & 0xFF) | (bytes[6] >> 7);
    	bytes[7] = ((bytes[7] << 2) & 0xFF) | (bytes[7] >> 6);
     
        //# len(solution space) = 10+26
        for( int i = 0; i < bytes.length; i++ ) {
            bytes[i] %= 36;
        }
        
        String masterPassword = "";
        for(int i = 0; i < bytes.length; i++ ) {
            masterPassword += Character.toString( byteToChar( bytes[ i ] ) );
        }
        
        String[] rets = { "FSI 5X4 DECIMAL OLD", masterPassword };
        ArrayList<String[]> ret = new ArrayList<String[]>();
        ret.add( rets );
        return ret;
    }
    
    /**
     * Generate BIOS password based on new 5x4 Decimal hash
     * @param code the hash
     * @return an array list containing the found password 
     */
    public ArrayList<String[]> generateBiosPasswordNew( String code ) {
        ArrayList<String[]> ret = new ArrayList<String[]>();
        
        if( code.length() != 24 ) return ret;
        
        code = code.replace("-", "");
        String pwdHash = 
                Character.toString( code.charAt(0) ) + 
                Character.toString( code.charAt(2) ) +
                Character.toString( code.charAt(5) ) + 
                Character.toString( code.charAt(11) ) +
                Character.toString( code.charAt(13) ) + 
                Character.toString( code.charAt(15) ) + 
                Character.toString( code.charAt(16) );
        
        String masterPassword = "";
        for( int c = 0, i = 0; c < pwdHash.length(); c++, i++ ) {
            masterPassword += Character.toString(
                keys[i].charAt( pwdHash.charAt( c ) - '0' )
            );
        }
        
        String[] r = { "FSI 5X4 DECIMAL NEW", masterPassword };
        ret.add( r );
        return ret;
    }
}
