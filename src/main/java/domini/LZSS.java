/**
 * @file ./src/main/java/domini/LZSS.java
 * @author Albert Mercadé Plasencia
*/
package domini;

import persistencia.IO;

import java.io.IOException;

/**
 * @brief Compresión y decompresión de archivos de texto con LZSS
 */
public final class LZSS {

    private LZSS() {}

    final static int MAX_SIZE_SW = 1023; // maximum size of the sliding window
    final static int MAX_LENGTH_COINCIDENCE = 17; //
    final static byte[] slidingWindow = new byte[MAX_SIZE_SW];
    final static byte[] actualCharacters = new byte[MAX_LENGTH_COINCIDENCE];
    public final static byte MAGIC_BYTE = 0x55; // magic byte for LZSS
    private final static int EOF = 0; // Pseudo EOF

    /**
     * @brief Usando el algoritmo LZSS esta función comprime un archivo txt
     * @param input objeto de lectura del archivo que se quiere comprimir
     * @param output objeto de escritura del archivo comprimido
     * @throws IOException se lanza cuando hay un error de lectura o escritura
     */
    public static void compress(final IO.Byte.reader input, final IO.Bit.writer output) throws IOException {
        // writing LZSS magic byte
        output.write(MAGIC_BYTE);

        // calculating num of bits needed for offset and length according
        // to MAX_SIZE_SW size and MAX_LENGTH_COINCIDENCE
        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW + 1);
        int minLength = (1 + nBitsLength + nBitsOffset) / 9 + 1;
        if(minLength < 2) minLength = 2;

        int currentSWIndex = 0, currentACIndex = 0;
        boolean fullSW = false;

        int index = 0, prevMatchingIndex = 0;
        boolean extraChar = false;

        byte c;
        int iAux = input.read();

        while(iAux != -1) {
            c = (byte)iAux;
            actualCharacters[currentACIndex] = c;
            ++currentACIndex;

            prevMatchingIndex = index;
            index = kmp(currentACIndex, currentSWIndex, fullSW);

            if(index == -1 || currentACIndex >= MAX_LENGTH_COINCIDENCE) {
                int auxACIndex = currentACIndex;
                if(auxACIndex >= 2) --auxACIndex;
                if(auxACIndex >= minLength) {
                    output.write(true);
                    output.write(new BitSetL(prevMatchingIndex, nBitsOffset));
                    output.write(new BitSetL(auxACIndex - minLength, nBitsLength));
                    for(int i = 0; i < auxACIndex; ++i) {
                        slidingWindow[currentSWIndex] = actualCharacters[i];
                        ++currentSWIndex;
                        if(currentSWIndex >= MAX_SIZE_SW) {
                            currentSWIndex = 0;
                            fullSW = true;
                        }
                    }
                }
                else {
                    for(int i = 0; i < auxACIndex; ++i) {
                        output.write(false);
                        output.write(new BitSetL(actualCharacters[i], 8));
                        slidingWindow[currentSWIndex] = actualCharacters[i];
                        ++currentSWIndex;
                        if(currentSWIndex >= MAX_SIZE_SW) {
                            currentSWIndex = 0;
                            fullSW = true;
                        }
                    }
                }

                if(currentACIndex >= 2) extraChar = true;
                currentACIndex = 0;
            }
            if(!extraChar) iAux = input.read();
            extraChar = false;
        }

        if(currentACIndex >= minLength) {
            output.write(true);
            output.write(new BitSetL(index - 1, nBitsOffset));
            output.write(new BitSetL(currentACIndex - minLength, nBitsLength));
        }
        else if (currentACIndex > 0) {
            for(int i = 0; i < currentACIndex; ++i) {
                output.write(false);
                output.write(new BitSetL(actualCharacters[i], 8));
            }
        }

        output.write(true);
        output.write(new BitSetL(EOF, nBitsLength + nBitsOffset));
    }

    /**
     *
     * @brief Usando el algoritmo LZSS esta función descomprime un archivo previamente comprimido por este compresor
     * @param input objeto de lectura del archivo comprimido
     * @param output objeto de escritura del archivo descomprimido
     * @throws IOException se lanza cuando hay un error de lecturo o escritura
     */
    public static void decompress(final IO.Bit.reader input, final IO.Byte.writer output) throws IOException {
        // ignoring magic byte
        input.readByte();

        // calculating num of bits needed for offset and length according
        // to MAX_SIZE_SW size and MAX_LENGTH_COINCIDENCE
        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW + 1);
        int minLength = (1 + nBitsLength + nBitsOffset) / 9 + 1;
        if(minLength < 2) minLength = 2;
        int currentSWIndex = 0;

        boolean c = input.read();
        boolean eof = false;

        while (!eof) {
            // if c is 1, then read offset and length and write the characters in output
            // otherwise c = 0, so we only need to read 16 bits (one character) as it hasn't been compressed
            if (c == true) {
                final int index = input.readBitSet(nBitsOffset).asInt();

                if(index == 0) eof = true;
                else {
                    final int length = input.readBitSet(nBitsLength).asInt() + minLength;
                    int indexBase;

                    if(currentSWIndex - index < 0) indexBase = MAX_SIZE_SW + currentSWIndex - index;
                    else indexBase = currentSWIndex - index;

                    for (int i = 0; i < length; ++i) {
                        byte cAux = slidingWindow[indexBase];
                        ++indexBase;
                        if(indexBase >= MAX_SIZE_SW) indexBase = 0;

                        output.write(cAux);
                        slidingWindow[currentSWIndex] = (byte)cAux;
                        ++currentSWIndex;
                        if(currentSWIndex >= MAX_SIZE_SW) currentSWIndex = 0;
                    }
                }
            } else {
                byte cAux = (byte)input.readBitSet(8).asInt();
                output.write(cAux);
                slidingWindow[currentSWIndex] = cAux;
                ++currentSWIndex;
                if(currentSWIndex >= MAX_SIZE_SW) currentSWIndex = 0;
            }

            //read next bit
            if (!eof) c = input.read();
        }
    }

    /**
     * @brief Calcula el vector lps, que para cada posición del vector nos dice la longitud máxima del prefijo que
     *        también es sufijo hasta esa posición
     * @param lps es un vector vacio que tras ejecutar esta función contiene para cada posición del vector la longitud
     *        máxima del prefijo que también es sufijo desde el principio hasta esa posición
     */
    private static void computeLPSArray(final int[] lps, int patLength) {
        int length = 0;
        int i = 1;

        lps[0] = 0;

        while (i < patLength) {
            if (actualCharacters[i] == actualCharacters[length]) {
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
    private static int kmp(int currentACIndex, int currentSWIndex, boolean fullSW) {
        final int patLength = currentACIndex, txtLength = currentSWIndex;

        final int[] lps = new int[patLength];
        computeLPSArray(lps, patLength);

        int j = 0, i;
        if(fullSW) i = currentSWIndex;
        else i = 0;
        boolean end = false;

        while (!end) {
            if (slidingWindow[i] == actualCharacters[j]) {
                ++j;
                ++i;
                if(i == MAX_SIZE_SW) i = 0;
                if(i == txtLength) end = true;
            }
            if (j == patLength) {
                int index = txtLength - i + j;
                if(index <= 0) return index + MAX_SIZE_SW;
                else return index;
            }
            else if (!end && slidingWindow[i] != actualCharacters[j]) {
                if (j != 0)
                    j = lps[j - 1];
                else {
                    ++i;
                    if(i == MAX_SIZE_SW) i = 0;
                    if(i == txtLength) end = true;
                }
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
