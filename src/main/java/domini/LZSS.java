/**
 * @file ./src/main/java/domini/LZSS.java
 * @author ***REMOVED*** Plasencia
*/
package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @brief Compresión y decompresión de archivos de texto con LZSS
 */
public final class LZSS {

    private LZSS() {}

    final static ArrayList<Character> slidingWindow = new ArrayList<Character>();
    final static ArrayList<Character> actualCharacters = new ArrayList<Character>();
    final static int MAX_SIZE_SW = 8192; // maximum size of the sliding window
    final static int MAX_LENGTH_COINCIDENCE = 65; //
    public final static byte MAGIC_BYTE = 0x55; // magic byte for LZSS

    /**
     * @brief Crea el objeto lector y escritor para la compresión y llama al compresor
     * @param inputFilename nombre del archivo que se quiere comprimir
     * @param outputFilename nombre del archivo comprimido
     * @throws IOException se lanza cuando hay un error de lectura o escritura
     */

    public static void compress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Char.reader input = new IO.Char.reader(inputFilename);
                IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            compress(input, output);
        }
    }

    /**
     * @brief Usando el algoritmo LZSS esta función comprime un archivo txt
     * @param input objeto de lectura del archivo que se quiere comprimir
     * @param output objeto de escritura del archivo comprimido
     * @throws IOException se lanza cuando hay un error de lectura o escritura
     */

    private static void compress(final IO.Char.reader input, final IO.Bit.writer output) throws IOException {
        // writing LZSS magic byte
        output.write(MAGIC_BYTE);

        // calculating num of bits needed for offset and length according
        // to MAX_SIZE_SW size and MAX_LENGTH_COINCIDENCE
        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW);
        final int minLength = (1 + nBitsLength + nBitsOffset) / 17 + 1;

        int index, matchIndex = -1, length = 0;
        boolean extraChar = false;

        int c = input.read();
        while (c != -1) {
            actualCharacters.add((char) c);

            // check for coincidence of actualCharacters in slidingWindow
            index = kmp();

            // if actualCharacter is found inside slidingWindow, increase length and update index of match
            // otherwise, if the match is greater than minLength we compress it
            // if not we simply write the characters directly
            if (index == -1 || length == MAX_LENGTH_COINCIDENCE) {
                if (matchIndex != -1)
                    actualCharacters.remove(actualCharacters.size() - 1);
                if (length >= minLength) {
                    output.write(true);
                    output.write(new BitSetL(matchIndex - 1, nBitsOffset));
                    output.write(new BitSetL(length - minLength, nBitsLength));
                    for (int i = 0; i < length; ++i) {
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                } else {
                    final int lengthAux = actualCharacters.size();
                    for (int i = 0; i < lengthAux; ++i) {
                        output.write(false);
                        output.write(new BitSetL(actualCharacters.get(0), 16));
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                }

                // treating last character that didn't match slidingWindow
                if (matchIndex != -1) {
                    actualCharacters.add((char) c);
                    index = kmp();
                    if (index == -1) {
                        output.write(false);
                        output.write(new BitSetL(actualCharacters.get(0), 16));
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    } else
                        extraChar = true;
                }

                // setting variables depending on whether there was a leftover character
                if (!extraChar) {
                    length = 0;
                    matchIndex = -1;
                } else {
                    length = 1;
                    matchIndex = index;
                    extraChar = false;
                }

            } else {
                ++length;
                matchIndex = index;
            }

            // keep SW size within MAX_SIZE_SW
            if (slidingWindow.size() > MAX_SIZE_SW) {
                final int auxSize = slidingWindow.size();
                for (int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                    slidingWindow.remove(0);
                }
            }

            // read next char
            c = input.read();
        }

        // treating remaining characters in actualCharacters after reaching EOF
        if (actualCharacters.size() > 0) {
            if (length >= minLength) {
                output.write(true);
                output.write(new BitSetL(matchIndex - 1, nBitsOffset));
                output.write(new BitSetL(length - minLength, nBitsLength));
            } else {
                output.write(false);
                output.write(new BitSetL(actualCharacters.get(0), 16));
            }
        }
    }

    /**
     * @brief Crea el objeto lector y escritor para la descompresión y llama al descompresor
     * @param inputFilename nombre del archivo comprimido
     * @param outputFilename nombre del archivo descomprimido
     * @throws IOException se lanza cuando hay un error de lectura o escritura
     */

    public static void decompress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
                IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            decompress(input, output);
        }
    }

    /**
     *
     * @brief Usando el algoritmo LZSS esta función descomprime un archivo previamente comprimido por este compresor
     * @param input objeto de lectura del archivo comprimido
     * @param output objeto de escritura del archivo descomprimido
     * @throws IOException se lanza cuando hay un error de lecturo o escritura
     */
    private static void decompress(final IO.Bit.reader input, final IO.Char.writer output) throws IOException {
        // ignoring magic byte
        input.readByte();

        // calculating num of bits needed for offset and length according
        // to MAX_SIZE_SW size and MAX_LENGTH_COINCIDENCE
        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW);
        final int minLength = (1 + nBitsLength + nBitsOffset) / 17 + 1;

        boolean c = input.read();
        try {
            // while(true) as when EOF is reached IO.Bit.Reader will throw EOFException
            while (true) {
                // if c is 1, then read offset and length and write the characters in output
                // otherwise c = 0, so we only need to read 16 bits (one character) as it hasn't been compressed
                if (c == true) {
                    final int index = input.readBitSet(nBitsOffset).asInt() + 1;
                    final int length = input.readBitSet(nBitsLength).asInt() + minLength;
                    final int indexBase = slidingWindow.size() - index;
                    for (int i = 0; i < length; ++i) {
                        final char cAux = slidingWindow.get(indexBase + i).charValue();
                        output.write(cAux);
                        slidingWindow.add(cAux);
                    }
                } else {
                    final char cAux = (char) input.readBitSet(16).asInt();
                    output.write(cAux);
                    slidingWindow.add(cAux);
                }

                // keep SW size within MAX_SIZE_SW
                if (slidingWindow.size() > MAX_SIZE_SW) {
                    final int auxSize = slidingWindow.size();
                    for (int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                        slidingWindow.remove(0);
                    }
                }

                // read next bit
                c = input.read();
            }
        } catch (final EOFException e) {
            // EOF
        }
    }

    /**
     * @brief Calcula el vector lps, que para cada posición del vector nos dice la longitud máxima del prefijo que
     *        también es sufijo hasta esa posición
     * @param lps es un vector vacio que tras ejecutar esta función contiene para cada posición del vector la longitud
     *        máxima del prefijo que también es sufijo desde el principio hasta esa posición
     */
    private static void computeLPSArray(final int[] lps) {
        final int patLength = actualCharacters.size();
        int length = 0;
        int i = 1;

        lps[0] = 0;

        while (i < patLength) {
            if (actualCharacters.get(i) == actualCharacters.get(length)) {
                ++length;
                lps[i] = length;
                ++i;
            } else if (length != 0)
                length = lps[length - 1];
            else {
                lps[i] = 0;
                ++i;
            }
        }
    }

    /**
     * @brief Usando el algoritmo Knuth-Morris-Pratt calcula índice de la primera ocurrencia de un patron
     * @return Devuelve el indice empezando por el final de la primera ocurrencia de actualCharacters dentro de
     *         slidingWindow o -1 si actualCharacters no se encuentra dentro del slidingWindow
     */
    private static int kmp() {
        final int patLength = actualCharacters.size();
        final int txtLength = slidingWindow.size();

        final int[] lps = new int[patLength];
        computeLPSArray(lps);

        int j = 0, i = 0;

        while (i < txtLength) {
            if (slidingWindow.get(i) == actualCharacters.get(j)) {
                ++j;
                ++i;
            }
            if (j == patLength)
                return txtLength - i + j;
            else if (i < txtLength && slidingWindow.get(i) != actualCharacters.get(j)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    ++i;
            }
        }

        return -1;
    }

    /**
     * @brief Calcula el logaritmo en base 2 de un real
     * @param n es el numero para el cual queremos calcular su logaritmo en base 2
     * @return Devuelve el logaritmo en base 2 de n
     */
    private static double log2(final double n) {
        return Math.log(n)/Math.log(2);
    }
}
