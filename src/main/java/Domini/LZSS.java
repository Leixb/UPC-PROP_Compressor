package Domini;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LZSS {
    final static int MAX_SIZE_SW = 4096;
    public void compressLZSS(String fileIn, String fileOut) throws IOException {
        IO.reader input = new IO.reader(fileIn);
        IO.writer output = new IO.writer(fileOut);
        ArrayList<Character> SlidingWindow = new ArrayList<Character>();



        while(input.read() != -1) {

        }
    }
}
