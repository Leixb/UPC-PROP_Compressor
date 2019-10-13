import java.io.*;

public class IO {
    public class reader {
        private FileInputStream fin;
        private BufferedInputStream bin;

        public reader(String filename) throws FileNotFoundException {
            fin = new FileInputStream(filename);
            bin = new BufferedInputStream(fin);
        }
        public byte[] read(int n) throws IOException {
            byte[] buffer = new byte[n];

            bin.read(buffer);

            return buffer;
        }
        public void close () throws IOException {
            bin.close();
            fin.close();
        }
    }

    public class writer {
        private FileOutputStream fout;
        private BufferedOutputStream bout;

        public writer(String filename) throws FileNotFoundException {
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