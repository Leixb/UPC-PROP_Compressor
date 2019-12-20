package domini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import persistencia.IO;

import java.io.*;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "data/texts/DonQuijote.txt";

        try {
            File tmpAux = File.createTempFile("LZWTest", ".lzw.piz");
            tmpAux.deleteOnExit();
            File tmpOut = File.createTempFile("LZWTest", ".txt");
            tmpOut.deleteOnExit();

            try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(tmpAux.getPath())) {
                LZW alg = new LZW();
                alg.compress(input, output);
            }
            try (IO.Bit.reader input = new IO.Bit.reader(tmpAux.getPath()); IO.Byte.writer output = new IO.Byte.writer(tmpOut.getPath())) {
                LZW alg = new LZW();
                alg.decompress(input, output);
            }
            CheckCompDecomp.assertFileEquals(inputFile, tmpOut.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
