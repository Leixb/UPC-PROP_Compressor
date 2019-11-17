import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZW;

class LZWTest {

    @Test
    void compressDecompressFile() {
        final String inputFile = "texts/Gordo.txt";//"images/gonza.ppm";//"texts/DonQuijote.txt";//"texts/sampleInput.txt";//"texts/prova.txt";//
        final String outputFile = "texts/GordoOut.txt";//"images/gonzaOut.ppm";//"texts/DonQuijoteOut.txt";//"texts/sampleOutput.txt";//"texts/provaOut.txt";//
        final String aux = "texts/compressedFile.txt";

        try {
            LZW.compress(inputFile, aux);
            LZW.decompress(aux, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
