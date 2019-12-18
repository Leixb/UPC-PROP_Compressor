/**
 * @file ./src/main/java/domini/JPEG.java
 * @author Aleix Boné
 */
package domini;

import java.io.IOException;
import java.util.ArrayList;

import domini.Huffman.HuffmanLookupException;
import persistencia.IO;

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
    public static void compress(final IO.Byte.reader input, final IO.Bit.writer output, final short quality)
            throws IOException, HuffmanLookupException {

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        try (PpmImage.Reader img = new PpmImage.Reader(input)) {


            output.write(MAGIC_BYTE); // magic byte
            output.write((int) quality);
            output.write(img.getWidth());
            output.write(img.getHeight());

            final int cols = img.widthBlocks();
            final int rows = img.heightBlocks();

            for (int i = 0; i < cols; ++i) {
                for (int j = 0; j < rows; ++j) {
                    final byte[][][] channelBlocks = toYCbCr(img.readBlock());

                    for (int chan = 0; chan < 3; ++chan) {
                        final short[] encoded = JPEGBlock.encode(quality, chan != 0, channelBlocks[chan]);
                        if (chan == 0)  writeBlock(encoded, huffAcLum,   huffDcLum,   output);
                        else            writeBlock(encoded, huffAcChrom, huffDcChrom, output);
                    }
                }
            }

        }

    }

    private static byte[][][] toYCbCr(byte[][][] channelBlocks) {
        byte Y, Cb, Cr;

        byte[][][] reorderedBlocks = new byte[3][8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                final double R = Byte.toUnsignedInt(channelBlocks[i][j][0]);
                final double G = Byte.toUnsignedInt(channelBlocks[i][j][1]);
                final double B = Byte.toUnsignedInt(channelBlocks[i][j][2]);

                Y = doubleToByte(0 + 0.299 * R + 0.587 * G + 0.114 * B);
                Cb = doubleToByte(128 - 0.1687 * R - 0.3313 * G + 0.5 * B);
                Cr = doubleToByte(128 + 0.5 * R - 0.4187 * G - 0.0813 * B);

                reorderedBlocks[0][i][j] = Y;
                reorderedBlocks[1][i][j] = Cb;
                reorderedBlocks[2][i][j] = Cr;
            }
        }

        return reorderedBlocks;
    }

    private static byte[][][] toRGB(byte[][][] channelBlocks) {
        byte R, G, B;

        byte[][][] reorderedBlocks = new byte[8][8][3];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                final double Y = Byte.toUnsignedInt(channelBlocks [0][i][j]);
                final double Cb = Byte.toUnsignedInt(channelBlocks[1][i][j]);
                final double Cr = Byte.toUnsignedInt(channelBlocks[2][i][j]);

                R = doubleToByte(Y + 1.402 * (Cr - 128));
                G = doubleToByte(Y - 0.34414 * (Cb - 128) - 0.71414 * (Cr - 128));
                B = doubleToByte(Y + 1.772 * (Cb - 128));

                reorderedBlocks[i][j][0] = R;
                reorderedBlocks[i][j][1] = G;
                reorderedBlocks[i][j][2] = B;
            }
        }

        return reorderedBlocks;
    }

    private static byte doubleToByte(final double d) {
        int n = (int) d;

        if (n < 0)
            n = 0;
        else if (n > 255)
            n = 255;

        return (byte) n;
    }

    /**
     * @brief Descomprime un fichero comprimido en JPEG y lo guarda la imagen resultante en un fichero PPM raw
     *
     * @param inputFile  nombre del fichero de entrada (comprimido)
     * @param outputFile nombre del fichero de salida (imagen PPM)
     * @throws IOException Si se produce un error de lectura / escritura
     */
    public static void decompress(IO.Bit.reader input, IO.Byte.writer output) throws IOException {
        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        input.readByte(); // Discard magic byte
        final short quality = (short) input.readInt();
        final int w = input.readInt();
        final int h = input.readInt();

        try (PpmImage.Writer img = new PpmImage.Writer(output, w, h)) {

        final int cols = img.widthBlocks();
        final int rows = img.heightBlocks();

            for (int i = 0; i < cols; ++i) {
                for (int j = 0; j < rows; ++j) {
                    final byte[][][] channelBlocks = new byte[3][8][8];

                    for (int chan = 0; chan < 3; ++chan) {
                        short[] encoded;
                        
                        if (chan == 0)  encoded = readBlock(huffAcLum,   huffDcLum,   input);
                        else            encoded = readBlock(huffAcChrom, huffDcChrom, input);

                        channelBlocks[chan] = JPEGBlock.decode(quality, chan != 0, encoded);

                    }

                    img.writeBlock(toRGB(channelBlocks));
                }
            }

        }


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
