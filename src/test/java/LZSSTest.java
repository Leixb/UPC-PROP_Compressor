import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZSS;

import java.io.*;

class LZSSTest {

    @Test
    void CompressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/DonQuijote.txt";
        final String outputFile = "generated/outLZSS.txt";
        final String aux = "generated/compressedFileLZSS.piz";

        try {
            LZSS.compress(inputFile, aux);
            LZSS.decompress(aux, outputFile);
			CheckCompDecomp.assertFileEquals(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
