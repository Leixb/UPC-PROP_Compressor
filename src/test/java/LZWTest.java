import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import Domini.LZW;
import Domini.IO;

import java.io.FileNotFoundException;
import java.io.IOException;

class LZWTest {

    @Test
    void compressDecompressFile {
        final String inputFile = "~/Imágenes/sampleInput.jpg";
        final String outputFile = "sampleOutput.jpg";
        final String aux = "compressedFile";​

        try {
            try (IO.reader input = new IO.reader(inputFile), IO.writer output = new IO.writer(aux)) {
                LZW.compress(input,output);
            }
​
            try (IO.reader input = new IO.reader(aux), IO.writer output = new IO.writer(output)) {
                LZW.decompress(input,output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
