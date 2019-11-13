import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Domini.LZW;
import Domini.IO;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/sampleInput.txt";
        final String outputFile = "texts/sampleOutput.txt";
        final String aux = "texts/compressedFile";

        try {
            try (IO.reader input = new IO.reader(inputFile); IO.writer output = new IO.writer(aux)) {
                LZW.compress(input,output);
            }
            try (IO.reader input = new IO.reader(aux); IO.writer output = new IO.writer(outputFile)) {
                LZW.decompress(input,output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
