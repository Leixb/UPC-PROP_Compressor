import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JPEG implements Codec<byte[][], byte[]> {

    public static class DCT implements Codec<byte[][], double[][]> {
        public static double[][] encode(byte[][] data) {
            double[][]G = new double[8][8];

            for (int u = 0; u < 8; u++) {
                for (int v = 0; v < 8; v++) {

                    G[u][v] = 0;

                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            G[u][v] += data[x][y] * Math.cos((2 * x + 1) *u*Math.PI/16)
                                * Math.cos((2 * y + 1) *v*Math.PI/16);
                        }
                    }

                    double ortho = 0.25;

                    // Make orthonormal
                    if (u == 0) ortho *= 1/Math.sqrt(2);
                    if (v == 0) ortho *= 1/Math.sqrt(2);

                    G[u][v]*=ortho;

                }
            }

            return G;
        }

        public static byte[][] decode(double[][] data) {
            byte[][] f = new byte[8][8];

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {

                    float fxy = 0;

                    for (int u = 0; u < 8; u++) {
                        for (int v = 0; v < 8; v++) {

                            // Make orthonormal
                            double ortho = 1;
                            if (u == 0) ortho *= 1/Math.sqrt(2);
                            if (v == 0) ortho *= 1/Math.sqrt(2);

                            fxy += ortho * data[u][v] * Math.cos((2 * x + 1) *u*Math.PI/16)
                                * Math.cos((2 * y + 1) *v*Math.PI/16);
                        }
                    }

                    fxy *= 0.25;

                    if (fxy > 127) fxy = 127;
                    if (fxy < -128) fxy = -128;
                    f[x][y] = (byte) fxy;

                }
            }
            return f;
        }
    }

    public static class Quantization implements Codec<double[][], byte[][]> {
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

        public static byte[][] encode(double[][] data) {
            return encode((short)50, true, data);
        }

        public static byte[][] encode(short quality, boolean isChrominance, double[][] block) {
            byte[][] data = new byte[8][8];
            for (short i = 0; i < 8; ++i) {
                for (short j = 0; j < 8; ++j) {
                    double quantVal = block[i][j]/QuantizationValue(quality, isChrominance, i, j);

                    if (quantVal > 127) quantVal = 127;
                    else if (quantVal < -128) quantVal = -128;

                    data[i][j] = (byte) quantVal;
                }
            }
            return data;
        }

        public static double[][] decode(byte[][] data) {
            return decode((short)50, true, data);
        }

        public static double[][] decode(short quality, boolean isChrominance, byte[][] block) {
            double[][] data = new double[8][8];
            for (short i = 0; i < 8; ++i) {
                for (short j = 0; j < 8; ++j) {
                    data[i][j] = block[i][j]*QuantizationValue(quality, isChrominance, i, j);
                }
            }
            return data;
        }
    }

    public static class ZigZag implements Codec<byte[][], byte[]> {

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

        public static byte[] encode(byte[][] block) {
            if (table == null) {
                calculateCorrespondenceTable();
            }

            byte[] L = new byte[8*8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    L[table[i][j]] = block[i][j];
                }
            }

            return L;
        }

        public static byte[][] decode(byte[] L) {
            if (table == null) {
                calculateCorrespondenceTable();
            }
            byte[][] block = new byte[8][8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    block[i][j] = L[table[i][j]];
                }
            }

            return block;
        }

    }

    public static class RLE implements Codec<byte[], byte[]> {
        // Codifica en RLE (primer byte = nombre de 0 precedents, segon valor.
        // Acaba amb 0,0
        public static byte[] encode(byte[] data) {
            ByteArrayOutputStream rleData = new ByteArrayOutputStream();
            for (int i = 0; i < data.length; ++i) {
                byte count = 0;
                while (i < data.length && data[i] == 0) {
                    ++i;
                    ++count;
                }

                if (i >= data.length) break;

                rleData.write(count);
                rleData.write(data[i]);
            }
            rleData.write((byte)0);
            rleData.write((byte)0);
            return rleData.toByteArray();
        }

        public static byte[] decode(byte[] data) {
            byte[] decodedData = new byte[64];
            ByteArrayInputStream inputData = new ByteArrayInputStream(data);
            byte[] RLEtuple = new byte[2];
            int i = 0;
            while (true) {
                try {
                    if (!(inputData.read(RLEtuple) == 2)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                if (RLEtuple[0] == 0 && RLEtuple[1] == 0) break;
                i += RLEtuple[0];
                decodedData[i] = RLEtuple[1];
                ++i;
            }
            return decodedData;
        }
    }

    public static class Huffman implements Codec<byte[], byte[]> {
        public static byte[] encode(byte[] data) {
            return data;
        }
        public static byte[] decode(byte[] data) {
            return data;
        }
    }

    public static byte[] encode(byte[][] data) {
        return encode((short)50, true, data);
    }

    public static byte[] encode(short quality, boolean isChrominance, byte[][] data) {

        double[][] DctEnc = DCT.encode(data);
        byte[][] quantEnc = Quantization.encode(quality, isChrominance, DctEnc);
        byte[] zigEnc = ZigZag.encode(quantEnc);
        byte[] rleEnc = RLE.encode(zigEnc);
        byte[] result = Huffman.encode(rleEnc);

        return result;

    }

    public static byte[][] decode(byte[] data) {
        return decode((short)50, true, data);
    }

    public static byte[][] decode(short quality, boolean isChrominance, byte[] data) {

        byte[] huffDec = Huffman.decode(data);
        byte[] rleDec = RLE.decode(huffDec);
        byte[][] zigDec = ZigZag.decode(rleDec);
        double[][] quantDec = Quantization.decode(quality, isChrominance, zigDec);
        byte[][] result = DCT.decode(quantDec);

        return result;

    }

}
