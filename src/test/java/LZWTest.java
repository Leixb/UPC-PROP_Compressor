import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;

import java.io.*;

class LZWTest {

    @Test
    void compressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/DonQuijote.txt";
        final String outputFile = "generated/outLZW.txt";
        final String aux = "generated/compressedFileLZW.piz";

        try {
            LZW.compress(inputFile, aux);
            LZW.decompress(aux, outputFile);
			CheckCompDecomp.assertFileEquals(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
