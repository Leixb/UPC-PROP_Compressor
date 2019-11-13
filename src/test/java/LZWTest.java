import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;
import domini.IO;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/sampleInput.txt";//"images/gonza.ppm";//"texts/DonQuijote.txt";
        final String outputFile = "texts/sampleOutput.txt";//"images/gonzaOut.ppm";//"texts/DonQuijoteOut.txt";
        final String aux = "texts/compressedFile";

        try {
            try (IO.Char.reader input = new IO.Char.reader(inputFile); IO.Char.writer output = new IO.Char.writer(aux)) {
                LZW.compress(input,output);
            }
            try (IO.Char.reader input = new IO.Char.reader(aux); IO.Char.writer output = new IO.Char.writer(outputFile)) {
                int c = input.read();
                if (c == 0x69) System.out.println("Todo OK.");
                LZW.decompress(input,output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
