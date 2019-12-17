import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import domini.LZ78;
import persistencia.IO;

import java.io.*;

class LZ78Test {

	@Test
	void CompressDecompressFile() {
		new File("generated/").mkdirs();
		final String inputFile = "texts/DonQuijote.txt";
		final String outputFile = "generated/outLZ78.txt";
		final String aux = "generated/compressedFileLZ78.piz";

		try {
			try (IO.Byte.reader input = new IO.Byte.reader(inputFile); IO.Bit.writer output = new IO.Bit.writer(aux)) {
				LZ78.compress(input, output);
			}
			try (IO.Bit.reader input = new IO.Bit.reader(aux); IO.Byte.writer output = new IO.Byte.writer(outputFile)) {
				LZ78.decompress(input, output);
			}
			checkCompressDecompress(inputFile,outputFile);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	void checkCompressDecompress (String original, String decompressed) throws IOException{
		BufferedReader origin = new BufferedReader(new FileReader(original));
		BufferedReader decomp = new BufferedReader(new FileReader(decompressed));
		String checkLine;

		checkLine = origin.readLine();
		while (checkLine != null) {
			assertEquals(checkLine, decomp.readLine());
			checkLine = origin.readLine();
		}
		assertNull(decomp.readLine(),"Compressio i decompressio fallida!");
	}
}
