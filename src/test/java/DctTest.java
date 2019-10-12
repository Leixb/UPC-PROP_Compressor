import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DctTest {

    @Test
    void Dct() {
        byte[][] block = new byte[8][8];
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
            }
        }
        double[][] dctBlock = Dct.forwardDct(block);
        byte[][] gotBlock = Dct.backwardDct(dctBlock);

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                int diff = Math.abs(gotBlock[i][j]-block[i][j]);
                assertTrue(diff < 2);
            }
        }
    }

    @Test
    void folds() {
        byte[] line = new byte[64];
        byte[][] block = new byte[8][8];

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
                line[i*8+j] = (byte)(i*8+j);
            }
        }

        byte[] gotLine = Dct.unfoldFromBlock(Dct.foldToBlock(line));
        byte[][] gotBlock = Dct.foldToBlock(Dct.unfoldFromBlock(block));
        assertArrayEquals(line, gotLine);
        for (int i = 0; i < 8; ++i) {
            assertArrayEquals(block[i], gotBlock[i]);
        }
    }

    @Test
    void quantizationValue() {
    }
}