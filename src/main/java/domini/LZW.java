package domini;

import java.io.IOException;
import java.util.HashMap;

import domini.IO;

public class LZW implements FileCodec {

    private static HashMap<String, Character> compressionDictionary;
    private static HashMap<Character, String> decompressionDictionary;

    public static class TooManyStringsException extends Exception {

        private static final long serialVersionUID = -3749513648532868661L;

        public TooManyStringsException() {
            super();
        }

        public TooManyStringsException(String s) { super(s); }

    }

    private static final char DICTIONARY_SIZE = 0xFF;

    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (char i = 0; i < DICTIONARY_SIZE; ++i){
            compressionDictionary.put(Character.toString(i),i);
        }
    }

    private static void createDecompressionDictionary () {
        decompressionDictionary = new HashMap<>();
        for (char i = 0; i < DICTIONARY_SIZE; ++i){
            decompressionDictionary.put(i,Character.toString(i));
        }
    }


    public static void compress (IO.Char.reader input, IO.Char.writer output) throws IOException, TooManyStringsException {
        createCompressionDictionary();

        char i = DICTIONARY_SIZE;

        String chars = "";

        int c = input.read();
        while (c != -1) {
            char ch = (char) c;
            String aux = chars + ch;

            if (compressionDictionary.containsKey(aux)) {
                chars = aux;
            }
            else {
                if (!compressionDictionary.containsKey(chars)) {
                    compressionDictionary.put(chars,i);
                    ++i;
                }
                char code = compressionDictionary.get(chars);
                output.write(code);

                compressionDictionary.put(aux,i);
                ++i;
                chars = "" + ch;
            }

            c = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }

        if (!compressionDictionary.containsKey(chars)) {
            compressionDictionary.put(chars,i);
        }
        char code = compressionDictionary.get(chars);
        output.write(code);
    }


    public static void decompress (IO.Char.reader input, IO.Char.writer output) throws IOException, TooManyStringsException{
        createDecompressionDictionary();
        char i = DICTIONARY_SIZE;

        char old_code = (char) input.read();
        if (decompressionDictionary.containsKey(old_code)) {
            String aux = decompressionDictionary.get(old_code);
            output.write(aux);
        }

        int c = input.read();
        while (c != -1) {
            char code = (char) c;
            if (decompressionDictionary.containsKey(code)) {
                String aux = decompressionDictionary.get(code);
                output.write(aux);

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

                output.write(aux);
            }

            c = input.read();

            if (i >= 255) {
                throw new TooManyStringsException("Dictionary not large enough. Too many unique strings.");
            }
        }
    }
}
