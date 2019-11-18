package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author ***REMOVED***
 *
 * @brief Compresión y descompresión de archivos de texto con LZW.
 */

public final class LZW {

    private LZW() { }

    /// Magic Byte LZW
    public final static byte MAGIC_BYTE = (byte) 0x11;

    /// Tamaño inicial del diccionario
    private final static int DICTIONARY_SIZE = 0xFFFF;

    /// Diccionario de compresión
    private static HashMap<String, Integer> compressionDictionary;

    /// Diccionario de descompresión
    private static HashMap<Integer, String> decompressionDictionary;

    /**
     * @brief Crea el diccionario de compresión y lo inicializa.
     */
    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            char c = (char) i;
            compressionDictionary.put(Character.toString(c) ,i);
        }
    }

    /**
     * @brief Crea el diccionario de descompresión y lo inicializa.
     */
    private static void createDecompressionDictionary() {
        decompressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            char c = (char) i;
            decompressionDictionary.put(i,Character.toString(c));
        }
    }

    /**
     * @brief LLama a una función que comprime un archivo de texto.
     *
     * @param inputFilename path y nombre del archivo que se quiere comprimir.
     * @param outputFilename path y nombre del archivo comprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    public static void compress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Char.reader input = new IO.Char.reader(inputFilename);
             IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            compress(input, output);
        }
    }


    /**
     * @brief Comprime un archivo de texto implementando un algoritmo LZW.
     *
     * @param input objeto de lectura del archivo que se quiere comprimir.
     * @param output objeto de ecritura del archivo comprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    private static void compress (IO.Char.reader input, IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        createCompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        String chars = "";
        int c = input.read();
        while (c != -1) {
            final char ch = (char) c;
            final String aux = chars + ch;

            if (compressionDictionary.containsKey(aux)) {
                chars = aux;
            } else {
                final int code = compressionDictionary.get(chars);
                output.write(code);

                compressionDictionary.put(aux,i++);
                chars = "" + ch;
            }

            if (i >= 0x7FFFFFFF) {  //[DICTIONARY OVERFLOW]
                output.write(0x7FFFFFFF);
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }

            c = input.read();
        }
        if (compressionDictionary.containsKey(chars)) {
            final int code = compressionDictionary.get(chars);
            output.write(code);
        }
    }

    /**
     * @brief LLama a una función que descomprime un archivo de texto.
     *
     * @param inputFilename path y nombre del archivo que se quiere descomprimir.
     * @param outputFilename path y nombre del archivo descomprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    public static void decompress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
                IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            decompress(input, output);
        }
    }

    /**
     * @brief Descomprime un archivo de texto implementando un algoritmo LZW.
     *
     * @param input es el objeto de lectura del archivo que se quiere descomprimir.
     * @param output es el objeto de ecritura del archivo desccomprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    private static void decompress (IO.Bit.reader input, IO.Char.writer output) throws IOException {
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

                if (code == 0x7FFFFFFF) {   //[DICTIONARY OVERFLOW DETECTED]
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
