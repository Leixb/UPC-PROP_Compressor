/**
 * @file ./src/main/java/domini/Folder.java
 * @author ***REMOVED***
 */
package domini;

import persistencia.IO;

import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * @brief Compresor y descompresor de carpetas
 */
public final class Folder {

    private Folder() {}

    private static char MARKER = 0xFFFF;

    public static void compress(String folderPath, IO.Bit.writer output) throws IOException {
        try(Stream<Path> file = Files.walk(Paths.get(folderPath)).filter(Files::isRegularFile)) {
            file.forEach((p) -> {
                try {
                    appendFile(p.toString(), output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static void appendFile(String filename, IO.Bit.writer output) throws IOException {
        // Write filename + Marker then call compression algorithm
        for (char c : filename.toCharArray()) {
            output.write(c);
        }
        output.write(MARKER);
        try (IO.Byte.reader input = new IO.Byte.reader(filename)) {
            LZW.compress(input, output);
        }
    }

    public static void decompress(String folderPath, IO.Bit.reader input) throws IOException {
        // Till EOF: Read filenames (with markers), then magic byte and then call decompress of the corresponding algorithm
        for(;;) {
            String filename = "";
            for (;;) {
                int read = input.readChar();
                if (read == -1) return; // EOF
                char c = (char) read;
                if (c == MARKER) break; // got filename
                filename += c;
            }

            final String filePath = Paths.get(folderPath, filename).toString();
            new File(filePath).getParentFile().mkdirs(); // Create dirs if not exist

            try (IO.Byte.writer output = new IO.Byte.writer(filePath)) {
                LZW.decompress(input, output);
            }
        }
    }

}
