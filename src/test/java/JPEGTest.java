import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JPEGTest {

    @Test
    void Dct() {
        byte[][] block = new byte[8][8];
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
            }
        }
        double[][] dctBlock = JPEG.DCT.encode(block);
        byte[][] gotBlock = JPEG.DCT.decode(dctBlock);

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                int diff = Math.abs(gotBlock[i][j]-block[i][j]);
                assertTrue(diff < 2);
            }
        }
    }

    @Test
    void ZigZag() {
        byte[] line = new byte[64];
        byte[][] block = new byte[8][8];

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
                line[i*8+j] = (byte)(i*8+j);
            }
        }

        byte[] gotLine = JPEG.ZigZag.encode(JPEG.ZigZag.decode(line));
        byte[][] gotBlock = JPEG.ZigZag.decode(JPEG.ZigZag.encode(block));

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
        byte[][] input = sampleBlock;

        final short quality = 75;
        final boolean isChrominance = true;

        double[][] DctEnc = JPEG.DCT.encode(input);
        byte[][] quantEnc = JPEG.Quantization.encode(quality, isChrominance, DctEnc);
        byte[] zigEnc = JPEG.ZigZag.encode(quantEnc);
        byte[] rleEnc = JPEG.RLE.encode(zigEnc);

        byte[] rleDec = JPEG.RLE.decode(rleEnc);
        byte[][] zigDec = JPEG.ZigZag.decode(rleDec);
        double[][] quantDec = JPEG.Quantization.decode(quality, isChrominance, zigDec);
        byte[][] result = JPEG.DCT.decode(quantDec);

        print("INPUT", input);
        print("DCT enc", DctEnc);
        print("Quant enc", quantEnc);
        print("Zig enc", zigEnc);
        print("RLE enc", rleEnc);

        print("RLE dec", rleDec);
        print("Zig dec", zigDec);
        print("Quant dec", quantDec);
        print("DCT dec", result);

        boolean ok = true;

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                int diff = Math.abs(input[i][j]-result[i][j]);
                ok = ok && diff < 3;
            }
        }

        assertTrue(ok);
    }

    void print(String name, byte[][] data) {
        System.out.println(name);
        for (int i=0; i < data.length; ++i) {
            for (int j=0; j < data[i].length; ++j)
                System.out.printf("%03d ", data[i][j]);
            System.out.println();
        }
    }

    void print(String name, double[][] data) {
        System.out.println(name);
        for (int i=0; i < data.length; ++i) {
            for (int j=0; j < data[i].length; ++j)
                System.out.printf("%.3f ", data[i][j]);
            System.out.println();
        }
    }

    void print(String name, byte[] array) {
        System.out.println(name);
        for (int i=0; i < array.length; ++i)
            System.out.printf("%03d ", array[i]);
        System.out.println();
    }

}
