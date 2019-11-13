package domini;

import java.io.IOException;
import java.util.ArrayList;

public class LZSS implements FileCodec{
    final static int MAX_SIZE_SW = 4096; // 13 bits for offset
    final static int MAX_LENGTH_COINCIDENCE = 34; // 5 bits for length

    public static void compress(IO.Char.reader input, IO.Char.writer output) throws IOException {
            ArrayList<Character> slidingWindow = new ArrayList<Character>();
            ArrayList<Character> actualCharacters = new ArrayList<Character>();
            int index, prevIndex = -1, length = 0;

            int c = input.read();
            while(c != -1) {
                actualCharacters.add((char)c);

                index = kmp(actualCharacters,slidingWindow);

                if(index == -1) {
                    if(length >= 3) {

                    }
                    else {
                        for(int i = 0; i < actualCharacters.size(); ++i) {
                            output.write('0');
                            output.write(actualCharacters.get(0).charValue());
                            slidingWindow.add(actualCharacters.get(0));
                            actualCharacters.remove(0);
                        }
                    }
                }
                else {
                    ++length;
                    prevIndex = index;
                }

                /*actualCharacters.add((char)c);

                length = 0;
                prevIndex = -1;

                index = kmp(actualCharacters,slidingWindow);
                while (index != -1) {
                    if(actualCharacters.size() > MAX_LENGTH_COINCIDENCE) break;
                    ++length;
                    c = input.read();
                    if(c == -1) break;
                    actualCharacters.add((char)c);
                    prevIndex = index;
                    index = kmp(actualCharacters,slidingWindow);
                }

                if(prevIndex != -1) actualCharacters.remove(actualCharacters.size()-1);

                if(length >= 3) {
                    System.out.println(prevIndex);
                    System.out.println(length);
                    output.write((byte)'1');
                    output.write((byte)((char)prevIndex));
                    output.write((byte)((char)length));
                    for(int i = 0; i < actualCharacters.size(); ++i) {
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                }
                else {
                    for(int i = 0; i < actualCharacters.size(); ++i) {
                        output.write((byte)'0');
                        output.write((byte)actualCharacters.get(0).charValue());
                        slidingWindow.add(actualCharacters.get(0));
                        actualCharacters.remove(0);
                    }
                }

                if(slidingWindow.size() > MAX_SIZE_SW) {
                    for(int i = 0; i < slidingWindow.size() - MAX_SIZE_SW; ++i) slidingWindow.remove(0);
                }

                c = input.read();*/
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
            else if (length != 0) length = lps[i-1];
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
}
