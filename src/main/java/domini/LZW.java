package domini;

import java.io.IOException;
import java.io.EOFException;
import java.util.HashMap;

import domini.IO;

public class LZW implements FileCodec {

    private static final byte MAGIC_BYTE = 0x11;
    private static final int DICTIONARY_SIZE = 0xFFFF;
    private static final int END = 0;
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
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            char c = (char) i;
            compressionDictionary.put(Character.toString(c) ,i);
        }
    }

    private static void createDecompressionDictionary () {
        decompressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            char c = (char) i;
            decompressionDictionary.put(i,Character.toString(c));
        }
    }


    public static void compress (IO.Char.reader input, IO.Bit.writer output) throws IOException, TooManyStringsException {
        output.write(MAGIC_BYTE);

        createCompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        String chars = "";
        int c = input.read();
        while (c != -1) {
            char ch = (char) c;
            String aux = chars + ch;

            if (compressionDictionary.containsKey(aux)) {
                chars = aux;
            }
            else {
                int code = compressionDictionary.get(chars);
                output.write(code);

                compressionDictionary.put(aux,i++);
                chars = "" + ch;
            }

            if (i == 0x0FFFFFFFF) {  //[DICTIONARY OVERFLOW]
                output.write(0xFFFFFFFF);
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }

            c = input.read();
        }
        int code = compressionDictionary.get(chars);
        output.write(code);

        output.write(END);
    }


    public static void decompress (IO.Bit.reader input, IO.Char.writer output) throws IOException, TooManyStringsException{
        input.readByte();

        createDecompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        try {
            int old_code = input.readInt();
            String aux = decompressionDictionary.get(old_code);
            output.write(aux);

            char ch = aux.charAt(0);

            while (true) {
                int code = input.readInt();
                if (decompressionDictionary.containsKey(code)) {
                    aux = decompressionDictionary.get(code);
                } else {
                    aux = decompressionDictionary.get(old_code) + ch;
                }
                output.write(aux);

                ch = aux.charAt(0);

                decompressionDictionary.put(i++, decompressionDictionary.get(old_code) + ch);

                old_code = code;

                if (code == 0xFFFFFFFF) {   //[DICTIONARY OVERFLOW DETECTED]
                    createDecompressionDictionary();
                    i = DICTIONARY_SIZE;
                    code = input.readInt();
                }
            }
        } catch (EOFException e){
            //End of file reached.
        }

    }
}