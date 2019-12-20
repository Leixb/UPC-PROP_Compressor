/**
 * @file ./src/main/java/domini/LZW.java
 * @author ***REMOVED***
 */
package domini;

import persistencia.IO;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief Compresión y descompresión de archivos con LZW
 */
public final class LZW implements CompressionAlg{

    /** Creadora vacia LZW */
    public LZW() { }

    /// Magic Byte LZW
    public final static byte MAGIC_BYTE = (byte) 0x11;

    /// Tamaño inicial del diccionario
    private final static int DICTIONARY_SIZE = 0x0FF;

    /// Marca el overflow del diccionario
    private final static int OVERFLOW = 0x7FFFFFFD;

    /// Pseudo EOF
    private final static int EOF = 0x7FFFFFFE;

    /// Diccionario de compresión
    private static Map<ArrayList<Byte>, Integer> compressionDictionary;

    /// Diccionario de descompresión
    private static Map<Integer, ArrayList<Byte> > decompressionDictionary;

    /**
     * @brief Crea el diccionario de compresión y lo inicializa con los valores unitarios
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
     * @brief Crea el diccionario de descompresión y lo inicializa con los valores unitarios
     */
    private static void createDecompressionDictionary() {
        decompressionDictionary = new HashMap<>();
        for (int i = 0; i <= DICTIONARY_SIZE; ++i){
            ArrayList<Byte> c = new ArrayList<>();
            c.add((byte) i);
            decompressionDictionary.put(i,c);
        }
    }

    public byte getMagicByte() {
        return MAGIC_BYTE;
    }

    /**
     * @brief  Calcula el numero de bits necesarios para codificar en base 2 el int pasado por parametro
     *
     * @param n  Numero integer del que se calculan cuantos bits son necesarios para codificarlo en base 2
     * @return Devuelve el numero de bits necesarios para codificar en base 2 el int pasado por parametro
     */
    private static int bitsNeeded(final int n) {
        if (n <= 0)
            return 1;
        return (int) (Math.log(n) / Math.log(2) + 1e-10) + 1;
    }

    /**
     * @brief  Escribe en output el entero code representado en n bits precedido de 5 bits que codifican n
     *
     * @param code Numero integer que se escribe
     * @param output Salida de tipo IO.Bit.writer para escribir en el archivo comprimido
     * @throws IOException Lanza cualquier excepción generada al escribir
     */
    private static void writeCode (int code, final IO.Bit.writer output) throws IOException{
        int nbits = bitsNeeded(code);
        final BitSetL n = new BitSetL(nbits-1,5);
        output.write(n);
        final BitSetL bsNum = new BitSetL(code, nbits);
        output.write(bsNum);    }

    /**
     * @brief Comprime un archivo implementando un algoritmo LZW
     *
     * @param input objeto de lectura del archivo que se comprime
     * @param output objeto de ecritura del archivo comprimido
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    public void compress (IO.Byte.reader input, IO.Bit.writer output) throws IOException {
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
                writeCode(code,output);

                compressionDictionary.put(aux,i++);

                bytes = new ArrayList<>();
                bytes.add(b);
            }

            if (i >= OVERFLOW) {  //[DICTIONARY OVERFLOW]
                writeCode(OVERFLOW,output);
                createCompressionDictionary();
                i = DICTIONARY_SIZE;
            }
            auxInt = input.read();
        }
        if (compressionDictionary.containsKey(bytes)) {
           final int code = compressionDictionary.get(bytes);
            writeCode(code,output);
        }
        writeCode(EOF,output);
    }

    /**
     * @brief Descomprime un archivo implementando un algoritmo LZW
     *
     * @param input objeto de lectura del archivo que se descomprime
     * @param output objeto de ecritura del archivo descomprimido
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    public void decompress (IO.Bit.reader input, IO.Byte.writer output) throws IOException {
        createDecompressionDictionary();
        int i = DICTIONARY_SIZE + 1;

        try {
            int n = input.readBitSet(5).asInt();
            int oldCode = input.readBitSet(n+1).asInt();
            if (oldCode != EOF) {
                ArrayList<Byte> aux = new ArrayList<>(decompressionDictionary.get(oldCode));

                for (byte b : aux) {
                    output.write(b);
                }

                byte ch = aux.get(0);
                n = input.readBitSet(5).asInt();
                int code = input.readBitSet(n+1).asInt();
                while (code != EOF) {
                    if (decompressionDictionary.containsKey(code)) {
                        aux = new ArrayList<>(decompressionDictionary.get(code));
                    } else {
                        aux = new ArrayList<>(decompressionDictionary.get(oldCode));
                        aux.add(ch);
                    }

                    for (byte b : aux) {
                        output.write(b);
                    }

                    ch = aux.get(0);
                    aux = new ArrayList<>(decompressionDictionary.get(oldCode));
                    aux.add(ch);
                    decompressionDictionary.put(i++, aux);

                    oldCode = code;
                    n = input.readBitSet(5).asInt();
                    code = input.readBitSet(n+1).asInt();

                    if (code == OVERFLOW) {   //[DICTIONARY OVERFLOW DETECTED]
                        createDecompressionDictionary();
                        i = DICTIONARY_SIZE;
                        n = input.readBitSet(5).asInt();
                        code = input.readBitSet(n+1).asInt();
                    }
                }
            }
        } catch (EOFException e){
            //End of file reached.
        }
    }
}
