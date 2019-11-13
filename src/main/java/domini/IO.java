package domini;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
}