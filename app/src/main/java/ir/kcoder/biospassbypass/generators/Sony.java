package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;

/**
 *
 * @author mnvoh
 */
public class Sony {
    /**
     * Generate passwords for Sony laptops with 7 digits of hash(numerical)
     * Hash Example: 8376670 => 2506556
     * @param serial the BIOS hash generated after three failed password attempts
     * @return the found password in an ArrayList
     */
    public ArrayList<String[]> generateBiosPassword( String serial ) {
        ArrayList<String[]> result = new ArrayList<String[]>();
        if( serial.length() != 7 )
        {
            String[] r_e = { "SONY LAPTOP", "INVALID SERIAL" };
            result.add( r_e );
            return result;
        }
        String table = "0987654321876543210976543210982109876543109876543221098765436543210987";
        int pos = 0;
        String code = "";
        for( int i = 0; i < serial.length(); i++ ) {
        	try {
        		int index = Integer.parseInt( serial.substring( i, i + 1 ) ) + 10 * pos;
        		code += Character.toString( table.charAt( index ) );;
        		pos++;
        	} catch( Exception ex ) { return result; }
        }
        System.out.println( code );
        String[] r = { "SONY LAPTOP", code };
        result.add( r );
        return result;
    }
}
