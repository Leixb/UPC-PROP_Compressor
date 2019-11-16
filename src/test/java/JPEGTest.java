import domini.Huffman;
import domini.IO;
import domini.JPEG;
import domini.JPEGBlock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JPEGTest {

    private static final byte[][] sampleBlock = {
            {16,11,21,27,25,27,27,29},
            {23,23,23,31,28,28,28,31},
            {23,28,32,34,31,23,23,24},
            {30,35,33,32,32,32,32,37},
            {30,32,33,34,32,27,27,30},
            {33,33,33,33,32,29,29,28},
            {34,34,33,32,33,29,29,30},
            {34,34,33,32,35,29,30,25}
    };

    @Test
    void RW() throws Exception {
        Huffman huffAC = new Huffman(true, true);
        Huffman huffDC = new Huffman(false, true);
        short[] code = JPEGBlock.encode(sampleBlock);
        try (IO.Bit.writer file = new IO.Bit.writer("testFile")) {
            JPEG.writeBlock(code, huffAC, huffDC, file);
            JPEG.writeBlock(code, huffAC, huffDC, file);
        }

        short[] result, resultB;

        try (IO.Bit.reader file = new IO.Bit.reader("testFile")) {
            result = JPEG.readBlock(huffAC, huffDC, file);
            resultB = JPEG.readBlock(huffAC, huffDC, file);
        }

        print("Original", code);
        print("Result", result);
        print("ResultB", result);

        assertArrayEquals(code, result);
        assertArrayEquals(code, resultB);

    }

    void print(String name, short[] array) {
        System.out.println(name);
        for (int i=0; i < array.length; ++i)
            System.out.printf("%04x ", array[i]);
        System.out.println();
        for (int i=0; i < array.length; ++i)
            System.out.printf("%04d ", array[i]);
        System.out.println();
    }

    //@Test
    void test() {
        try {
            JPEG.compress("images/boat.ppm", "boat.out", (short) 50);
            JPEG.decompress("boat.out", "boat_rec.ppm");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}