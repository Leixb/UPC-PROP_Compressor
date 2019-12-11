/**
 * @file ./src/main/java/domini/PpmImage.java
 * @author Aleix Boné
*/
package domini;

import persistencia.IO;

import java.io.*;

/**
 * @brief Imagen PPM
 */
public class PpmImage {
    private byte[][][] pixels; // width * height * channel
    private int width, height;

    /**
     * @brief Inicializa la imagen vacía con los valores de anchura y altura
     * @param w anchura (width)
     * @param h altura (height)
     */
    public void setDimensions(final int w, final int h) {
        pixels = new byte[w][h][3];
        width = w;
        height = h;
    }

    /**
     * @brief Lee una fichero imagen y lo guarda en la memoria
     * @param filename nombre del fichero a leer
     * @throws IOException error en la lectura
     * @throws InvalidFileFormat Formato de imagen invalido (No es PPM raw)
     */
    public void readFile(final String filename) throws IOException, InvalidFileFormat {
        try (IO.Byte.reader file = new IO.Byte.reader(filename)) {

            final byte[] magic = new byte[2];
            if (2 != file.read(magic))
                throw new EOFException();

            if (magic[0] != 'P' || magic[1] != '6')
                throw new InvalidFileFormat();

            this.width = readInt(file);
            this.height = readInt(file);
            final int maxVal = readInt(file);

            if (maxVal >= 256)
                throw new InvalidFileFormat();

            this.pixels = new byte[this.width][this.height][3];

            for (int i = 0; i < this.width; ++i)
                for (int j = 0; j < this.height; ++j)
                    if (file.read(this.pixels[i][j]) != 3)
                        throw new EOFException();
        }
    }

    /**
     * @brief Escribe la imagen en un fichero
     * @param filename nombre del fichero a escribir
     * @throws IOException error al escribir el fichero
     */
    public void writeFile(final String filename) throws IOException {
        try (IO.Byte.writer fout = new IO.Byte.writer(filename)) {

            final byte[] header = String.format("P6\n%d %d\n255\n", this.width, this.height).getBytes();
            fout.write(header);

            for (int i = 0; i < this.width; ++i)
                for (int j = 0; j < this.height; ++j)
                    fout.write(pixels[i][j]);
        }
    }

    /** El formato del fichero no es un PPM valido */
    public static class InvalidFileFormat extends Exception {
        private static final long serialVersionUID = -7627960741299880112L;

        public InvalidFileFormat() {
            super();
        }
        public InvalidFileFormat(String s) {
            super(s);
        }
    }

    /**
     * @brief Lee el siguiente entero codificado en ASCII que encuentra en el fichero
     * @param file fichero donde buscar el entero
     * @return entero leído
     * @throws IOException error en la lectura del fichero
     */
    private int readInt(final IO.Byte.reader file) throws IOException {

        // Read till we find ascii integers
        char c;
        do {
            c = (char) file.read();
            if (c == '#') { // Comment, discard till newline
                while (c != '\n')
                    c = (char) file.read();
            }
        } while (c < '0' || c > '9');

        // Read till not ascii integer
        int n = 0;
        do {
            n = n * 10 + c - '0';
            c = (char) file.read();
        } while (c >= '0' && c <= '9');

        return n;
    }

    private byte doubleToByte(final double d) {
        int n = (int) d;

        if (n < 0)
            n = 0;
        else if (n > 255)
            n = 255;

        return (byte) n;
    }

    /** @brief Convierte la imagen de espacio de color YCbCr a espacio RGB */
    public void toRGB() {
        byte R, G, B;
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                final double Y = Byte.toUnsignedInt(this.pixels[i][j][0]);
                final double Cb = Byte.toUnsignedInt(this.pixels[i][j][1]);
                final double Cr = Byte.toUnsignedInt(this.pixels[i][j][2]);

                R = doubleToByte(Y + 1.402 * (Cr - 128));
                G = doubleToByte(Y - 0.34414 * (Cb - 128) - 0.71414 * (Cr - 128));
                B = doubleToByte(Y + 1.772 * (Cb - 128));

                this.pixels[i][j][0] = R;
                this.pixels[i][j][1] = G;
                this.pixels[i][j][2] = B;
            }
        }

    }

    /** @brief Convierte la imagen de espacio de color RGB a espacio YCbCr */
    public void toYCbCr() {
        byte Y, Cb, Cr;
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                final double R = Byte.toUnsignedInt(this.pixels[i][j][0]);
                final double G = Byte.toUnsignedInt(this.pixels[i][j][1]);
                final double B = Byte.toUnsignedInt(this.pixels[i][j][2]);

                Y = doubleToByte(0 + 0.299 * R + 0.587 * G + 0.114 * B);
                Cb = doubleToByte(128 - 0.1687 * R - 0.3313 * G + 0.5 * B);
                Cr = doubleToByte(128 + 0.5 * R - 0.4187 * G - 0.0813 * B);

                this.pixels[i][j][0] = Y;
                this.pixels[i][j][1] = Cb;
                this.pixels[i][j][2] = Cr;
            }
        }
    }

    /**
     * @brief Devuelve un bloque de la imagen de 8x8
     * @param channel canal de color del bloque
     * @param x posición del bloque en coordenada x
     * @param y posición del bloque en coordenada y
     * @return bloque 8x8
     */
    public byte[][] readBlock(final int channel, final int x, final int y) {
        final byte[][] block = new byte[8][8];

        for (int i = 0; i < 8; ++i) {
            final int posX = Math.min(8 * x + i, width - 1);
            for (int j = 0; j < 8; ++j) {
                final int posY = Math.min(8 * y + j, height - 1);
                block[i][j] = pixels[posX][posY][channel];
            }
        }

        return block;
    }

    /**
     * @brief Escribe un bloque de la imagen de 8x8
     * @param block bloque 8x8 a escribir
     * @param channel canal de color del bloque
     * @param x posición del bloque en coordenada x
     * @param y posición del bloque en coordenada y
     */
    public void writeBlock(final byte[][] block, final int channel, final int x, final int y) {
        for (int i = 0; i < 8; ++i) {
            final int posX = 8 * x + i;
            if (posX >= width)
                break;
            for (int j = 0; j < 8; ++j) {
                final int posY = 8 * y + j;
                if (posY >= height) break;
                pixels[posX][posY][channel] = block[i][j];
            }
        }
    }

    /**
     * @brief Devuelve la anchura de la imagen en pixels
     * @return anchura de la imagen en pixels
     */
    public int width() {
        return width;
    }

    /**
     * @brief Devuelve la altura de la imagen en pixels
     * @return altura de la imagen en pixels
     */
    public int height() {
        return height;
    }

    /**
     * @brief Devuelve la anchura de la imagen en bloques (8x8).
     *
     * Es decir, el número de bloques 8x8 que caben horizontalmente en la imagen.
     * @return anchura de la imagen en bloques (8x8)
     */
    public int columns() {
        return width/8 + ((width%8 == 0)? 0 : 1);
    }

    /**
     * @brief Devuelve la altura de la imagen en bloques (8x8).
     *
     * Es decir, el número de bloques 8x8 que caben verticalmente en la imagen.
     * @return altura de la imagen en bloques (8x8)
     */
    public int rows() {
        return height/8 + ((height%8 == 0)? 0 : 1);
    }
}
