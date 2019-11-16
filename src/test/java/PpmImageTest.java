import domini.JPEGBlock;
import domini.PpmImage;
//import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PpmImageTest {

    void readWriteImage() {
        final short quality = 75;
        final String imageFile = "images/gradients32.ppm";
        final String outputFile = "gradients32.ppm";

        PpmImage img = new PpmImage();
        try {
            img.readFile(imageFile);

            img.toYCbCr();

            final int original_size = img.width()*img.height()*3;
            int compressed_size = 0;

            System.out.printf("W %d H %d - C %d R %d \n", img.width(), img.height(),
                    img.columns(), img.rows());

            for (int channel = 0; channel < 3; ++channel)
                for (int i = 0; i < img.columns(); ++i) {
                    for (int j = 0; j < img.rows(); ++j) {
                        byte[][] block = img.getBlock(channel, i, j);

                        short[] encoded = JPEGBlock.encode(quality, channel!=0 , block);
                        byte[][] decoded = JPEGBlock.decode(quality, channel!=0, encoded);

                        compressed_size += encoded.length;

                        img.writeBlock(decoded, channel, i, j);
                    }
                }


            img.toRGB();

            System.out.printf("%d -> %d (%.2f%%)\n", original_size, compressed_size, (100*compressed_size/(double)original_size));

            new File("generated/").mkdirs();
            img.writeFile("generated/" + outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
