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

            ////System.out.println(i + ": " + (char) i);
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

            ////System.out.println(i + ": " + (char) i);
            decompressionDictionary.put(i,c);
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
        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename);
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
    private static void compress (IO.Byte.reader input, IO.Bit.writer output) throws IOException {
        //System.out.println("----------------------------------------");
        output.write(MAGIC_BYTE);
        //System.out.println("Magic Byte: " + MAGIC_BYTE);

        createCompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        ArrayList<Byte> bytes = new ArrayList<>();
        //System.out.print("Bytes ");
        for(int j=0; j<bytes.size(); ++j){
            byte auxByte = bytes.get(j);
            //System.out.print((char) auxByte);
        }
        //System.out.println();
        int auxInt = input.read();
        //System.out.println("AuxInt: " + auxInt);

        while (auxInt != -1) {
            final byte b = (byte) auxInt;

            //System.out.println("b: " + (char) b);
            ArrayList<Byte> aux = new ArrayList<>(bytes);
            aux.add(b);
            //System.out.print("aux: ");
            for(int j=0; j<aux.size(); ++j){
                byte auxByte = aux.get(j);
                //System.out.print((char) auxByte);
            }
            //System.out.println();

            if (compressionDictionary.containsKey(aux)) {
                //System.out.println("--------------------");
                //System.out.println("Contains Aux");
                bytes = aux;
                //System.out.print("Bytes: ");
                for(int j=0; j<bytes.size(); ++j){
                    byte auxByte = bytes.get(j);
                    //System.out.print((char) auxByte);
                }
                //System.out.println();
                //System.out.println("-----");

            } else {
                //System.out.println("--------------------");

                //System.out.println("Doesn't contain Aux");
                //System.out.print("Bytes: ");
                for(int j=0; j<bytes.size(); ++j){
                    byte auxByte = bytes.get(j);
                    //System.out.print((char) auxByte);
                }
                //System.out.println();
                final int code = compressionDictionary.get(bytes);
                output.write(code);

                //System.out.println("code: " + code);
                //System.out.print("aux: ");
                for(int j=0; j<aux.size(); ++j){
                    byte auxByte = aux.get(j);
                    //System.out.print((char) auxByte);
                }
                //System.out.println();

                compressionDictionary.put(aux,i++);

                bytes = new ArrayList<>();
                bytes.add(b);

                //System.out.println("-----");
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
            for(int j=0; j<bytes.size(); ++j){
                byte auxByte = bytes.get(j);
                //System.out.print((char) auxByte);
            }
            //System.out.println();
            final int code = compressionDictionary.get(bytes);
            output.write(code);

            //System.out.println("code: " + code);
        }
        output.write(EOF);
        //System.out.println("EOF: " + EOF);
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
             IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
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
    private static void decompress (IO.Bit.reader input, IO.Byte.writer output) throws IOException {
        byte mb = (byte) input.readByte();
        //System.out.println(mb);
        createDecompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        try {
            int old_code = input.readInt();
            //System.out.println("old_code: "+ old_code);

            if (old_code != EOF) {
                ArrayList<Byte> aux = new ArrayList<>(decompressionDictionary.get(old_code));

                for (int j = 0; j < aux.size(); ++j) {
                    output.write(aux.get(j));
                    //System.out.println(aux.get(j));
                }

                byte ch = aux.get(0);

                int code = input.readInt();
                //System.out.println("code: "+ code);
                while (code != EOF) {
                    if (decompressionDictionary.containsKey(code)) {
                        aux = new ArrayList<>(decompressionDictionary.get(code));
                    } else {
                        aux = new ArrayList<>(decompressionDictionary.get(old_code));
                        aux.add(ch);
                    }

                    for (int j = 0; j < aux.size(); ++j) {
                        output.write(aux.get(j));
                        //System.out.println(aux.get(j));
                    }

                    ch = aux.get(0);

                    aux = new ArrayList<>(decompressionDictionary.get(old_code));
                    aux.add(ch);
                    decompressionDictionary.put(i++, aux);

                    old_code = code;
                    code = input.readInt();
                    //System.out.println("code: "+ code);

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
    }
}