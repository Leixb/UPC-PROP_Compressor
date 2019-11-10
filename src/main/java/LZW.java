import IO.reader;
import IO.writer;
import java.util.HashMap;

public class LZW {

    static final int DICTIONARY_SIZE = 95;

    private HashMap<String, Integer> createCompressionDictionary () {
        HashMap<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) (i+32);
            dictionary.put(Character.toString(c),i);
        }
        return dictionary;
    }

    private HashMap<Integer, String> createDecompressionDictionary () {
        HashMap<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < DICTIONARY_SIZE; ++i){
            char c = (char) (i+32);
            dictionary.put(i,Character.toString(c));
        }
        return dictionary;
    }


    public void compress_LZW (String filename) throws FileNotFoundException, IOException {

        //INICIALIZE THE DICTIONARY
        HashMap<String, Integer> dictionary = createCompressionDictionary();
        int i = DICTIONARY_SIZE;

        try (IO.reader input  = new IO.reader(filename);
             IO.writer output = new IO.writer("Compressed_" + filename)) {

            java.lang.String chars = "";

            int ch = input.read();
            while (ch != -1) {
                String aux = chars + (char) ch;
                if (dictionary.containsKey(aux)) {
                    chars = aux;
                }
                else {
                    int code = dictionary.get(chars);
                    output.write(code);
                    dictionary.put(aux,i);
                    ++i;
                    chars = "" + (char) ch;
                }
                ch = input.read();
            }
            int code = dictionary.get(chars);
            output.write(code);
        }
    }


    public void decompress_LZW (String filename) {
        HashMap<Integer, Stirng> dictionary = createDecompressionDictionary();
        int i = DICTIONARY_SIZE;

        try (IO.reader input  = new IO.reader(filename);
             IO.writer output = new IO.writer("Decompressed_" + filename)) {

            java.lang.String chars = "";
            int old_code = input.read();
            if (dictionary.containsKey(old_code)) {
                chars = dictionary.get(old_code);
                output.write(chars);
            }

            int code = input.read();

            while (code != -1) {
                if (dictionary.containsKey(code)) {
                    chars = dictionary.get(code);
                }
                else {
                    chars = dictionary.get(code);
                    chars += chars.charAt(0);
                }
                output.write(chars);

                String aux = dictionary.get(old_code) + chars.charAt(0);
                dictionary.put(aux);

                old_code = code;

                code = input.read();
            }
        }
    }
}
