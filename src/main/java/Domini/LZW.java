package Domini;

import java.io.IOException;
import java.util.HashMap;

import Domini.IO;

public class LZW implements FileCodec {

    private static HashMap<String, Byte> compressionDictionary;
    private static HashMap<Byte, String> decompressionDictionary;

    public static class TooManyStringsException extends Exception {

        public TooManyStringsException() { super(); }

        public TooManyStringsException(String s) { super(s); }

    }

    private static final byte DICTIONARY_SIZE = 0x7E;

    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (byte i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) i;
            compressionDictionary.put(Character.toString(c),i);
        }
    }

    private static void createDecompressionDictionary () {
        decompressionDictionary = new HashMap<>();
        for (byte i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) i;
            decompressionDictionary.put(i,Character.toString(c));
        }
    }


    public static void compress (IO.reader input, IO.writer output) throws IOException, TooManyStringsException {
        createCompressionDictionary();
        byte i = DICTIONARY_SIZE;

        String chars = "";

        int c = input.read();
        while (c != -1) {
            byte ch = (byte) c;
            String aux = chars + (char) ch;

            if (compressionDictionary.containsKey(aux)) {
                chars = aux;
            }
            else {
                if (!compressionDictionary.containsKey(chars)) {
                    compressionDictionary.put(chars,i);
                    ++i;
                }
                byte code = compressionDictionary.get(chars);

                byte[] buf = new byte[1];
                buf[0] = (byte) code;

                output.write(buf);

                compressionDictionary.put(aux,i);
                ++i;
                chars = "" + (char) ch;
            }

            c = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }

        if (!compressionDictionary.containsKey(chars)) {
            compressionDictionary.put(chars,i);
        }
        byte code = compressionDictionary.get(chars);

        byte[] buf = new byte[1];
        buf[0] = code;

        output.write(buf);
    }


    public static void decompress (IO.reader input, IO.writer output) throws IOException, TooManyStringsException{
        createDecompressionDictionary();
        byte i = DICTIONARY_SIZE;

        byte old_code = (byte) input.read();
        if (decompressionDictionary.containsKey(old_code)) {
            String aux = decompressionDictionary.get(old_code);
            output.write(aux.getBytes());
        }

        int c = input.read();

        while (c != -1) {
            byte code = (byte) c;
            if (decompressionDictionary.containsKey(code)) {
                String aux = decompressionDictionary.get(code);
                output.write(aux.getBytes());

                aux = decompressionDictionary.get(old_code) + aux.charAt(0);
                decompressionDictionary.put(i,aux);
                ++i;
                old_code = code;
            }
            else {
                String aux = decompressionDictionary.get(old_code);
                aux += aux.charAt(0);

                decompressionDictionary.put(i,aux);
                i++;

                output.write(aux.getBytes());
            }

            c = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }
    }
}
