/**
 * @file ./src/main/java/persistencia/PpmImage.java
 * @author ***REMOVED***
*/
package persistencia;

import java.io.*;

/**
 * @brief Imagen PPM
 */
public class PpmImage {

    /** Lector de PpmImage */
    static public class Reader {
        private IO.Byte.reader file;
        private int width, height;

        private int buffPos; // Bloque actual en el buffer
        private byte[][][] buffer;

        public Reader(IO.Byte.reader file) throws IOException {
            this.file = file;

            final byte[] magic = new byte[2];
            if (2 != file.read(magic))
                throw new EOFException();

            if (magic[0] != 'P' || magic[1] != '6')
                throw new FileFormatException("Not PPM image (P6)");

            this.width = readInt();
            this.height = readInt();

            final int maxVal = readInt();

            if (maxVal >= 256)
                throw new FileFormatException("bit depth > 255");

            buffer = new byte[width][8][3];
            buffPos = width/8+1;
        }

        public byte[][][] readBlock() throws IOException {
            byte[][][] block = new byte[8][8][3];

            if (buffPos*8 >= width) fillBuffer();

            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    int x = Math.min(buffPos*8 + i, width - 1); // padding
                    block[i][j] = buffer[x][j];
                }
            }

            ++buffPos; // Advance bufferPos

            return block;
        }

        private void fillBuffer() throws IOException {
            buffPos = 0;
            for (int j = 0; j < 8; ++j) {
                for (int i = 0; i < width; ++i) {
                    if(file.read(buffer[i][j]) != 3) {
                        if (j == 0) throw new EOFException();
                        // EOF, fill the rest of buffer with previous rows and end
                        for (int k = j; k < 8; ++k) {
                            buffer[i][k] = buffer[i][j-1];
                        }
                        return;
                    }
                }
            }
        }

        public int getWidth() {
            return width;
        }
        public int getHeight() {
            return height;
        }

        public int widthBlocks() {
            return width/8 + ((width%8 == 0)? 0 : 1);
        }
        public int heightBlocks() {
            return height/8 + ((height%8 == 0)? 0 : 1);
        }

        private int readInt() throws IOException {
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
    }

    /** Escritor de PpmImage */
    static public class Writer {
        private IO.Byte.writer file;

        private int width, height;

        private static final String HEADER_FORMAT = "P6\n%d %d\n255\n";

        private byte[][][] buffer;
        private int buffPos;
        private int writtenBuffers;

        boolean EOF = false;

        public Writer(IO.Byte.writer file, int width, int height) throws IOException {
            this.file = file;
            this.width = width;
            this.height = height;

            buffer = new byte[width][8][3];

            writtenBuffers = 0;
            buffPos = 0;

            writeHeader();
        }

        private void writeHeader() throws IOException {
            file.write(String.format(HEADER_FORMAT, this.width, this.height).getBytes());
        }

        public void writeBlock(byte[][][] block) throws IOException {

            if (EOF) throw new EOFException();

            for (int i = 0; i < 8; ++i) {
                if (buffPos*8 + i >= width) break;
                for (int j = 0; j < 8; ++j) {
                    buffer[buffPos*8 + i][j] = block[i][j];
                }
            }

            ++buffPos;
            if (buffPos*8 >= width) writeBuffer();
        }

        private void writeBuffer() throws IOException {
            for (int j = 0; j < 8; ++j) {
                if (writtenBuffers*8 + j >= height) {
                    break;
                }
                for (int i = 0; i < width; ++i) {
                    file.write(buffer[i][j]);
                }
            }

            buffPos = 0;
            writtenBuffers++;
        }

        public int widthBlocks() {
            return width/8 + ((width%8 == 0)? 0 : 1);
        }
        public int heightBlocks() {
            return height/8 + ((height%8 == 0)? 0 : 1);
        }
    }

    /** Excepción del formato de archivo  */
    public static class FileFormatException extends IOException {
        private static final long serialVersionUID = 483426725025690872L;

        public FileFormatException(String s) {
            super(String.format("Invalid file format: %s", s));
        }
    }
}
