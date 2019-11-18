package domini;

public final class JPEGBlock implements Codec<byte[][], short[]> {

    private JPEGBlock () {}

    public static short[] encode(final short quality, final boolean isChrominance, final byte[][] data) {
        System.out.printf("Llamda JPEGBlock.encode(short quality=%d, boolean isChrominance=%b, byte[][] data) -> short[]\n", quality, isChrominance);
        return new short[]{0x03, 5, 0x12, 3, 0x00};
    }

    public static byte[][] decode(final short quality, final boolean isChrominance, final short[] data) {
        System.out.printf("Llamda JPEGBlock.decode(short quality=%d, boolean isChrominance=%b, short[] data) -> byte[][]\n", quality, isChrominance);
        return new byte[][] {
            {16,11,21,27,25,27,27,29},
                {23,23,23,31,28,28,28,31},
                {23,28,32,34,31,23,23,24},
                {30,35,33,32,32,32,32,37},
                {30,32,33,34,32,27,27,30},
                {33,33,33,33,32,29,29,28},
                {34,34,33,32,33,29,29,30},
                {34,34,33,32,35,29,30,25}
        };
    }

}
