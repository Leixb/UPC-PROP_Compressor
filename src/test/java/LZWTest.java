import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;
import domini.IO;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/hola.txt";
        final String outputFile = "generated/out.txt";
        final String aux = "generated/compressedFile.txt";

        try {
            try (IO.Char.reader input = new IO.Char.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(aux)) {
                System.out.println("Starting compression...");
                LZW.compress(input,output);
                System.out.println("File successfully compressed.");
            }
            try (IO.Bit.reader input = new IO.Bit.reader(aux); IO.Char.writer output = new IO.Char.writer(outputFile)) {
                System.out.println("Starting decompression...");
                LZW.decompress(input,output);
                System.out.println("File successfully decompressed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
