/**
 * @file ./src/main/java/domini/JPEG.java
 * @author ***REMOVED***
 */
package domini;

import java.io.IOException;
import java.util.ArrayList;

import persistencia.IO;
import persistencia.PpmImage;

/**
 * @brief compresión y descompresión de imágenes PPM con JPEG
 */
public final class JPEG implements CompressionAlg{
    /// Calidad de compresión
    private short quality;

    /**
     * Constructora JPEG con calidad, inicializa quality con la calidad dada
     * @param quality calidad de compresión
     */
    public JPEG(final short quality) {
        this.quality = quality;
    }

    /// Magic Byte JPEG
    public final static byte MAGIC_BYTE = (byte) 0x98;

    public byte getMagicByte() {
        return MAGIC_BYTE;
    }

    /**
     * @brief Comprime una imagen PPM bloque a bloque
     *
     * @param input  objeto lector del fichero de entrada
     * @param output objeto escritor del fichero comprimido
     * @throws IOException Si se produce un error de lectura / escritura
     */
    public void compress(final IO.Byte.reader input, final IO.Bit.writer output) throws IOException {

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        PpmImage.Reader img = new PpmImage.Reader(input);

        output.write((int) quality);
        output.write(img.getWidth());
        output.write(img.getHeight());

        final int cols = img.widthBlocks();
        final int rows = img.heightBlocks();

        for (int j = 0; j < rows; ++j) {
            for (int i = 0; i < cols; ++i) {
                final byte[][][] channelBlocks = toYCbCr(img.readBlock());


                for (int chan = 0; chan < 3; ++chan) {
                    final short[] encoded = JPEGBlock.encode(quality, chan != 0, channelBlocks[chan]);

                    if (chan == 0)  writeBlock(encoded, huffAcLum,   huffDcLum,   output);
                    else            writeBlock(encoded, huffAcChrom, huffDcChrom, output);
                }
            }
        }
    }


    /**
     * @brief Descomprime un fichero comprimido en JPEG y lo guarda la imagen resultante en un fichero PPM raw
     *
     * @param input  objeto lector del fichero comprimido
     * @param output objeto escritor del fichero descomprimido
     * @throws IOException Si se produce un error de lectura / escritura
     */
    public void decompress(IO.Bit.reader input, IO.Byte.writer output) throws IOException {

        final Huffman huffAcChrom = new Huffman(true, true);
        final Huffman huffAcLum = new Huffman(true, false);
        final Huffman huffDcChrom = new Huffman(false, true);
        final Huffman huffDcLum = new Huffman(false, false);

        final short quality = (short) input.readInt();
        final int w = input.readInt();
        final int h = input.readInt();

        PpmImage.Writer img = new PpmImage.Writer(output, w, h);

        final int cols = img.widthBlocks();
        final int rows = img.heightBlocks();

        for (int j = 0; j < rows; ++j) {
            for (int i = 0; i < cols; ++i) {
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

    /**
     * @brief Lee un bloque codificado con las tablas Huffman.
     *
     * @param huffAC tabla Huffman de valores AC
     * @param huffDC tabla Huffman de valores DC
     * @param file fichero comprimido del que leer
     * @return bloque codificado sin Huffman
     * @throws IOException error en la lectura
     */
    static short[] readBlock(final Huffman huffAC, final Huffman huffDC, final IO.Bit.reader file)
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

    /**
     * @brief Lee un código Huffman del fichero y lo decodifica
     *
     * @param huff objeto tipo Huffman
     * @param file objeto escritor del fichero comprimido
     * @return devuelve el valor asociado al código Huffman leído
     * @throws IOException error en la lectura
     */
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
    static void writeBlock(final short[] encoded, final Huffman huffAC, final Huffman huffDC,
            final IO.Bit.writer file) throws IOException {
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
    /**
     * @brief Dado un valor, una longitud y un objeto de escritura, escribe el valor representado con la longitud dada en el objeto de escritura.
     *
     * @param value valor que se ecribe
     * @param l longitud de valor que se escribe
     * @param file objeto de escritura del archivo al que se escribe
     * @throws IOException Lanza cualquier excepción generada al escribir
     */
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
    /**
     * @brief Dada una longitud y un objeto de lectura, se leen los bits correspondientes a la longitud dada del objeto de lectura
     *
     * @param length longitud que se lee
     * @param file objeto de lectura del archivo del cual se lee
     * @return short leído
     * @throws IOException Lanza cualquier excepción generada al leer
     */
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

    /**
     * @brief Convierte el bloque dado en espacio de color RGB a YCbCr
     *
     * @param channelBlocks bloque en espacio de color RGB
     * @return bloque en espacio de color YCbCr
     */
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

    /**
     * @brief Convierte el bloque dado en espacio de color YCbCr a RGB
     *
     * @param channelBlocks bloque en espacio de color YCbCr
     * @return bloque en espacio de color RGB
     */
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

    /**
     * @brief Dado un double, devuelve dicho double en formato de byte
     *
     * @param d double a transformar
     * @return el double en fromato de byte
     */
    private static byte doubleToByte(final double d) {
        int n = (int) d;

        if (n < 0)
            n = 0;
        else if (n > 255)
            n = 255;

        return (byte) n;
    }

}
