import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;

import java.io.*;

class LZWTest {

    @Test
    void compressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/Large.txt";
        final String outputFile = "generated/outLZW.txt";
        final String aux = "generated/compressedFileLZW.piz";

        try {
            LZW.compress(inputFile, aux);
            LZW.decompress(aux, outputFile);
            checkCompressDecompress(inputFile,outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    void checkCompressDecompress (String original, String decompressed) throws IOException {
        BufferedReader origin = new BufferedReader(new FileReader(original));
        BufferedReader decomp = new BufferedReader(new FileReader(decompressed));
        String checkLine;

        checkLine = origin.readLine();
        while (checkLine != null) {
            assertEquals(checkLine, decomp.readLine());
            checkLine = origin.readLine();
        }
        assertNull(decomp.readLine(),"Compressio i decompressio fallida!");
    }
}
