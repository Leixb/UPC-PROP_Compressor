package domini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class JPEGBlockTest {

    @Test
    void Dct() {
        final int TOLERANCE = 2;

        byte[][] block = new byte[8][8];
        for (int i=0; i < 8; ++i) {
            for (int j=0; j < 8; ++j) {
                block[i][j] = (byte)(i*8+j);
            }
        }
        double[][] dctBlock = JPEGBlock.DCT.encode(block);
        byte[][] gotBlock = JPEGBlock.DCT.decode(dctBlock);


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

        short[] gotLine = JPEGBlock.ZigZag.encode(JPEGBlock.ZigZag.decode(line));
        short[][] gotBlock = JPEGBlock.ZigZag.decode(JPEGBlock.ZigZag.encode(block));

        assertArrayEquals(line, gotLine);
        for (int i = 0; i < 8; ++i) {
            assertArrayEquals(block[i], gotBlock[i], "ZigZag");
        }
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
        final short quality = 100;
        final boolean isChrominance = true;

        byte[][] input = sampleBlock;

        double[][] DctEnc = JPEGBlock.DCT.encode(input);
        short[][] quantEnc = JPEGBlock.Quantization.encode(quality, isChrominance, DctEnc);
        short[] zigEnc = JPEGBlock.ZigZag.encode(quantEnc);
        short[] rleEnc = JPEGBlock.RLE.encode(zigEnc);

        short[] rleDec = JPEGBlock.RLE.decode(rleEnc);
        short[][] zigDec = JPEGBlock.ZigZag.decode(rleDec);
        double[][] quantDec = JPEGBlock.Quantization.decode(quality, isChrominance, zigDec);
        byte[][] result = JPEGBlock.DCT.decode(quantDec);

        if (!CompareWithTolerance(input, result, TOLERANCE)) {
            print("INPUT", input);
            print("DCT enc", DctEnc);
            print("Quant enc", quantEnc);
            print("Zig enc", zigEnc);
            print("RLE enc", rleEnc);

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

        short[] encoded = JPEGBlock.encode((short) 70, true, sampleBlock);
        byte[][] result = JPEGBlock.decode((short) 70, true, encoded);

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
            System.out.printf("%04x ", array[i]);
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
