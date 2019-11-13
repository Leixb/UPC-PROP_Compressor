import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;
import domini.IO;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/sampleInput.txt";
        final String outputFile = "texts/sampleOutput.txt";
        final String aux = "texts/compressedFile";

        try {
            try (IO.Char.reader input = new IO.Char.reader(inputFile); IO.Char.writer output = new IO.Char.writer(aux)) {
                LZW.compress(input,output);
            }
            try (IO.Char.reader input = new IO.Char.reader(aux); IO.Char.writer output = new IO.Char.writer(outputFile)) {
                LZW.decompress(input,output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
