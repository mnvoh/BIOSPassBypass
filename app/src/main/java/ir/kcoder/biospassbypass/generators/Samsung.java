package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;
/**
 * Master Password Generator for Samsung laptops (12 hexadecimal digits version)
 * e.g. 07088120410C0000
 * @author mnvoh
 */
public class Samsung {
    private int[] rotationMatrix1 = {
        7, 1, 5, 3, 0, 6, 2, 5, 2, 3, 0, 6, 1, 7, 6, 1, 5, 2, 7, 1, 0, 3, 7, 
        6, 1, 0, 5, 2, 1, 5, 7, 3, 2, 0, 6
    };
    private int[] rotationMatrix2 = {
        1, 6, 2, 5, 7, 3, 0, 7, 1, 6, 2, 5, 0, 3, 0, 6, 5, 1, 1, 7, 2, 5, 2, 
        3, 7, 6, 2, 1, 3, 7, 6, 5, 0, 1, 7
    };
    
    private int[] decryptHash( int[] hash, int key, int[] rotationMatrix ) {
        int[] outhash = new int[ hash.length ];
        for( int i = 0; i < hash.length; i++ ) {
            int part1 = ( hash[ i ] << ( rotationMatrix[ 7 * key + i] ) ) & 0xFF;
            int part2 = ( hash[ i ] >> ( 8 - rotationMatrix[ 7 * key + i ] ) );
            outhash[ i ] = part1 | part2;
        }
        return outhash;
    }
    
    /**
     * Master Password Generator for Samsung laptops (12 hexadecimal digits version)
     * e.g. 07088120410C0000
     * @param code The hash
     * @return an array list containing the found passwords
     */
    public ArrayList<String[]> generateBiosPassword( String code ) {
        ArrayList<String[]> ret = new ArrayList<String[]>();
        
        int[] hash = new int[ code.length() / 2 - 1 ];
        KeyboardDict keyboardDict = new KeyboardDict();
        for( int i = 1; i < hash.length; i++ ) {
            hash[ i - 1 ] = 
                    Integer.parseInt( code.substring( 2 * i, 2 * i + 2 ), 16 );
        }
        int key = Integer.parseInt( code.substring( 0, 2 ), 16 ) % 5;
        int[] decryptedHash = decryptHash( hash, key, this.rotationMatrix1 );
        String password = keyboardDict.keyboardEncToAscii( decryptedHash );
        String keycodePwd = "", asciiPwd = "";
        for( int i = 0; i < decryptedHash.length; i++ ) {
            keycodePwd += String.format( "%02x", decryptedHash[i] );
            asciiPwd += Character.toString( ( char )decryptedHash[ i ] );
        }
        if( password.length() <= 0 ) 
            password = keyboardDict.keyboardEncToAscii( decryptHash( hash, key, this.rotationMatrix2 ) );
        if( password.length() <= 0 )
            password = "FAILED";
        
        String[] r1 = { "SAMSUNG KEYCODE PASSWORD", keycodePwd };
        String[] r2 = { "SAMSUNG ASCII PASSWORD", asciiPwd };
        String[] r3 = { "SAMSUNG PASSWORD", password };
        ret.add( r1 );
        ret.add( r2 );
        ret.add( r3 );
        return ret;
    }
}
