package domini;

public final class LZW {
    public final static byte MAGIC_BYTE = 0x69;

    public static void compress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZW.compress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }

    public static void decompress(final String inputFilename, final String outputFilename) {
        System.out.printf("Llamda LZW.decompress(String inputFile=%s, String outputFile=%s)\n", inputFilename, outputFilename);
    }
}
