package ir.kcoder.biospassbypass.generators;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * generates master passwords which can be used to unlock the BIOS
 * password of most Phoenix BIOS versions. It also works for some versions of
 * FSI, HP and Compaq laptops which use slightly different hashing algorithms
 * in the BIOS.
 * @author mnvoh
 */
public class FiveDecimals {
    private KeyboardDict keyboardDict;
    
    public FiveDecimals() {
        keyboardDict = new KeyboardDict();
    }
    
    private short badCRC16(short[] pwd, short salt) {
        short hash = salt;
        
        for(int i = 0; i < pwd.length; i++) {
            hash ^= ( short )pwd[i];
            for(int j = 0; j < 8; j++) {
                if( ( hash & 1 ) > 0 )
                    hash = ( short )( ( hash >> 1 ) ^ 0x2001 );
                else
                    hash = ( short )( hash >> 1 );
            }
        }
        
        return hash;
    }
    
    private String bruteForce(short hash, short salt, byte dictMode, short minLen, short maxLen) {
        short[] encodedPwd = {0,0,0,0,0,0,0};
        Random random = new Random();
        
        if( hash > 0x3FFF ) {
            return "Bad Hash";
        }

        int kk = 0;
        
        this.keyboardDict.setDictMode( dictMode );
        
        short[] keys = this.keyboardDict.getKeys();
        
        while( true ) {
//            int rndVal = random.nextInt( keys.length );
            
            for( int i = 0; i < 7; i++ ) {
                int value = random.nextInt( keys.length );
                encodedPwd[i] = keys[ value ];
//                rndVal *= 7;
            }
            
            for( int i = minLen; i <= maxLen; i++ ) {
                short crc = badCRC16( slice( encodedPwd, 0, i ), salt );
                if( crc == hash ) {
                    encodedPwd = slice( encodedPwd, 0, i );
                    return this.keyboardDict.keyboardEncToAscii( encodedPwd );
                }
            }
                        
            if(++kk > 50000) {
                return "Failed";
            }
        }
    }
    
    private short[] slice( short[] array, int start, int end ) {
        start = ( start < 0 ) ? 0 : start;
        end = ( end > array.length ) ? array.length : end;
        
        short[] retval = new short[ end - start ];
        
        for( int i = start; i < end; i++ ) {
            retval[ i - start ] = array[i];
        }
        
        return retval;
    }
    
    /**
     * Generates master password based on hashes with five decimal numbers
     * @param hash  The BIOS hash
     * @return An array list containing the calculated passwords
     */
    public ArrayList<String[]> generateBiosPassword( String hash ) {
        Pattern pattern = Pattern.compile("^[0-9]{5}$");
        Matcher matcher = pattern.matcher(hash);
        ArrayList<String[]> result = new ArrayList<String[]>();
        
        if( !matcher.find() ) {
            return result;
        }

        short iHash = Short.parseShort( hash );
        
        String[] s1 = { "Generic Phoenix BIOS", 
            bruteForce( iHash, ( short )0, KeyboardDict.DICT_CHARS, ( short )3, ( short )8 )
        };
        String[] s2 = { "HP/Compaq Phoenix BIOS", 
            bruteForce( iHash, ( short )17232, KeyboardDict.DICT_CHARS, ( short )3, ( short )8 )
        };
        String[] s3 = { "FSI Phoenix BIOS (generic)", 
            bruteForce( iHash, ( short )65, KeyboardDict.DICT_DIGITS, ( short )3, ( short )7 )
        };
        String[] s4 = { "FSI Phoenix BIOS ('L' model)", 
            bruteForce( ( short )( iHash + 1 ), ( short )'L', KeyboardDict.DICT_DIGITS, ( short )3, ( short )7 )
        };
        String[] s5 = { "FSI Phoenix BIOS ('P' model)", 
            bruteForce( ( short )( iHash + 1 ), ( short )'P', KeyboardDict.DICT_DIGITS, ( short )3, ( short )7 )
        };
        String[] s6 = { "FSI Phoenix BIOS ('S' model)", 
            bruteForce( ( short )( iHash + 1 ), ( short )'S', KeyboardDict.DICT_DIGITS, ( short )3, ( short )7 )
        };
        String[] s7 = { "FSI Phoenix BIOS ('X' model)", 
            bruteForce( ( short )( iHash + 1 ), ( short )'X', KeyboardDict.DICT_DIGITS, ( short )3, ( short )7 )
        };
        
        result.add(s1);
        result.add(s2);
        result.add(s3);
        result.add(s4);
        result.add(s5);
        result.add(s6);
        result.add(s7);
        
        return result;
    }
}
