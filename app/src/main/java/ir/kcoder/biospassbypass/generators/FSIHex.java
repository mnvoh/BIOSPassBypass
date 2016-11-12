package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;

/**
 * Generates master passwords which can be used to unlock
 * the BIOS passwords of most Fujitsu Siemens laptops (Lifebook, Amilo etc.).
 * After entering the wrong password for the third time, you will receive a
 * hexadecimal code from which the master password can be calculated,
 * e.g. 0A1B2D3E or AAAA-BBBB-CCCC-DEAD-BEEF
 * @author mnvoh
 */
public class FSIHex {
    private int[] table;
    private void generateCRC16Table() {
        this.table = new int[256];
        for( int i = 0; i < 256; i++ ) {
            int crc = ( i << 8 );
            for( int j = 0; j < 8; j++ ) {
                if( ( crc & 0x8000 ) > 0 )
                    crc = ( crc << 1 ) ^ 0x1021;
                else
                    crc = ( crc << 1);
            }
            this.table[i] = crc & 0xFFFF;
        }
    }
    
    private int calculateHash( String word ) {
        int hash = 0;
        for( int i = 0; i < word.length(); i++ ) {
            int d = this.table[ ( word.charAt( i ) ^ ( hash >> 8 ) ) % 256 ];
            hash = ( ( hash << 8 ) ^ d ) & 0xFFFF;
        }
        return hash;
    }
    
    private String hashToString( int hash ) {
        return(
            Character.toString( ( char )('0' + ( ( hash >> 12 ) % 16 ) % 10 ) ) +
            Character.toString( ( char )('0' + ( ( hash >> 8 ) % 16 ) % 10 ) ) + 
            Character.toString( ( char )('0' + ( ( hash >> 4 ) % 16 ) % 10 ) ) + 
            Character.toString( ( char )('0' + ( ( hash ) % 16 ) % 10 ) )
        );
    }
    
    /**
     * Generates master passwords based on xxxxxxxx or 
     * xxxx-xxxx-xxxx-xxxx-xxxx hex hashes
     * @param code the hash
     * @return The found password in a string
     */
    public ArrayList<String[]> generateBiosPassword( String code ) {
        ArrayList<String[]> ret = new ArrayList<String[]>();
        
        code = code.replace("-", "");
        if( code.length() == 20 )
        	code = code.substring(12, 20);
        
        if( code.length() != 8 ) return ret;
        
        this.generateCRC16Table();
        
        String[] r = { "FSI 5X4 HEX", 
            hashToString( calculateHash( code.substring( 0, 4 ) ) ) + 
            hashToString( calculateHash( code.substring( 4, 8 ) ) ) };
        ret.add( r );
        return( ret );
    }
}
