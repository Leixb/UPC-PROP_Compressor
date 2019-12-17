import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;
import persistencia.IO;

import java.io.*;

class LZWTest {

    @Test
    void compressDecompressFile() {
        new File("generated/").mkdirs();
        final String inputFile = "texts/DonQuijote.txt";
        final String outputFile = "generated/outLZW.txt";
        final String aux = "generated/compressedFileLZW.piz";

        try {
			try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(aux)) {
				LZW.compress(input, output);
			}
			try (IO.Bit.reader input = new IO.Bit.reader(aux); IO.Byte.writer output = new IO.Byte.writer(outputFile)) {
				LZW.decompress(input, output);
			}
			CheckCompDecomp.assertFileEquals(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
