package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Master Password Generator for HP/Compaq Mini Netbooks with BIOS hashes
 * similar to CNU1234ABC
 * @author mnvoh
 */
public class HPMini {
    private char[] table1Key = {
        '1', '0', '3', '2', '5', '4', '7', '6', '9', 
        '8', 'a', 'c', 'b', 'e', 'd', 'g', 'f', 'i', 'h', 'k', 'j', 'm', 'l', 
        'o', 'n', 'q', 'p', 's', 'r', 'u', 't', 'w', 'v', 'y', 'x', 'z'
    };
    private char[] table1Value = {
        '3', '1', 'F', '7', 'Q', 'V', 'X', 'G', 'O', 'U', 'C', 'E', 'P', 'M', 
        'T', 'H', '8', 'Y', 'Z', 'S', 'W', '4', 'K', 'J', '9', '5', '2', 'N',
        'B', 'L', 'A', 'D', '6', 'I', '4', '0'
    };
    private char[] table2Key = {
        '1', '0', '3', '2', '5', '4', '7', '6', '9', '8', 'a', 'c', 'b', 'e', 
        'd', 'g', 'f', 'i', 'h', 'k', 'j', 'm', 'l', 'o', 'n', 'q', 'p', 's', 
        'r', 'u', 't', 'w', 'v', 'y', 'x', 'z'
    };
    private char[] table2Value = {
        '3', '1', 'F', '7', 'Q', 'V', 'X', 'G', 'O', 'U', 'C', 'E', 'P', 'M', 
        'T', 'H', '8', 'Y', 'Z', 'S', 'W', '4', 'K', 'J', '9', '5', '2', 'N', 
        'B', 'L', 'A', 'D', '6', 'I', 'R', '0'
    };
    
    private char table1( char key ) {
        for( int i = 0; i < table1Key.length; i++ ) {
            if( this.table1Key[ i ] == key )
                return table1Value[ i ];
        }
        return 0;
    }
    private char table2( char key ) {
        for( int i = 0; i < table2Key.length; i++ ) {
            if( this.table2Key[ i ] == key )
                return table2Value[ i ];
        }
        return 0;
    }
    
    /**
     * generates master password for hashes like CNU1234ABC
     * @param code The BIOS hash
     * @return An array list containing the found password(s)
     */
    public ArrayList<String[]> generateBiosPassword( String code ) {
        ArrayList<String[]> results = new ArrayList<String[]>();
        
        if( code.length() != 10 ) return results;
        
        String password1 = "", password2 = "";
        code = code.toLowerCase(Locale.ENGLISH);
        for( int i = 0; i < code.length(); i++ ) {
            password1 += Character.toString( this.table1( code.charAt( i ) ) );
            password2 += Character.toString( this.table2( code.charAt( i ) ) );
        }
        if( password1.equals( password2 ) ) {
            String[] r1 = { "HP Master Password", password1.toLowerCase(Locale.ENGLISH) };
            results.add( r1 );
        } else {
            String[] r1 = { "HP Master Password1", password1.toLowerCase(Locale.ENGLISH) };
            String[] r2 = { "HP Master Password2", password2.toLowerCase(Locale.ENGLISH) };
            results.add( r1 );
            results.add( r2 );
        }
        return results;
    }
}
