package persistencia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.CheckCompDecomp;

import java.io.*;

class PpmImageTest {

    @Test
    void moveFile() {
        final String inputFile = "data/images/boat.ppm";

        try {
            File tmpOut = File.createTempFile("PpmTest", ".ppm");
            tmpOut.deleteOnExit();

            try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Byte.writer output = new IO.Byte.writer(tmpOut.getPath())) {

                PpmImage.Reader imgIn = new PpmImage.Reader(input);

                final int cols = imgIn.widthBlocks();
                final int rows = imgIn.heightBlocks();
                PpmImage.Writer imgOut = new PpmImage.Writer(output, imgIn.getWidth(), imgIn.getHeight());

                for (int j = 0; j < rows; ++j) {
                    for (int i = 0; i < cols; ++i) {
                        imgOut.writeBlock(imgIn.readBlock());
                    }
                }
            }

            CheckCompDecomp.assertFileEquals(inputFile, tmpOut.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
