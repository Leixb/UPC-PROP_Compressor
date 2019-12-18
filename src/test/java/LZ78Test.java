import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZ78;
import persistencia.IO;

import java.io.*;

class LZ78Test {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/DonQuijote.txt";

        try {
            File tmpAux = File.createTempFile("LZ78Test", ".lz78.piz");
            tmpAux.deleteOnExit();
            File tmpOut = File.createTempFile("LZ78Test", ".txt");
            tmpOut.deleteOnExit();

            try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(tmpAux.getPath())) {
                LZ78.compress(input, output);
            }
            try (IO.Bit.reader input = new IO.Bit.reader(tmpAux.getPath()); IO.Byte.writer output = new IO.Byte.writer(tmpOut.getPath())) {
                LZ78.decompress(input, output);
            }
            CheckCompDecomp.assertFileEquals(inputFile, tmpOut.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
