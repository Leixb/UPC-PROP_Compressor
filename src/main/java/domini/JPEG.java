package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import domini.PpmImage.InvalidFileFormat;

public class JPEG {
    public static void compress(String inputFile, String outputFile, short quality)
            throws Exception, InvalidFileFormat {
        PpmImage img = new PpmImage();
        img.readFile(inputFile);

        img.toYCbCr();

        Huffman huff = new Huffman(true, true);

        try (IO.Bit.writer file = new IO.Bit.writer(outputFile)) {

            //file.write(0x92); // magic byte

            file.write(img.width());
            file.write(img.height());
            for (int channel = 0; channel < 3; ++channel)
                for (int i = 0; i < img.columns(); ++i) {
                    for (int j = 0; j < img.rows(); ++j) {
                        byte[][] block = img.getBlock(channel, i, j);

                        short[] encoded = JPEGBlock.encode(quality, channel != 0, block);

                        writeBlock(encoded, huff,file);

                    }
                }
            // Padding at EOF
            for (int i = 0; i < 8; ++i)
                file.write(0);
        }
    }

    public static void decompress(String inputFile, String outputFile, short quality) throws IOException {
        PpmImage img = new PpmImage();
        Huffman huff = new Huffman(true, true);

        try (IO.Bit.reader file = new IO.Bit.reader(inputFile)) {

            int w = file.readInt();
            int h = file.readInt();

            // file.fill(); // work around

            img.setDimensions(w, h);

            int channel = 0, i = 0, j = 0;

            try {
                for (channel = 0; channel < 3; ++channel)
                    for (i = 0; i < img.columns(); ++i) {
                        for (j = 0; j < img.rows(); ++j) {
                            short[] encoded = decodeBlock(huff, file);

                            byte[][] data = JPEGBlock.decode(quality, channel != 0, encoded);

                            // System.out.printf("%d, %d, %d\n", channel, i, j);
                            img.writeBlock(data, channel, i, j);

                        }
                    }
            } catch (EOFException e) {
                System.out.println("EOF");
                System.out.printf("%d, %d, %d\n", channel, i, j);
                System.out.printf("%d, %d, %d\n", 3, img.columns(), img.rows());
            }
        }
        img.toRGB();
        img.writeFile(outputFile);
    }

    public static short[] decodeBlock(Huffman huff, IO.Bit.reader file) throws IOException {
        ArrayList<Short> block = new ArrayList<>();

        for (int i = 0; i < 64; ++i) {
            Huffman.Node n = huff.decode(file.read());
            while (!n.isLeaf()) {
                n = huff.decode(n, file.read());
            }

            block.add(n.getValue());

            if (n.getValue() == 0x00)
                break;

            int length = n.getValue() & 0xF;

            if (length == 0)
                continue;

            BitSetL bs = new BitSetL(length);

            for (int k = 0; k < length; ++k) {
                bs.set(k, file.read());
            }

            short num =0;
            if (bs.get(0)) {
                num = (short) bs.asInt();
            } else {
                bs.flip();
                num = (short) -bs.asInt();
            }

            block.add(num);
        }

        short[] r = new short[block.size()];
        for (int i = 0; i < block.size(); ++i) {
            r[i] = block.get(i);
        }
        return r;
    }

    public static void writeBlock(short[] encoded, Huffman huff, IO.Bit.writer file) throws IOException {
        for (int k = 0; k < encoded.length; ++k) {
            file.write(huff.encode(encoded[k]));
            int l = encoded[k]&0x0F;
            if (l != 0) {
                ++k;
                BitSetL bs;
                if (encoded[k] < 0) {
                    bs = new BitSetL(-encoded[k], l);
                    bs.flip();
                } else {
                    bs = new BitSetL(encoded[k], l);
                }
                file.write(bs);
            }
        }
    }
}