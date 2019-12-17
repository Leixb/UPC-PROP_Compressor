/**
 * @file ./src/main/java/domini/Folder.java
 * @author TODO
*/
package domini;

import persistencia.IO;

/**
 * @brief Compresor y descompresor de carpetas
 */
public final class Folder {

    private Folder() {}

	public static void compress(String folderPath, IO.Bit.writer output) {
		// for all files in folderPath do : appendFile(fileane, output).
	}

	private static void appendFile(String filename, IO.Bit.writer output) {
		// Write filename + Marker then call compression algorithm
	}

	public static void decompress(String folderPath, IO.Bit.reader input) {
		// Till EOF: Read filenames (with markers), then magic byte and then call decompress of the corresponding algorithm
	}

}
