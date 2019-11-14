import domini.Huffman;
import domini.IO;
import domini.JPEG;
import domini.JPEGBlock;
import domini.PpmImage.InvalidFileFormat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class JPEG_Test {

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
    void RW() throws Exception {
        Huffman huff = new Huffman(true, true);
        short[] code = JPEGBlock.encode(sampleBlock);
        try (IO.Bit.writer file = new IO.Bit.writer("testFile")) {
            JPEG.writeBlock(code, huff, file);
        }

        short[] result, resultB;

        try (IO.Bit.reader file = new IO.Bit.reader("testFile")) {
            result = JPEG.decodeBlock(huff, file);
        }

        print("Original", code);
        print("Result", result);

        assertArrayEquals(code, result);

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

    @Test
    void test() {
        try {
            JPEG.compress("images/boat_frag32.ppm", "boat.out", (short) 50);
            JPEG.decompress("boat.out", "boat_rec.ppm", (short) 50);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }
}