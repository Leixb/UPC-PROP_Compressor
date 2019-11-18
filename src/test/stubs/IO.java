package domini;

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

public class IO {
    public static class Char {
        public static class reader extends BufferedReader {
            public reader(final String filename) throws FileNotFoundException {
                super(new FileReader(filename));
                System.out.printf("Llamda IO.Char.reader(String filename=%s)\n", filename);
            }
        }

        public static class writer extends BufferedWriter {
            public writer(final String filename) throws IOException {
                super(new FileWriter(filename));
                System.out.printf("Llamda IO.Char.writer(String filename=%s)\n", filename);
            }
        }
    }

    public static class Byte {
        public static class reader extends BufferedInputStream {
            public reader(final String filename) throws FileNotFoundException {
                super(new FileInputStream(filename));
                System.out.printf("Llamda IO.Byte.reader(String filename=%s)\n", filename);
            }
        }

        public static class writer extends BufferedOutputStream {
            public writer(final String filename) throws IOException {
                super(new FileOutputStream(filename));
                System.out.printf("Llamda IO.Byte.writer(String filename=%s)\n", filename);
            }
        }
    }

    /** Lector / Escritor bit a bit con buffer */
    public static class Bit {
        public static class writer implements AutoCloseable {

            public writer(final String filename) {
                System.out.printf("Llamda IO.Bit.writer(String filename=%s)\n", filename);
            }

            public void write(final boolean bit) throws IOException {
                System.out.printf("Llamda IO.Bit.writer.write(boolean bit=%b)\n", bit);
            }

            public void write(final BitSetL bs) throws IOException {
                System.out.printf("Llamda IO.Bit.writer.write(BitSetL bs)\n");
            }

            public void write(final byte b) throws IOException {
                System.out.printf("Llamda IO.Bit.writer.write(byte b=%x)\n", b);
            }

            public void write(final char c) throws IOException {
                System.out.printf("Llamda IO.Bit.writer.write(char c=%x)\n", c);
            }

            public void write(final int num) throws IOException {
                System.out.printf("Llamda IO.Bit.writer.write(int n=%x)\n", num);
            }

            public void flush() throws IOException {
                System.out.printf("Llamda IO.Bit.writer.flush()\n");
            }

            public void close() throws IOException {
                System.out.printf("Llamda IO.Bit.writer.close()\n");
            }
        }

        public static class reader implements AutoCloseable {

            public reader(final String filename) {
                System.out.printf("Llamda IO.Bit.reader(String filename=%s)\n", filename);
            }

            public boolean read() throws IOException {
                System.out.printf("Llamda IO.Bit.reader.read() -> true\n");
                return true;
            }

            public int readByte() throws IOException {
                System.out.printf("Llamda IO.Bit.reader.readByte() -> 0x07\n");
                return 0x07;
            }

            public int readChar() throws IOException {
                System.out.printf("Llamda IO.Bit.reader.readChar() -> 'A'\n");
                return 0x41;
            }

            public int readInt() throws IOException {
                System.out.printf("Llamda IO.Bit.reader.readInt() -> 0x02\n");
                return 0x02;
            }

            public BitSetL readBitSet(final int length) throws IOException {
                System.out.printf("Llamda IO.Bit.reader.readBitSet(int length=%d) -> BitSetL(length)\n", length);
                return new BitSetL(length);
            }

            public void close() throws IOException {
                System.out.printf("Llamda IO.Bit.reader.close()\n");
            }
        }
    }
}
