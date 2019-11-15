package domini;

import java.io.IOException;
import java.util.HashMap;

import domini.IO;

public class LZW implements FileCodec {

    private static final byte MAGIC_BYTE = 0x11;
    private static final int DICTIONARY_SIZE = 0xFF;
    private static HashMap<String, Integer> compressionDictionary;
    private static HashMap<Integer, String> decompressionDictionary;

    public static class TooManyStringsException extends Exception {

        private static final long serialVersionUID = -3749513648532868661L;


        public TooManyStringsException() {
            super();
        }

        public TooManyStringsException(String s) { super(s); }

    }

    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (int i = 0; i < DICTIONARY_SIZE; ++i){
            compressionDictionary.put(Integer.toString(i),i);
        }
    }

    private static void createDecompressionDictionary () {
        decompressionDictionary = new HashMap<>();
        for (int i = 0; i < DICTIONARY_SIZE; ++i){
            decompressionDictionary.put(i,Integer.toString(i));
        }
    }


    public static void compress (IO.Char.reader input, IO.Bit.writer output) throws IOException, TooManyStringsException {
        output.write(MAGIC_BYTE);

        createCompressionDictionary();
        int i = DICTIONARY_SIZE;

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
                }
                int code = compressionDictionary.get(chars);
                output.write(code);

                compressionDictionary.put(aux,i);
                ++i;
                chars = "" + ch;
            }

            if (i >= 0xFFFFFFFF) {
                System.out.println("[DICTIONARY OVERFLOW]");
                output.write(0xFFFFFFFF);
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }

            c = input.read();
        }

        if (!compressionDictionary.containsKey(chars)) {
            compressionDictionary.put(chars,i);
        }
        int code = compressionDictionary.get(chars);
        output.write(code);
    }


    public static void decompress (IO.Bit.reader input, IO.Char.writer output) throws IOException, TooManyStringsException{
        input.readByte();

        createDecompressionDictionary();
        int i = DICTIONARY_SIZE;

        int old_code = input.readInt();

        String aux = decompressionDictionary.get(old_code);
        output.write(aux);

        char ch = aux.charAt(0);

        int code = input.readInt();
        while (code != -1) {
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

            code = input.readInt();

            if (code == 0xFFFFFFFF) {
                System.out.println("[DICTIONARY OVERFLOW DETECTED]");
                createDecompressionDictionary();
                i = DICTIONARY_SIZE;
                code = input.readInt();
            }
        }
    }
}