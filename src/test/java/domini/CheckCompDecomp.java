package domini;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class CheckCompDecomp {
    static public void assertFileEquals (String original, String decompressed) throws IOException {
        try(BufferedReader origin = new BufferedReader(new FileReader(original));
            BufferedReader decomp = new BufferedReader(new FileReader(decompressed))) {

            String checkLine;

            checkLine = origin.readLine();
            while (checkLine != null) {
                assertEquals(checkLine, decomp.readLine());
                checkLine = origin.readLine();
            }
            assertNull(decomp.readLine(),"Compressio i decompressio fallida!");
        }
    }
}
