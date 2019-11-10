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
        public void close () throws IOException {
            bout.flush();
            bout.close();
            fout.flush();
            fout.close();
        }
    }
}
