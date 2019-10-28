import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    private static final byte[][] sampleBlock = {
            {16,11,21,27,25,27,27,27},
            {23,23,23,31,28,28,28,30},
            {23,28,32,34,31,23,23,23},
            {30,35,33,32,32,32,32,33},
            {30,32,33,34,32,27,27,28},
            {33,33,33,33,32,29,29,29},
            {34,34,33,32,33,29,29,29},
            {34,34,33,32,35,29,30,26}
    };

    @Test
    void RLE() {
    }

    @Test
    void all() {
        short quality = 100;
        double[][] dct = Dct.forwardDct(sampleBlock);
        byte[][] quant = Dct.Quanitzate(quality, dct);
        byte[] folded = Dct.unfoldFromBlock(quant);
        byte[] rle = Dct.RLE(folded);

        byte[] unRle = new byte[0];
        try {
            unRle = Dct.undoRLE(rle);
        } catch (IOException e) {
            fail(e);
        }
        byte[][] unFolded = Dct.foldToBlock(unRle);
        double[][] unQuant = Dct.DeQuanitzate(quality, unFolded);
        byte[][] unDct = Dct.backwardDct(unQuant);

        boolean ok = true;

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                int diff = Math.abs(sampleBlock[i][j]-unDct[i][j]);
                ok = ok && diff < 2;
            }
        }

        System.out.println("ORIGINAL:");
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) System.out.printf("%03d ", sampleBlock[i][j]);
            System.out.println();
        }

        System.out.println("RESTORED:");
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) System.out.printf("%03d ", unDct[i][j]);
            System.out.println();
        }

        assertTrue(ok);
    }
}