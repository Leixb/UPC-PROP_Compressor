package domini;

public final class LZ78 {
    public final static byte MAGIC_BYTE = 0x78;

    public static void compress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZ78.compress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }

    public static void decompress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZ78.decompress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }
}
