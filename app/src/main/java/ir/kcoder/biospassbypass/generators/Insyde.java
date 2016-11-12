package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;

/**
 * This script generates master passwords which can be used to unlock
 * the BIOS passwords of most Fujitsu Siemens laptops (Lifebook, Amilo etc.).
 * 
 * @author mnvoh
 */
public class Insyde {
    
    /**
     * Generates master password for Fujitsu Siemens laptops (Lifebook, Amilo etc.)
     * e.g. of hash: 03133610
     * @param strHash
     * @return 
     */
    public ArrayList<String[]> generateBiosPassword( String strHash ) {
        ArrayList<String[]> results = new ArrayList<String[]>();
        
        if( strHash.length() != 8 ) return results;
        
        String salt = "Iou|hj&Z";
        String pwd  = "";
        for( int i = 0; i < strHash.length(); i++ ) {
            long b = salt.charAt( i ) ^ strHash.charAt( i );
            long a = b;
            a = ( a * 0x66666667 ) >> 32;
            a = ( a >> 2 ) | ( a & 0xC0 );
            if( ( a & 0x80000000 ) > 1 )
                a++;
            a *= 10;
            pwd += Long.toString( b - a );
        }
        
        String[] r = { "INSYDE MASTER PASSWORD", pwd };
        
        results.add( r );
        return results;
    }
}
