import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;
import domini.IO;

import java.io.File;

class LZWTest {

    @Test
    void compressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/hola.txt";
        final String outputFile = "generated/out.txt";
        final String aux = "generated/compressedFile.txt";

        try {
            try (IO.Char.reader input = new IO.Char.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(aux)) {
                LZW.compress(input,output);
            }
            try (IO.Bit.reader input = new IO.Bit.reader(aux); IO.Char.writer output = new IO.Char.writer(outputFile)) {
                LZW.decompress(input,output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
