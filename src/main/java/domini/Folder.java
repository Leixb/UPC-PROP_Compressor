/**
 * @file ./src/main/java/domini/Folder.java
 * @author Aleix Boné
 */
package domini;

import persistencia.IO;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * @brief Compresor y descompresor de carpetas
 */
public final class Folder {

    /** Constructora vacía Folder */
    private Folder() {}

    /// Marca separadora
    private static char MARKER = 0xFFFF;

    /// MagicByte Folder
    public final static byte MAGIC_BYTE = (byte)0xf0;

    /**
     * @brief Dada la dirección de la carpeta a comprimir y un objeto de escritura del archivo comprimido, ejecuta la compresión con el algoritmo pertienente
     *
     * @param folderPath dirección de la carpeta a comprimir
     * @param output objeto de escritura del archivo comprimido
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    public static void compress(String folderPath, IO.Bit.writer output) throws IOException {
        Path basePath = Paths.get(folderPath);
        try(Stream<Path> files = Files.walk(basePath).filter(Files::isRegularFile)) {
            files.forEach(f -> {
                try {
                    appendFile(basePath, f, output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * @brief
     *
     * @param basePath
     * @param file dirección del archivo
     * @param output objeto de escritura del archivo comprimido
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    private static void appendFile(Path basePath, Path file, IO.Bit.writer output) throws IOException {
        // Write filename + Marker then call compression algorithm
        for (char c : (basePath.relativize(file)).toString().toCharArray()) {
            output.write(c);
        }
        output.write(MARKER);
        try (IO.Byte.reader input = new IO.Byte.reader(file.toString())) {
            CtrlDomini.getInstance().compress(0, input, output, (short) 0);
        }
    }

    /**
     * @brief Dada la dirección de la carpeta a descomprimir y un objeto de lectura del archivo comprimido, ejecuta la descompresión con el algoritmo pertienente
     *
     * @param folderPath dirección de la carpeta a descomprimir
     * @param input objeto de lectura del archivo comprimido
     * @throws IOException Lanza cualquier excepción generada al descomprimir
     */
    public static void decompress(String folderPath, IO.Bit.reader input) throws IOException {
        // Till EOF: Read filenames (with markers), then magic byte and then call decompress of the corresponding algorithm
        for(;;) {
            String filename = "";
            try {
                for (;;) {
                    char c = (char)input.readChar();
                    if (c == MARKER) break; // got filename
                    filename += c;
                }
            } catch (EOFException e) {
                // EOF
                return;
            }

            final String filePath = Paths.get(folderPath, filename).toString();
            new File(filePath).getParentFile().mkdirs(); // Create dirs if not exist

            try (IO.Byte.writer output = new IO.Byte.writer(filePath)) {
                CtrlDomini.getInstance().decompress(input, output, (byte)0);
            } 
        }
    }

}
