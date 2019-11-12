package Domini;

import java.io.IOException;
import java.util.HashMap;

public class LZW {

    public static class TooManyStringsException extends Exception {

        public TooManyStringsException() { super(); }

        public TooManyStringsException(String s) { super(s); }

    }

    static final byte DICTIONARY_SIZE = 0x7E-0x20;

    private HashMap<String,Byte> createCompressionDictionary () {
        HashMap<String, Byte> dictionary = new HashMap<>();
        for (byte i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) (i+0x20);
            dictionary.put(Character.toString(c),i);
        }
        return dictionary;
    }

    private HashMap<Byte, String> createDecompressionDictionary () {
        HashMap<Byte, String> dictionary = new HashMap<>();
        for (byte i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) (i+32);
            dictionary.put(i,Character.toString(c));
        }
        return dictionary;
    }


    public void compress_LZW (IO.reader input, IO.writer output) throws IOException, TooManyStringsException {

        //INICIALIZE THE DICTIONARY
        HashMap<String, Byte> dictionary = createCompressionDictionary();
        byte i = DICTIONARY_SIZE;

        String chars = "";

        byte ch = input.read();
        while (ch != -1) {
            String aux = chars + (char) ch;
            if (dictionary.containsKey(aux)) {
                chars = aux;
            }
            else {
                byte code = dictionary.get(chars);

                byte[] buf = new byte[1];
                buf[0] = (byte) code;

                output.write(buf);

                dictionary.put(aux,i);
                ++i;
                chars = "" + (char) ch;
            }
            ch = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }
        byte code = dictionary.get(chars);

        byte[] buf = new byte[1];
        buf[0] = code;

        output.write(buf);
    }


    public void decompress_LZW (IO.reader input, IO.writer output) throws IOException, TooManyStringsException{
        HashMap<Byte, String> dictionary = createDecompressionDictionary();
        byte i = DICTIONARY_SIZE;
        String chars = "";
        byte old_code = input.read();
        if (dictionary.containsKey(old_code)) {
            chars = dictionary.get(old_code);
            output.write(chars.getBytes());
        }

        byte code = input.read();

        while (code != -1) {
            if (dictionary.containsKey(code)) {
                chars = dictionary.get(code);
            }
            else {
                chars = dictionary.get(code);
                chars += chars.charAt(0);
            }
            output.write(chars.getBytes());

            String aux = dictionary.get(old_code) + chars.charAt(0);

            dictionary.put(i, aux);
            ++i;

            old_code = code;

            code = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }
    }
}
