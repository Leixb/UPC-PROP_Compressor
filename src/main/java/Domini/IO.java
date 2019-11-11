package Domini;

import java.io.*;

public class IO {
    public static class reader implements AutoCloseable {
        private FileInputStream fin;
        private BufferedInputStream bin;

        public reader(String filename) throws FileNotFoundException {
            fin = new FileInputStream(filename);
            bin = new BufferedInputStream(fin);
        }

        public int read(byte[] buffer) throws IOException {
            return bin.read(buffer);
        }
        public int read() throws IOException {
            return bin.read();
        }

        int bitBuffer;
        int bitMask;
        public boolean readBit() throws IOException {
            if (bitMask == 0 || bitMask > 0xFF) {
                bitBuffer = read();
                if (bitBuffer == -1) throw new EOFException();

                bitMask = 1;
            }

            boolean bit = (bitBuffer & bitMask) != 0;

            bitMask = bitMask << 1;

            return bit;
        }

        void flushBitBuffer() {
            bitMask = 0x100;
        }

        public String readLine() throws IOException {
            StringBuffer sbuf = new StringBuffer();

            int c = bin.read();
            while (c != -1) {
                char Ch = (char) c;
                if (Ch == '\n') break;
                sbuf.append(Ch);

                c = bin.read(); // Read next char
            }
            return sbuf.toString();
        }

        public void close () throws IOException {
            bin.close();
            fin.close();
        }
    }

    public static class writer implements AutoCloseable {
        private FileOutputStream fout;
        private BufferedOutputStream bout;

        public writer(String filename) throws IOException {
            // Create file if not exists
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            fout = new FileOutputStream(filename);
            bout = new BufferedOutputStream(fout);
        }

        public void write(byte[] buffer) throws IOException {
            bout.write(buffer);
        }
        public void write(byte b) throws IOException {
            bout.write(b);
        }

        int bitBuffer;
        int bitMask;
        public void writeBit(boolean bit) throws IOException {
            if (bitMask == 0) {
                writeBitBuffer();
                bitBuffer = 0x00;
                bitMask = 0x80;
            }

            if (bit) bitBuffer &= bitMask;

            bitMask = bitMask >> 1;
        }

        private void writeBitBuffer() throws IOException {
            bout.write((byte)bitBuffer);
        }

        public void close () throws IOException {
            writeBitBuffer();
            bout.flush();
            bout.close();
            fout.flush();
            fout.close();
        }
    }
}
