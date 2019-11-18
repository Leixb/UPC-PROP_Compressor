import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZ78;

import java.io.*;

class LZ78Test {

    @Test
    void CompressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/Large.txt";
        final String outputFile = "generated/outLZ78.txt";
        final String aux = "generated/compressedFileLZ78.piz";

        try {
            LZ78.compress(inputFile, aux);
            LZ78.decompress(aux, outputFile);
            checkCompressDecompress(inputFile,outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    void checkCompressDecompress (String original, String decompressed) throws IOException{
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