package domini;

public final class JPEG {
    public final static byte MAGIC_BYTE = (byte) 0x98;

    public static void compress(final String inputFile, final String outputFile, final short quality) {
        System.out.printf("Llamda JPEG.compress(String inputFile=%s, String outputFile=%s, short quality=%d)\n", inputFile, outputFile, quality);
    }

    public static void decompress(final String inputFile, final String outputFile) {
        System.out.printf("Llamda JPEG.decompress(String inputFile=%s, String outputFile=%s)\n", inputFile, outputFile);
    }
}
