package domini;

import java.io.IOException;
import java.util.HashMap;

import domini.IO;

public final class LZW extends LZ {

    private LZW() {}

    private static HashMap<String, Character> compressionDictionary;
    private static HashMap<Character, String> decompressionDictionary;

    public static class TooManyStringsException extends Exception {

        private static final long serialVersionUID = -3749513648532868661L;

        public TooManyStringsException() {
            super();
        }

        public TooManyStringsException(final String s) {
            super(s);
        }

    }

    private static final char DICTIONARY_SIZE = 0xFF;

    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (char i = 0; i < DICTIONARY_SIZE; ++i) {
            compressionDictionary.put(Character.toString(i), i);
        }
    }

    private static void createDecompressionDictionary() {
        decompressionDictionary = new HashMap<>();
        for (char i = 0; i < DICTIONARY_SIZE; ++i) {
            decompressionDictionary.put(i, Character.toString(i));
        }
    }

    public static void compress(final IO.Char.reader input, final IO.Char.writer output)
            throws IOException, TooManyStringsException {
        output.write(0x69);

        createCompressionDictionary();
        char i = DICTIONARY_SIZE;

        String chars = "";

        int c = input.read();
        while (c != -1) {
            final char ch = (char) c;
            final String aux = chars + ch;

            if (compressionDictionary.containsKey(aux)) {
                chars = aux;
            } else {
                final char code = compressionDictionary.get(chars);
                output.write(code);

                compressionDictionary.put(aux, i);
                ++i;
                chars = "" + ch;
            }

            if (i >= 0xFFFF) {
                System.out.println("[DICTIONARY OVERFLOW]");
                output.write(0xFFFF);
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }

            c = input.read();
        }

        if (!compressionDictionary.containsKey(chars)) {
            compressionDictionary.put(chars, i);
        }
        final char code = compressionDictionary.get(chars);
        output.write(code);
    }

    public static void decompress(final IO.Char.reader input, final IO.Char.writer output)
            throws IOException, TooManyStringsException {
        createDecompressionDictionary();
        char i = DICTIONARY_SIZE;

        char old_code = (char) input.read();

        String aux = decompressionDictionary.get(old_code);
        output.write(aux);

        char ch = aux.charAt(0);

        int c = input.read();
        while (c != -1) {
            final char code = (char) c;
            if (decompressionDictionary.containsKey(code)) {
                aux = decompressionDictionary.get(code);
            }
            else {
                aux = decompressionDictionary.get(old_code) + ch;
            }

            output.write(aux);

            ch = aux.charAt(0);

            decompressionDictionary.put(i, decompressionDictionary.get(old_code) + ch);
            i++;

            old_code = code;

            c = input.read();

            if (c == 0xFFFF) {
                System.out.println("[DICTIONARY OVERFLOW DETECTED]");
                createDecompressionDictionary();
                i = DICTIONARY_SIZE;
                c = input.read();
            }
        }
    }
}