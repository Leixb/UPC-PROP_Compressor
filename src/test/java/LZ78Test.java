import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZ78;

import java.io.*;

class LZ78Test {

    @Test
    void CompressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/DonQuijote.txt";
        final String outputFile = "generated/outLZ78.txt";
        final String aux = "generated/compressedFileLZ78.piz";

        try {
            LZ78.compress(inputFile, aux);
            LZ78.decompress(aux, outputFile);
			CheckCompDecomp.assertFileEquals(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
