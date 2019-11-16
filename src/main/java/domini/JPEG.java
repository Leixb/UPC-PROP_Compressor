/**
 * @author Aleix Boné
 */
package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import domini.PpmImage.InvalidFileFormat;

public final class JPEG {

    private JPEG() {}

    public final static byte MAGIC_BYTE = (byte) 0x98;

    public static void compress(final String inputFile, final String outputFile, final short quality)
            throws Exception, InvalidFileFormat {
        final PpmImage img = new PpmImage();
        img.readFile(inputFile);

        img.toYCbCr();

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        Huffman huffAc, huffDc;

        try (IO.Bit.writer file = new IO.Bit.writer(outputFile)) {

            file.write(MAGIC_BYTE); // magic byte
            file.write((int)quality);
            file.write(img.width());
            file.write(img.height());

            final int cols = img.columns();
            final int rows = img.rows();

            for (int channel = 0; channel < 3; ++channel) {

                System.out.println(channel);

                if (channel == 0) {
                    huffAc = huffAcLum;
                    huffDc = huffDcLum;
                } else {
                    huffAc = huffAcChrom;
                    huffDc = huffDcChrom;
                }

                //int DC_prev = 0;

                for (int i = 0; i < cols; ++i) {
                    for (int j = 0; j < rows; ++j) {
                        final byte[][] block = img.getBlock(channel, i, j);

                        final short[] encoded = JPEGBlock.encode(quality, channel != 0, block);

                        writeBlock(encoded, huffAc, huffDc, file);

                    }
                }
            }
            // Padding at EOF
            for (int i = 0; i < 8; ++i)
                file.write(0);
        }
    }

    public static void decompress(final String inputFile, final String outputFile)
            throws IOException {
        final PpmImage img = new PpmImage();

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        Huffman huffAc, huffDc;

        try (IO.Bit.reader file = new IO.Bit.reader(inputFile)) {

            file.readByte(); // Discard magic byte
            final short quality = (short) file.readInt();
            int w = file.readInt();
            int h = file.readInt();

            img.setDimensions(w, h);

            int channel = 0, i = 0, j = 0;

            try {
                for (channel = 0; channel < 3; ++channel) {
                    if (channel == 0) {
                        huffAc = huffAcLum;
                        huffDc = huffDcLum;
                    }
                    else {
                        huffAc = huffAcChrom;
                        huffDc = huffDcChrom;
                    }
                    for (i = 0; i < img.columns(); ++i) {
                        for (j = 0; j < img.rows(); ++j) {
                            final short[] encoded = readBlock(huffAc, huffDc, file);

                            final byte[][] data = JPEGBlock.decode(quality, channel != 0, encoded);

                            img.writeBlock(data, channel, i, j);

                        }
                    }
                }
            } catch (final EOFException e) {
                System.out.println("EOF");
                System.out.printf("%d, %d, %d\n", channel, i, j);
                System.out.printf("%d, %d, %d\n", 3, img.columns(), img.rows());
            }
        }
        img.toRGB();
        img.writeFile(outputFile);
    }

    public static short[] readBlock(final Huffman huffAC, final Huffman huffDC, final IO.Bit.reader file) throws IOException {
        final ArrayList<Short> block = new ArrayList<>();

        block.add(readHuffman(huffDC, file));
        if (block.get(0) != 0) block.add(read(block.get(0), file));

        //for (int i = 0; i < 63; ++i) {
        for (int i = 0; i < 100; ++i) {
            if (i >= 64) {
                System.out.println("___WOT");
                break;
            }
            final short decodedValue = readHuffman(huffAC, file);

            block.add(decodedValue);

            if (decodedValue == 0x00) break; // EOB

            final int length = decodedValue & 0xF;
            if (length == 0) continue; // ZRL

            block.add(read(length, file));
        }

        final short[] r = new short[block.size()];
        for (int i = 0; i < block.size(); ++i) {
            r[i] = block.get(i);
        }
        return r;
    }

    private static short readHuffman(Huffman huff, IO.Bit.reader file) throws IOException {
        Huffman.Node n = huff.decode(file.read());
        while (!n.isLeaf()) {
            n = huff.decode(n, file.read());
        }
        return n.getValue();
    }

    public static void writeBlock(final short[] encoded, final Huffman huffAC, final Huffman huffDC, final IO.Bit.writer file) throws IOException {
        // write DC coefficient
        file.write(huffDC.encode(encoded[0]));

        int k;
        if (encoded[0] != 0) {
            write(encoded[1], encoded[0], file);
            k = 2;
        } else {
            k = 1;
        }

        // write AC coefficients
        for (; k < encoded.length; ++k) {
            // escriu codi huffman
            file.write(huffAC.encode(encoded[k]));

            final int l = encoded[k]&0x0F;
            if (l == 0) continue;

            ++k;

            write(encoded[k], l, file);

        }
    }

    // Si es negatiu escriu el valor negat en binari.
    private static void write(int value, int l, IO.Bit.writer file) throws IOException {
        BitSetL bs;
        if (value < 0) {
            bs = new BitSetL(-value, l);
            bs.flip();
        } else {
            bs = new BitSetL(value, l);
        }
        file.write(bs);
    }
    private static short read(int length, IO.Bit.reader file) throws IOException {
        BitSetL bs = file.readBitSet(length);

        short num =0;
        if (bs.get(0)) {
            num = (short) bs.asInt();
        } else {
            bs.flip();
            num = (short) -bs.asInt();
        }
        return num;
    }
}
