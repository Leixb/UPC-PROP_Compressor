/**
 * @file ./src/main/java/domini/Folder.java
 * @author ***REMOVED***
 */
package domini;

import persistencia.IO;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @brief Compresor y descompresor de carpetas
 */
public final class Folder {

    private Folder() {}

    private static char EMPTY_FOLDER = 0xFFEF;
    private static char FILE = 0xFFF1;

    public final static byte MAGIC_BYTE = (byte)0xf0;

    public static void compress(String folderPath, IO.Bit.writer output) throws IOException {
        Path root = Paths.get(folderPath);

        Files.walkFileTree(root, new CompressFiles(root, output));
    }

    private static class CompressFiles implements FileVisitor<Path> {
        private IO.Bit.writer output;
        private Path root; 

        private boolean emptyDir = true;

        public CompressFiles(Path root, IO.Bit.writer output) {
            this.output = output;
            this.root = root;
            this.emptyDir = true;
        }

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (emptyDir) {
                for (char c : root.relativize(dir).toString().toCharArray()) {
                    output.write(c);
                }
                output.write(EMPTY_FOLDER);
            }
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            emptyDir = true;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            emptyDir = false;
            for (char c : root.relativize(file).toString().toCharArray()) {
                output.write(c);
            }
            output.write(FILE);
            try (IO.Byte.reader input = new IO.Byte.reader(file.toString())) {
                CtrlDomini.getInstance().compress(0, input, output, (short) 0);
            }
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            throw exc;
		}
    }

    public static void decompress(String folderPath, IO.Bit.reader input) throws IOException {
        // Till EOF: Read filenames (with markers), then magic byte and then call decompress of the corresponding algorithm
        for(;;) {
            String filename = "";
            char c;
            try {
                for (;;) {
                    c = (char)input.readChar();
                    if (c == FILE || c == EMPTY_FOLDER) break; // got filename
                    filename += c;
                }
            } catch (EOFException e) {
                // EOF
                return;
            }

            final String filePath = Paths.get(folderPath, filename).toString();

            if (c == FILE) {
                new File(filePath).getParentFile().mkdirs(); // Create dirs if not exist
                try (IO.Byte.writer output = new IO.Byte.writer(filePath)) {
                    CtrlDomini.getInstance().decompress(input, output, (byte)0);
                } 
            } else if (c == EMPTY_FOLDER) {
                new File(filePath).mkdirs(); // Create dir
            } else {
                throw new FolderFormatException();
            }
        }
    }

    public static class FolderFormatException extends IOException {
		private static final long serialVersionUID = 89267786123678912L;

        public FolderFormatException() {
            super("Invalid marker found");
        }
    }
}
