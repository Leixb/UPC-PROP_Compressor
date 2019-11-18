package domini;

public final class LZSS {
    public final static byte MAGIC_BYTE = 0x55;

    public static void compress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZSS.compress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }

    public static void decompress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZSS.decompress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }
}
