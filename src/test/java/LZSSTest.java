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