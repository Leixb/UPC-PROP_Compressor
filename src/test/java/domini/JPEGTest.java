package domini;

import persistencia.IO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

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
        File tmpFile = File.createTempFile("JPEG_RW_test", ".block");
        tmpFile.deleteOnExit();

        Huffman huffAC = new Huffman(true, true);
        Huffman huffDC = new Huffman(false, true);
        short[] code = JPEGBlock.encode((short) 70, true, sampleBlock);
        try (IO.Bit.writer file = new IO.Bit.writer(tmpFile.getPath())) {
            JPEG.writeBlock(code, huffAC, huffDC, file);
            JPEG.writeBlock(code, huffAC, huffDC, file);
        }

        short[] result, resultB;

        try (IO.Bit.reader file = new IO.Bit.reader(tmpFile.getPath())) {
            result = JPEG.readBlock(huffAC, huffDC, file);
            resultB = JPEG.readBlock(huffAC, huffDC, file);
        }

        assertArrayEquals(code, result);
        assertArrayEquals(code, resultB);

    }

    @Test
    void test() {
        String inputFile = "data/images/boat.ppm";

        try {
            File tmpOut = File.createTempFile("JPEG_test", ".ppm");
            tmpOut.deleteOnExit();
            File tmpAux = File.createTempFile("JPEG_test", ".piz.jpeg");
            tmpAux.deleteOnExit();

            try(IO.Byte.reader input = new IO.Byte.reader(inputFile);
                IO.Bit.writer output = new IO.Bit.writer(tmpAux.getPath())) {
                    JPEG alg = new JPEG((short)50);
                    alg.compress(input, output);
            }
            try(IO.Bit.reader input = new IO.Bit.reader(tmpAux.getPath());
                IO.Byte.writer output = new IO.Byte.writer(tmpOut.getPath())) {
                JPEG alg = new JPEG((short)0);
                alg.decompress(input, output);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
