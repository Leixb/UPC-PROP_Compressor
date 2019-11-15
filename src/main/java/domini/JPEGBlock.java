package domini;

import java.util.ArrayList;

public class JPEGBlock implements Codec<byte[][], short[]> {

    public static class DCT implements Codec<byte[][], double[][]> {
        public static double[][] encode(byte[][] data) {
            double[][]G = new double[8][8];

            for (int u = 0; u < 8; u++) {
                for (int v = 0; v < 8; v++) {

                    G[u][v] = 0;

                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            int dataxy = Byte.toUnsignedInt(data[x][y]) - 128;
                            G[u][v] += dataxy * Math.cos((2 * x + 1)*u*Math.PI/16.0)
                                              * Math.cos((2 * y + 1)*v*Math.PI/16.0);
                        }
                    }

                    double ortho = 0.25;

                    // Make orthonormal
                    if (u == 0) ortho *= 1.0/Math.sqrt(2);
                    if (v == 0) ortho *= 1.0/Math.sqrt(2);

                    G[u][v]*=ortho;

                }
            }

            return G;
        }

        public static byte[][] decode(double[][] data) {
            byte[][] f = new byte[8][8];

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {

                    double fxy = 0;

                    for (int u = 0; u < 8; u++) {
                        for (int v = 0; v < 8; v++) {

                            // Make orthonormal
                            double ortho = 1;
                            if (u == 0) ortho *= 1.0/Math.sqrt(2);
                            if (v == 0) ortho *= 1.0/Math.sqrt(2);

                            fxy += ortho * data[u][v] * Math.cos((2 * x + 1)*u*Math.PI/16.0)
                                                      * Math.cos((2 * y + 1)*v*Math.PI/16.0);
                        }
                    }

                    fxy *= 0.25;

                    if (fxy >= 127) fxy = 127;
                    if (fxy <= -128) fxy = -128;
                    int v = ((int)fxy +128)&0xFF;
                    f[x][y] = ((byte) v);

                }
            }
            return f;
        }
    }

    public static class Quantization implements Codec<double[][], short[][]> {
        final static private byte[][] LuminanceTable = {
            {  16 ,  11 ,  10 ,  16 ,  24 ,  40 ,  51 ,  61 },
            {  12 ,  12 ,  14 ,  19 ,  26 ,  58 ,  60 ,  55 },
            {  14 ,  13 ,  16 ,  24 ,  40 ,  57 ,  69 ,  56 },
            {  14 ,  17 ,  22 ,  29 ,  51 ,  87 ,  80 ,  62 },
            {  18 ,  22 ,  37 ,  56 ,  68 , 109 , 103 ,  77 },
            {  24 ,  35 ,  55 ,  64 ,  81 , 104 , 113 ,  92 },
            {  49 ,  64 ,  78 ,  87 , 103 , 121 , 120 , 101 },
            {  72 ,  92 ,  95 ,  98 , 112 , 100 , 103 ,  99 }
        };

        final static private byte[][] ChrominanceTable = {
            {  17 ,  18 ,  24 ,  47 ,  99 ,  99 ,  99 ,  99 },
            {  18 ,  21 ,  26 ,  66 ,  99 ,  99 ,  99 ,  99 },
            {  24 ,  26 ,  56 ,  99 ,  99 ,  99 ,  99 ,  99 },
            {  47 ,  66 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 },
            {  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 },
            {  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 },
            {  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 },
            {  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 ,  99 }
        };

        static double QuantizationValue(short quality, boolean isChrominance, short x, short y) throws IllegalArgumentException {
            if (quality < 1 || quality > 100) throw new IllegalArgumentException("Quality must be between 1 and 100");
            if (x < 0 || x >= 8) throw new IllegalArgumentException("0 < x < 8");
            if (y < 0 || y >= 8) throw new IllegalArgumentException("0 < y < 8");

            byte[][] QTable = LuminanceTable;
            if (isChrominance) QTable = ChrominanceTable;

            double q = 5000.0/quality;
            if (quality >= 50) q = 200 - 2*quality;

            return (q*QTable[x][y] + 50)/100;
        }

        public static short[][] encode(double[][] data) {
            return encode((short)50, true, data);
        }

        public static short[][] encode(short quality, boolean isChrominance, double[][] block) {
            short[][] data = new short[8][8];
            for (short i = 0; i < 8; ++i) {
                for (short j = 0; j < 8; ++j) {
                    double quantVal = block[i][j]/QuantizationValue(quality, isChrominance, i, j);

                    data[i][j] = (short) quantVal;
                }
            }
            return data;
        }

        public static double[][] decode(short[][] data) {
            return decode((short)50, true, data);
        }

        public static double[][] decode(short quality, boolean isChrominance, short[][] block) {
            double[][] data = new double[8][8];
            for (short i = 0; i < 8; ++i) {
                for (short j = 0; j < 8; ++j) {
                    data[i][j] = block[i][j]*QuantizationValue(quality, isChrominance, i, j);
                }
            }
            return data;
        }
    }

    public static class ZigZag implements Codec<short[][], short[]> {

        // Correspondencia coordenades taula amb ZigZag
        private static byte[][] table;

        private static void calculateCorrespondenceTable() {
            table = new byte[8][8];

            int i=0, j=0;
            boolean up = true;
            for (byte pos = 0; pos < 8*8; ++pos) {
                table[i][j] = pos;

                if (up) {
                    --i;
                    ++j;
                } else {
                    ++i;
                    --j;
                }

                boolean change_dir = true;

                if (i == 8 && j == -1) {
                    i = 7;
                    j = 1;
                } else if (i < 0) {
                    i = 0;
                } else if (j < 0) {
                    j = 0;
                } else if (i >= 8) {
                    i = 7;
                    j += 2;
                } else if (j >= 8) {
                    j = 7;
                    i += 2;
                } else {
                    change_dir = false;
                }

                if (change_dir) up = !up;
            }
        }

        public static short[] encode(short[][] block) {
            if (table == null) {
                calculateCorrespondenceTable();
            }

            short[] L = new short[8*8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (table[i][j] > L.length)
                        System.out.println(table[i][j]);
                    L[table[i][j]] = block[i][j];
                }
            }

            return L;
        }

        public static short[][] decode(short[] L) {
            if (table == null) {
                calculateCorrespondenceTable();
            }
            short[][] block = new short[8][8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    block[i][j] = L[table[i][j]];
                }
            }

            return block;
        }

    }

    private static int bitLength(short n) {
        return 0xF & (int) (Math.floor(Math.log(Math.abs(n)) / Math.log(2)) + 1);
    }

    public static class RLE implements Codec<short[], short[]> {
        // Codifica en RLE (primer byte = nombre de 0 precedents, segon valor.
        // Acaba amb 0,0
        public static short[] encode(short[] data) {

            ArrayList<Short> buff = new ArrayList<Short>();

            // Afegim DC
            if (data[0] == 0) buff.add((short)0);
            else {
                buff.add((short)bitLength(data[0]));
                buff.add(data[0]);
            }

            // Comencem des de 1 ja que 0 es el DC
            for (int i = 1; i < data.length; ++i) {
                byte count = 0;
                while (i < data.length && data[i] == 0) {
                    ++i;
                    ++count;
                }
                if (i >= data.length) {
                    //buff.add((short) 0x00); // EOB
                    break;
                }
                if (count >= 16) {
                    for (int j = 0; j < count / 16; ++j) {
                        buff.add((short) 0xF0); // ZRL
                    }
                    count %= 16;
                }

                if (data[i] > 1023) {
                    System.out.printf("padding -> %d\n", data[i]);
                    data[i] = 1023;
                } else if (data[i] < -1023) {
                    System.out.printf("padding -> %d\n", data[i]);
                    data[i] = -1023;
                }

                buff.add((short) ((count<<4) | bitLength(data[i])));
                buff.add(data[i]);

            }
            buff.add((short) 0x00); // EOB
            //buff.add((short) 0x00);
            short[] r = new short[buff.size()];
            for (int i = 0; i < buff.size(); ++i) {
                r[i] = buff.get(i);
            }
            return r;
        }

        public static short[] decode(short[] data) {

            short[] decodedData = new short[64];

            // i (data), j (decodedData)
            int i, j = 1;

            // data[0] = length DC, data[1] = DC
            if (data[0] == 0) {
                decodedData[0] = 0;
                i = 1;
            } else {
                decodedData[0] = data[1];
                i = 2;
            }

            for (; i < data.length; ) {
                if (data[i] == 0x00) break;

                int run = (data[i] & 0xF0)>>4;
                int length = data[i] & 0x0F;

                j += run; // Fill with zeros
                ++i;

                if (length == 0) continue;
                decodedData[j] = data[i];
                ++i;
                ++j;
            }

            return decodedData;
            
        }
    }

    public static short[] encode(byte[][] data) {
        return encode((short)50, true, data);
    }

    public static short[] encode(short quality, boolean isChrominance, byte[][] data) {

        double[][] DctEnc = DCT.encode(data);
        short[][] quantEnc = Quantization.encode(quality, isChrominance, DctEnc);
        short[] zigEnc = ZigZag.encode(quantEnc);
        short[] result = RLE.encode(zigEnc);

        return result;

    }

    public static byte[][] decode(short[] data) {
        return decode((short)50, true, data);
    }

    public static byte[][] decode(short quality, boolean isChrominance, short[] data) {

        short[] rleDec = RLE.decode(data);
        short[][] zigDec = ZigZag.decode(rleDec);
        double[][] quantDec = Quantization.decode(quality, isChrominance, zigDec);
        byte[][] result = DCT.decode(quantDec);

        return result;

    }

}
