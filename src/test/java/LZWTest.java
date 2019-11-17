import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;

import java.io.File;

class LZWTest {

    @Test
    void compressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/Gordo.txt";
        final String outputFile = "generated/out.txt";
        final String aux = "generated/compressedFile.txt";

        try {
            LZW.compress(inputFile, aux);
            LZW.decompress(aux, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
