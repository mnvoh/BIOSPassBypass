package ir.kcoder.biospassbypass.generators;

/**
 *
 * @author mnvoh
 */
public class KeyboardDict {
    private short[] keyboardDictKeys = { 
        2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
        30, 31, 32, 33, 34, 35, 36, 37, 38, 
        44, 45, 46, 47, 48, 49, 50 
    };
    private char[] keyboardDictValues = { 
        '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
        'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
        'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 
        'z', 'x', 'c', 'v', 'b', 'n', 'm' 
    };

    public static final byte DICT_CHARS = 0;
    public static final byte DICT_DIGITS = 1;
    public static final byte DICT_ALL = 2;
    private byte dictMode = DICT_ALL;
    
    private char valueFromKey(int k) {
        for(int i = 0; i < keyboardDictKeys.length; i++) {
            if(keyboardDictKeys[i] == k) return keyboardDictValues[i];
        }
        return 0;
    }
    private short keyFromValue(int v) {
        for(int i = 0; i < keyboardDictValues.length; i++) {
            if(keyboardDictValues[i] == v) return keyboardDictKeys[i];
        }
        return 0;
    }

    public String keyboardEncToAscii(short[] in) {
        String retval = "";

        for(int i = 0; i < in.length; i++) {
            if(in[i] > 0) {
                retval += valueFromKey( in[i] );
            } else {
                return retval;
            }
        }

        return retval;
    }
    public String keyboardEncToAscii(int[] in) {
        String retval = "";

        for(int i = 0; i < in.length; i++) {
            if(in[i] > 0) {
                retval += valueFromKey( in[i] );
            } else {
                return retval;
            }
        }

        return retval;
    }

    public short[] asciiToKeyboardenc(String in) {
        short[] retval = new short[in.length()];

        for(int i = 0; i < in.length(); i++) {
            retval[i] = keyFromValue(in.charAt(i));
        }

        return retval;
    }
    
    public void setDictMode(byte mode) {
        this.dictMode = ( byte )( mode % 3 );
    }
    public short[] getKeys() {
        int start = 0, length = this.keyboardDictKeys.length;
        short[] keys;
        if(this.dictMode == KeyboardDict.DICT_CHARS) {
            start = 10;
            length = this.keyboardDictKeys.length - 10;
        } else if( this.dictMode == KeyboardDict.DICT_DIGITS ) {
            start = 0;
            length = 10;
        }
        keys = new short[length];
        System.arraycopy(this.keyboardDictKeys, start, keys, 0, length);
        return( keys );
    }
    
    public int length() {
        if(this.dictMode == KeyboardDict.DICT_CHARS) {
            return( this.keyboardDictKeys.length - 10 );
        } else if( this.dictMode == KeyboardDict.DICT_DIGITS ) {
            return 10;
        }
        return( this.keyboardDictKeys.length );
    }
}