import domini.JPEG;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JPEGTest {

    @Test
    void Dct() {
        final int TOLERANCE = 2;

        byte[][] block = new byte[8][8];
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
            }
        }
        double[][] dctBlock = JPEG.DCT.encode(block);
        byte[][] gotBlock = JPEG.DCT.decode(dctBlock);


        assertTrue(
                CompareWithTolerance(gotBlock, block, TOLERANCE),
                String.format("DCT, diff >=%d", TOLERANCE)
        );
    }

    @Test
    void Quantization() { }

    @Test
    void ZigZag() {
        short[] line = new short[64];
        short[][] block = new short[8][8];

        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
                line[i*8+j] = (byte)(i*8+j);
            }
        }

        short[] gotLine = JPEG.ZigZag.encode(JPEG.ZigZag.decode(line));
        short[][] gotBlock = JPEG.ZigZag.decode(JPEG.ZigZag.encode(block));

        assertArrayEquals(line, gotLine);
        for (int i = 0; i < 8; ++i) {
            assertArrayEquals(block[i], gotBlock[i], "ZigZag");
        }
    }

    @Test
    void RLE() {
        short[] smallInput = {0x1FF, 11, 0, 24, 12, 30, 0, 0, 0, 0, 5, 0, 1, 2, 0, 0, 0, 0, 0, 0};
        final byte[] expected = {0, (byte)0xFF, 0, 0x1, 0, 11, 3, 24, 1, 12, 1, 30, 9, 5, 3, 1, 1, 2, 0, 0};
        // Al passar de short a int tenim sempre el doble de 0 i un extra

        short[] input = new short[64];
        System.arraycopy(smallInput, 0, input, 0, smallInput.length);

        print("EXPECTED: ", expected);
        print("GOT", JPEG.RLE.encode(input));

        assertArrayEquals(JPEG.RLE.encode(input), expected, "RLE encode");
        assertArrayEquals(JPEG.RLE.decode(expected), input, "RLE decode");
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
    void all() {

        final int TOLERANCE = 15; // diff max to fail test

        // Quantization parameters
        final short quality = 75;
        final boolean isChrominance = true;

        byte[][] input = sampleBlock;

        double[][] DctEnc = JPEG.DCT.encode(input);
        short[][] quantEnc = JPEG.Quantization.encode(quality, isChrominance, DctEnc);
        short[] zigEnc = JPEG.ZigZag.encode(quantEnc);
        byte[] rleEnc = JPEG.RLE.encode(zigEnc);
        byte[] huffEnc = JPEG.Huffman.encode(rleEnc);

        byte[] huffDec = JPEG.Huffman.decode(huffEnc);
        short[] rleDec = JPEG.RLE.decode(huffDec);
        short[][] zigDec = JPEG.ZigZag.decode(rleDec);
        double[][] quantDec = JPEG.Quantization.decode(quality, isChrominance, zigDec);
        byte[][] result = JPEG.DCT.decode(quantDec);

        if (!CompareWithTolerance(input, result, TOLERANCE)) {
            print("INPUT", input);
            print("DCT enc", DctEnc);
            print("Quant enc", quantEnc);
            print("Zig enc", zigEnc);
            print("RLE enc", rleEnc);
            print("Huff enc", huffEnc);

            print("Huff dec", huffDec);
            print("RLE dec", rleDec);
            print("Zig dec", zigDec);
            print("Quant dec", quantDec);
            print("DCT dec", result);

            fail(String.format("ALL diff >%d, (quality=%d, isChrom=%b)", TOLERANCE, quality, isChrominance));
        }
    }

    @Test
    void encodeDecode() {
        final int TOLERANCE = 20;

        byte[] encoded = JPEG.encode(sampleBlock);
        byte[][] result = JPEG.decode(encoded);

        assertTrue(
                CompareWithTolerance(sampleBlock, result, TOLERANCE),
                String.format("encode -> decode diff >%d", TOLERANCE)
        );
    }

    boolean CompareWithTolerance(byte[][] a, byte[][] b, int tolerance) {
        if (a.length != b.length) return false;

        for (int i=0; i < a.length; ++i) {
            if (a[i].length != b[i].length) return false;
            for (int j=0; j < a[i].length; ++j)
                if (Math.abs(a[i][j]-b[i][j])>= tolerance) return false;
        }

        return true;
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
                System.out.printf("%.1f ", data[i][j]);
            System.out.println();
        }
    }

    void print(String name, byte[] array) {
        System.out.println(name);
        for (int i=0; i < array.length; ++i)
            System.out.printf("%03d ", array[i]);
        System.out.println();
    }

    void print(String name, short[] array) {
        System.out.println(name);
        for (int i=0; i < array.length; ++i)
            System.out.printf("%03d ", array[i]);
        System.out.println();
    }

    void print(String name, short[][] data) {
        System.out.println(name);
        for (int i=0; i < data.length; ++i) {
            for (int j=0; j < data[i].length; ++j)
                System.out.printf("%03d ", data[i][j]);
            System.out.println();
        }
    }

}
