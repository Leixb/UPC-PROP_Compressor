package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

public final class LZSS extends LZ {

    private LZSS() {}

    final static int MAX_SIZE_SW = 8192;
    final static int MAX_LENGTH_COINCIDENCE = 65;
    public final static byte MAGIC_BYTE = 0x55;

    public static void compress(final IO.Char.reader input, final IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW);
        final int minLength = (1 + nBitsLength + nBitsOffset) / 17 + 1;

        final ArrayList<Character> slidingWindow = new ArrayList<Character>();
        final ArrayList<Character> actualCharacters = new ArrayList<Character>();
        int index, prevIndex = -1, length = 0;
        boolean extraChar = false;

        int c = input.read();
        while (c != -1) {
            actualCharacters.add((char) c);

            index = kmp(actualCharacters, slidingWindow);

            if (index == -1 || length == MAX_LENGTH_COINCIDENCE) {
                if (prevIndex != -1)
                    actualCharacters.remove(actualCharacters.size() - 1);
                if (length >= minLength) {
                    output.write(true);
                    output.write(new BitSetL(prevIndex - 1, nBitsOffset));
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

                if (prevIndex != -1) {
                    actualCharacters.add((char) c);
                    index = kmp(actualCharacters, slidingWindow);
                    if (index == -1) {
                        output.write(false);
                        output.write(new BitSetL(actualCharacters.get(0), 16));
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    } else
                        extraChar = true;
                }

                if (!extraChar) {
                    length = 0;
                    prevIndex = -1;
                } else {
                    length = 1;
                    prevIndex = index;
                    extraChar = false;
                }

            } else {
                ++length;
                prevIndex = index;
            }

            if (slidingWindow.size() > MAX_SIZE_SW) {
                final int auxSize = slidingWindow.size();
                for (int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                    slidingWindow.remove(0);
                }
            }

            c = input.read();
        }

        if (actualCharacters.size() > 0) {
            if (length >= minLength) {
                output.write(true);
                output.write(new BitSetL(prevIndex - 1, nBitsOffset));
                output.write(new BitSetL(length - minLength, nBitsLength));
            } else {
                output.write(false);
                output.write(new BitSetL(actualCharacters.get(0), 16));
            }
        }
    }

    public static void decompress(final IO.Bit.reader input, final IO.Char.writer output) throws IOException {
        input.readByte();

        final int nBitsLength = (int) log2(MAX_LENGTH_COINCIDENCE - 1);
        final int nBitsOffset = (int) log2(MAX_SIZE_SW);
        final int minLength = (1 + nBitsLength + nBitsOffset) / 17 + 1;

        final ArrayList<Character> slidingWindow = new ArrayList<Character>();

        boolean c = input.read();
        try {
            while (true) {
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

                if (slidingWindow.size() > MAX_SIZE_SW) {
                    final int auxSize = slidingWindow.size();
                    for (int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                        slidingWindow.remove(0);
                    }
                }

                c = input.read();
            }
        } catch (final EOFException e) {
            // EOF  
        }
    }

    private static void computeLPSArray(final ArrayList<Character> pattern, final int[] lps) {
        final int patLength = pattern.size();
        int length = 0;
        int i = 1;

        lps[0] = 0;

        while (i < patLength) {
            if (pattern.get(i) == pattern.get(length)) {
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

    private static int kmp(final ArrayList<Character> pattern, final ArrayList<Character> text) {
        final int patLength = pattern.size();
        final int txtLength = text.size();

        final int[] lps = new int[patLength];
        computeLPSArray(pattern, lps);

        int j = 0, i = 0;

        while (i < txtLength) {
            if (text.get(i) == pattern.get(j)) {
                ++j;
                ++i;
            }
            if (j == patLength)
                return txtLength - i + j;
            else if (i < txtLength && text.get(i) != pattern.get(j)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    ++i;
            }
        }

        return -1;
    }

    private static double log2(final double n) {
        return Math.log(n)/Math.log(2);
    }
}
