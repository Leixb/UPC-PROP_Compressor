
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.PpmImage;
import persistencia.IO;

import java.io.*;

class PpmImageTest {

    @Test
    void moveFile() {
        //final String file = "gradient_35";
        final String file = "boat";
        final String inputFile = String.format("images/%s.ppm", file);

        try {
            // File tmpOut = File.createTempFile("PpmTest", ".ppm");
            // tmpOut.deleteOnExit();
            File tmpOut = new File( String.format("generated/%s.ppm", file));

            try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Byte.writer output = new IO.Byte.writer(tmpOut.getPath())) {
                try(PpmImage.Reader imgIn = new PpmImage.Reader(input)) {

                    final int cols = imgIn.widthBlocks();
                    final int rows = imgIn.heightBlocks();

                    try(PpmImage.Writer imgOut = new PpmImage.Writer(output, imgIn.getWidth(), imgIn.getHeight())) {
                        for (int j = 0; j < rows; ++j) {
                            System.out.println(j);
                            for (int i = 0; i < cols; ++i) {
                                imgOut.writeBlock(imgIn.readBlock());
                            }
                            //assertArrayEquals(imgOut.buffer, imgIn.buffer);

                            // for (int i = 0; i < imgOut.buffer.length; ++i) {
                            //     for (int k = 0; k < imgOut.buffer[i].length; ++k) {
                            //         System.out.printf("%02x ", imgOut.buffer[i][k][0]);
                            //     }
                            //     System.out.println();
                            // }
                        }
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
