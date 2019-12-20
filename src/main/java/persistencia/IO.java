/**
 * @file ./src/main/java/persistencia/IO.java
 * @author ***REMOVED***
 */
package persistencia;

import domini.BitSetL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * @brief classes IO para lectura y escritura de Char, Byte, y Bit con buffer.
 */
public class IO {
    /** Lector / Escritor char a char con buffer */
    public static class Char {
        public static class reader extends BufferedReader {
            public reader(final String filename) throws FileNotFoundException {
                super(new FileReader(filename));
            }
        }

        public static class writer extends BufferedWriter {
            public writer(final String filename) throws IOException {
                super(new FileWriter(filename));
            }
        }
    }

    /** Lector / Escritor byte a byte con buffer */
    public static class Byte {
        public static class reader extends BufferedInputStream {
            private String filename;
            public reader(final String filename) throws FileNotFoundException {
                super(new FileInputStream(filename));
                this.filename = filename;
            }
            public String getFilename() {
                return filename;
            }
        }

        public static class writer extends BufferedOutputStream {
            public writer(final String filename) throws IOException {
                super(new FileOutputStream(filename));
            }
        }
    }

    /** Lector / Escritor bit a bit con buffer */
    public static class Bit {
        public static class writer implements AutoCloseable {
            private final BufferedOutputStream out;
            private int buffer;
            private int n;

            public writer(final String filename) throws FileNotFoundException {
                out = new BufferedOutputStream(new FileOutputStream(filename));
                buffer = 0;
                n = 0;
            }

            public void write(final boolean bit) throws IOException {
                buffer <<= 1;
                if (bit)
                    buffer |= 1;

                ++n;
                if (n >= 8)
                    clear();
            }

            public void write(final BitSetL bs) throws IOException {
                for (int i = 0; i < bs.length(); ++i) {
                    write(bs.get(i));
                }
            }

            public void write(final byte b) throws IOException {
                writeMask(b, 0x80);
            }

            public void write(final char c) throws IOException {
                writeMask(c, 0x8000);
            }

            public void write(final int num) throws IOException {
                writeMask(num, 0x80000000);
            }

            private void writeMask(final int num, int mask) throws IOException {
                while (mask != 0) {
                    write((num & mask) != 0);
                    mask >>>= 1;
                }
            }

            private void clear() throws IOException {
                if (n == 0)
                    return;
                buffer <<= 8 - n;
                out.write(buffer);
                n = 0;
                buffer = 0;
            }

            private void flush() throws IOException {
                clear();
                out.flush();
            }

            public void close() throws IOException {
                flush();
                out.close();
            }
        }

        public static class reader implements AutoCloseable {
            private final BufferedInputStream in;
            private int buffer;
            private int n;

            public reader(final String filename) throws IOException {
                in = new BufferedInputStream(new FileInputStream(filename));
                buffer = 0;
                n = 0;
            }

            private void fill() throws IOException {
                buffer = in.read();
                n = 8;
                if (buffer == -1)
                    throw new EOFException();
            }

            public boolean read() throws IOException {
                if (n == 0)
                    fill();
                n--;
                final boolean bit = ((buffer >>> n) & 1) == 1;
                return bit;
            }

            private int readMask(int mask) throws IOException {
                int num = 0;
                while (mask != 0) {
                    if (read())
                        num |= mask;
                    mask >>>= 1;
                }
                return num;
            }

            public int readByte() throws IOException {
                return readMask(0x80);
            }

            public int readChar() throws IOException {
                return readMask(0x8000);
            }

            public int readInt() throws IOException {
                return readMask(0x80000000);
            }

            public BitSetL readBitSet(final int length) throws IOException {
                BitSetL bs = new BitSetL(length);
                for (int k = 0; k < length; ++k) bs.set(k, read());
                return bs;
            }

            public void close() throws IOException {
                in.close();
            }
        }
    }
}
