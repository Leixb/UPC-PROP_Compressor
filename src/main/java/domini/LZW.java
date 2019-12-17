/**
 * @file ./src/main/java/domini/LZW.java
 * @author Alex Herrero
 */
package domini;

import persistencia.IO;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief Compresión y descompresión de archivos de texto con LZW.
 */
public final class LZW {

    private LZW() { }

    /// Magic Byte LZW
    public final static byte MAGIC_BYTE = (byte) 0x11;

    /// Tamaño inicial del diccionario
    private final static int DICTIONARY_SIZE = 0x7FFF;

    /// Overflow del diccionario
    private final static int OVERFLOW = 0x7FFFFFFE;

    /// Pseudo EOF
    private final static int EOF = 0x7FFFFFFF;

    /// Diccionario de compresión
    private static Map<ArrayList<Byte>, Integer> compressionDictionary;

    /// Diccionario de descompresión
    private static Map<Integer, ArrayList<Byte> > decompressionDictionary;

    /**
     * @brief Crea el diccionario de compresión y lo inicializa.
     */
    private static void createCompressionDictionary() {
        compressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            ArrayList<Byte> c = new ArrayList<>();
            c.add((byte) i);
            compressionDictionary.put(c ,i);
        }
    }

    /**
     * @brief Crea el diccionario de descompresión y lo inicializa.
     */
    private static void createDecompressionDictionary() {
        decompressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            ArrayList<Byte> c = new ArrayList<>();
            c.add((byte) i);
            decompressionDictionary.put(i,c);
        }
    }

    /**
     * @brief Comprime un archivo de texto implementando un algoritmo LZW.
     *
     * @param input objeto de lectura del archivo que se quiere comprimir.
     * @param output objeto de ecritura del archivo comprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    public static void compress (IO.Byte.reader input, IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        createCompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        ArrayList<Byte> bytes = new ArrayList<>();
        int auxInt = input.read();
        while (auxInt != -1) {
            final byte b = (byte) auxInt;
            ArrayList<Byte> aux = new ArrayList<>(bytes);
            aux.add(b);
            if (compressionDictionary.containsKey(aux)) {
                bytes = aux;
            } else {
                final int code = compressionDictionary.get(bytes);
                output.write(code);

                compressionDictionary.put(aux,i++);

                bytes = new ArrayList<>();
                bytes.add(b);
            }

            if (i >= OVERFLOW) {  //[DICTIONARY OVERFLOW]
                output.write(OVERFLOW);
                System.out.println("[DICTIONARY OVERFLOW]");
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }
            auxInt = input.read();
        }
        if (compressionDictionary.containsKey(bytes)) {
           final int code = compressionDictionary.get(bytes);
            output.write(code);
        }
        output.write(EOF);

        compressionDictionary = new HashMap<>();
    }

    /**
     * @brief Descomprime un archivo de texto implementando un algoritmo LZW.
     *
     * @param input es el objeto de lectura del archivo que se quiere descomprimir.
     * @param output es el objeto de ecritura del archivo desccomprimido.
     * @throws IOException se produce un error en la lectura / escritura.
     */
    public static void decompress (IO.Bit.reader input, IO.Byte.writer output) throws IOException {
        input.readByte();
        createDecompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        try {
            int old_code = input.readInt();
            if (old_code != EOF) {
                ArrayList<Byte> aux = new ArrayList<>(decompressionDictionary.get(old_code));

                for (byte b : aux) {
                    output.write(b);
                }

                byte ch = aux.get(0);
                int code = input.readInt();
                while (code != EOF) {
                    if (decompressionDictionary.containsKey(code)) {
                        aux = new ArrayList<>(decompressionDictionary.get(code));
                    } else {
                        aux = new ArrayList<>(decompressionDictionary.get(old_code));
                        aux.add(ch);
                    }

                    for (byte b : aux) {
                        output.write(b);
                    }

                    ch = aux.get(0);
                    aux = new ArrayList<>(decompressionDictionary.get(old_code));
                    aux.add(ch);
                    decompressionDictionary.put(i++, aux);

                    old_code = code;
                    code = input.readInt();

                    if (code == OVERFLOW) {   //[DICTIONARY OVERFLOW DETECTED]
                        System.out.println("[DICTIONARY OVERFLOW DETECTED]");
                        createDecompressionDictionary();
                        i = DICTIONARY_SIZE;
                        code = input.readInt();
                    }
                }
            }
        } catch (EOFException e){
            //End of file reached.
        }

        decompressionDictionary = new HashMap<>();
    }
}
