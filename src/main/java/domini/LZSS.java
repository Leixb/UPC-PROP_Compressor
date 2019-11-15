package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

public class LZSS {
    final static int MAX_SIZE_SW = 8192;
    final static int MAX_LENGTH_COINCIDENCE = 65;
    public final static byte MAGIC_BYTE = 0x55;

    public static void compress(IO.Char.reader input, IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        int nBitsLength = (int)log2(MAX_LENGTH_COINCIDENCE-1);
        int nBitsOffset = (int)log2(MAX_SIZE_SW);
        int minLength = (1+nBitsLength+nBitsOffset)/17 + 1;

        ArrayList<Character> slidingWindow = new ArrayList<Character>();
        ArrayList<Character> actualCharacters = new ArrayList<Character>();
        int index, prevIndex = -1, length = 0;
        boolean extraChar = false;

        int c = input.read();
        while(c != -1) {
            actualCharacters.add((char)c);

            index = kmp(actualCharacters,slidingWindow);

            if(index == -1 || length == MAX_LENGTH_COINCIDENCE) {
                if(prevIndex != -1) actualCharacters.remove(actualCharacters.size()-1);
                if(length >= minLength) {
                    output.write(true);
                    boolean[] bits = intToNBits(prevIndex-1, nBitsOffset);
                    for(int j = nBitsOffset-1; j >= 0; --j) output.write(bits[j]);
                    bits = intToNBits(length-minLength, nBitsLength);
                    for(int j = nBitsLength-1; j >= 0; --j) output.write(bits[j]);
                    for(int i = 0; i < length; ++i) {
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                }
                else {
                    int lengthAux = actualCharacters.size();
                    for(int i = 0; i < lengthAux; ++i) {
                        output.write(false);
                        boolean[] bits = intToNBits(actualCharacters.get(0),16);
                        for(int j = 15; j >= 0; --j) output.write(bits[j]);
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                }

                if(prevIndex != -1) {
                    actualCharacters.add((char) c);
                    index = kmp(actualCharacters,slidingWindow);
                    if (index == -1) {
                        output.write(false);
                        boolean[] bits = intToNBits(actualCharacters.get(0),16);
                        for(int i = 15; i >= 0; --i) output.write(bits[i]);
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                    else extraChar = true;
                }

                if(!extraChar) {
                    length = 0;
                    prevIndex = -1;
                }
                else {
                    length = 1;
                    prevIndex = index;
                    extraChar = false;
                }

            }
            else {
                ++length;
                prevIndex = index;
            }

            if(slidingWindow.size() > MAX_SIZE_SW) {
                int auxSize = slidingWindow.size();
                for(int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                    slidingWindow.remove(0);
                }
            }

            c = input.read();
        }

        if(actualCharacters.size()>0) {
            if(length >= minLength) {
                output.write(true);
                boolean[] bits = intToNBits(prevIndex-1,nBitsOffset);
                for(int j = nBitsOffset-1; j >= 0; --j) output.write(bits[j]);
                bits = intToNBits(length-minLength,nBitsLength);
                for(int j = nBitsLength-1; j >= 0; --j) output.write(bits[j]);
            }
            else {
                int lengthAux = actualCharacters.size();
                output.write(false);
                boolean[] bits = intToNBits(actualCharacters.get(0),16);
                for(int j = 15; j >= 0; --j) output.write(bits[j]);
            }
        }
    }

    public static void decompress(IO.Bit.reader input, IO.Char.writer output) throws IOException {
        input.readByte();

        int nBitsLength = (int)log2(MAX_LENGTH_COINCIDENCE-1);
        int nBitsOffset = (int)log2(MAX_SIZE_SW);
        int minLength = (1+nBitsLength+nBitsOffset)/17 + 1;

        ArrayList<Character> slidingWindow = new ArrayList<Character>();

        boolean c = input.read();
        try {
            while (true) {
                if (c == true) {
                    boolean[] bits = new boolean[nBitsOffset];
                    for (int i = 0; i < nBitsOffset; ++i) bits[i] = input.read();
                    int index = bitsToInt(bits) + 1;
                    bits = new boolean[nBitsLength];
                    for (int i = 0; i < nBitsLength; ++i) bits[i] = input.read();
                    int length = bitsToInt(bits) + minLength;
                    int indexBase = slidingWindow.size() - index;
                    for (int i = 0; i < length; ++i) {
                        char cAux = slidingWindow.get(indexBase + i).charValue();
                        output.write(cAux);
                        slidingWindow.add(cAux);
                    }
                } else {
                    boolean[] bits = new boolean[16];
                    for (int i = 0; i < 16; ++i) bits[i] = input.read();
                    char cAux = (char) bitsToInt(bits);
                    output.write(cAux);
                    slidingWindow.add(cAux);
                }

                if(slidingWindow.size() > MAX_SIZE_SW) {
                    int auxSize = slidingWindow.size();
                    for(int i = 0; i < auxSize - MAX_SIZE_SW; ++i) {
                        slidingWindow.remove(0);
                    }
                }

                c = input.read();
            }
        } catch (EOFException e) {

        }
    }

    private static void computeLPSArray (ArrayList<Character> pattern, int[] lps) {
        int patLength = pattern.size();
        int length = 0;
        int i = 1;

        lps[0] = 0;

        while(i < patLength) {
            if(pattern.get(i) == pattern.get(length)) {
                ++length;
                lps[i] = length;
                ++i;
            }
            else if (length != 0) length = lps[length-1];
            else {
                lps[i] = 0;
                ++i;
            }
        }
    }

    private static int kmp(ArrayList<Character> pattern, ArrayList<Character> text) {
        int patLength = pattern.size();
        int txtLength = text.size();

        int[] lps = new int[patLength];
        computeLPSArray(pattern,lps);

        int j = 0, i = 0;

        while(i < txtLength) {
            if(text.get(i) == pattern.get(j)) {
                ++j;
                ++i;
            }
            if(j == patLength) return txtLength - i + j;
            else if (i < txtLength && text.get(i) != pattern.get(j)) {
                if(j != 0) j = lps[j - 1];
                else ++i;
            }
        }

        return -1;
    }

    private static boolean[] intToNBits(int cInt, int n) {
        boolean[] bits = new boolean[n];
        for(int i = 0; i < n; ++i) {
            int aux = cInt % 2;
            if(aux == 1) bits[i] = true;
            cInt /= 2;
        }
        return bits;
    }

    private static int bitsToInt(boolean[] bits) {
        int size = bits.length, result = 0;
        for(int i = 0; i < size; ++i) {
            if(bits[i]) result += Math.pow(2,size - 1 - i);
        }
        return  result;
    }

    private static double log2(double n) {
        return Math.log(n)/Math.log(2);
    }
}
