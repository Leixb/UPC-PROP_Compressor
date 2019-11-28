/**
 * @file ./src/main/java/domini/JPEG.java
 * @author ***REMOVED***
 */
package domini;

import java.io.IOException;
import java.util.ArrayList;

import domini.Huffman.HuffmanLookupException;
import domini.PpmImage.InvalidFileFormat;

/**
 * @brief compresión y descompresión de imágenes PPM con JPEG
 */
public final class JPEG {

    private JPEG() { }

    /// Magic Byte JPEG
    public final static byte MAGIC_BYTE = (byte) 0x98;

    /**
     * @brief Comprime una imagen PPM bloque a bloque
     *
     * @param inputFile  nombre del fichero de entrada (imagen ppm)
     * @param outputFile nombre del fichero de salida (comprimido)
     * @param quality    calidad de compreso (1-100) donde 100 es la mejor calidad
     * @throws InvalidFileFormat      Si el fichero de entrada no es un PPM raw valido
     * @throws IOException            Si se produce un error de lectura / escritura
     * @throws HuffmanLookupException Si no se puede codificar algún valor (Solo sucede si la tabla Huffman no es correcta)
     */
    public static void compress(final String inputFile, final String outputFile, final short quality)
            throws InvalidFileFormat, IOException, HuffmanLookupException {
        final PpmImage img = new PpmImage();
        img.readFile(inputFile);

        img.toYCbCr();

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        Huffman huffAc, huffDc;

        try (final IO.Bit.writer file = new IO.Bit.writer(outputFile)) {

            file.write(MAGIC_BYTE); // magic byte
            file.write((int) quality);
            file.write(img.width());
            file.write(img.height());

            final int cols = img.columns();
            final int rows = img.rows();

            for (int channel = 0; channel < 3; ++channel) {
                if (channel == 0) {
                    // Luminance
                    huffAc = huffAcLum;
                    huffDc = huffDcLum;
                } else {
                    // Chrominance
                    huffAc = huffAcChrom;
                    huffDc = huffDcChrom;
                }

                for (int i = 0; i < cols; ++i) {
                    for (int j = 0; j < rows; ++j) {
                        final byte[][] block = img.readBlock(channel, i, j);

                        final short[] encoded = JPEGBlock.encode(quality, channel != 0, block);

                        writeBlock(encoded, huffAc, huffDc, file);

                    }
                }
            }
        }
    }

    /**
     * @brief Descomprime un fichero comprimido en JPEG y lo guarda la imagen resultante en un fichero PPM raw
     *
     * @param inputFile  nombre del fichero de entrada (comprimido)
     * @param outputFile nombre del fichero de salida (imagen PPM)
     * @throws IOException Si se produce un error de lectura / escritura
     */
    public static void decompress(final String inputFile, final String outputFile) throws IOException {
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

            for (channel = 0; channel < 3; ++channel) {
                if (channel == 0) {
                    huffAc = huffAcLum;
                    huffDc = huffDcLum;
                } else {
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
        }
        img.toRGB();
        img.writeFile(outputFile);
    }

    /**
     * @brief Lee un bloque codificado con las tablas Huffman.
     * @param huffAC tabla Huffman de valores AC
     * @param huffDC tabla Huffman de valores DC
     * @param file fichero comprimido del que leer
     * @return bloque codificado sin Huffman
     * @throws IOException error en la lectura
     */
    public static short[] readBlock(final Huffman huffAC, final Huffman huffDC, final IO.Bit.reader file)
            throws IOException {
        final ArrayList<Short> block = new ArrayList<>();

        block.add(readHuffman(huffDC, file));
        if (block.get(0) != 0)
            block.add(read(block.get(0), file));

        for (int i = 0; i < 64; ++i) {
            final short decodedValue = readHuffman(huffAC, file);

            block.add(decodedValue);

            if (decodedValue == 0x00)
                break; // EOB

            final int length = decodedValue & 0xF;
            if (length == 0)
                continue; // ZRL

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

    /**
     * @brief Escribe un bloque codificado con las tablas Huffman.
     * @param encoded codificado sin Huffman
     * @param huffAC tabla Huffman de valores AC
     * @param huffDC tabla Huffman de valores DC
     * @param file fichero comprimido al que escribir
     * @throws IOException error en la escritura
     */
    public static void writeBlock(final short[] encoded, final Huffman huffAC, final Huffman huffDC,
            final IO.Bit.writer file) throws IOException, HuffmanLookupException {
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

    // Si era negatiu llegeix el valor negat en binari.
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
