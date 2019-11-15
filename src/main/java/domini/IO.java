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
            public reader(String filename) throws FileNotFoundException {
                super(new FileReader(filename));
            }
        }

        public static class writer extends BufferedWriter {
            public writer(String filename) throws IOException {
                super(new FileWriter(filename));
            }
        }
    }

    public static class Byte {
        public static class reader extends BufferedInputStream {
            public reader(String filename) throws FileNotFoundException {
                super(new FileInputStream(filename));
            }
        }

        public static class writer extends BufferedOutputStream {
            public writer(String filename) throws IOException {
                super(new FileOutputStream(filename));
            }
        }
    }

    public static class Bit {
        public static class writer implements AutoCloseable {
            private BufferedOutputStream out;
            private int buffer;
            private int n;

            public writer(String filename) throws FileNotFoundException {
                out = new BufferedOutputStream(new FileOutputStream(filename));
                buffer = 0;
                n = 0;
            }

            public void write(boolean bit) throws IOException {
                buffer <<= 1;
                if (bit) buffer |= 1;

                ++n;
                if (n >= 8) clear();
            }

            public void write(BitSetL bs) throws IOException {
                for (int i = 0; i < bs.length(); ++i) {
                    write(bs.get(i));
                }
            }

            public void write(byte b) throws IOException {
                writeMask(b, 0x80);
            }
            public void write(char c) throws IOException { 
                writeMask(c, 0x8000);
            }
            public void write(int num)  throws IOException {
                writeMask(num, 0x80000000);
            }

            private void writeMask(int num, int mask) throws IOException {
                while (mask != 0) {
                    write((num&mask) != 0);
                    mask >>>= 1;
                }
            }

            private void clear() throws IOException {
                if (n == 0) return;
                buffer <<= (8 - n);
                out.write(buffer);
                n = 0;
                buffer = 0;
            }

            public void flush() throws IOException {
                clear();
                out.flush();
            }

            public void close() throws Exception {
                flush();
                out.close();
            }
        }

        public static class reader implements AutoCloseable {
            private BufferedInputStream in;
            private int buffer;
            private int n;

            public reader(String filename) throws IOException {
                in = new BufferedInputStream(new FileInputStream(filename));
                buffer = 0;
                n = 0;
                fill();
            }

            private void fill() throws IOException {

                buffer = in.read();
                n = 8;
                if (buffer == -1) throw new EOFException();
            }

            public boolean read() throws IOException {
                n--;
                boolean bit = ((buffer >>> n) & 1) == 1;
                if (n == 0) fill();
                return bit;
            }

            private int readMask(int mask) throws IOException {
                int num = 0;
                while (mask != 0) {
                    if (read())  num |= mask;
                    mask>>>=1;
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
            public BitSetL readBitSet(int n) throws IOException {
                return new BitSetL(readMask(0x1<<(n-1)),n);
            }
            public void close() throws IOException {
                in.close();
            }
        }
    }
}