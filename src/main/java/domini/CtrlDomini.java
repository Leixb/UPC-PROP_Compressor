package domini;

import java.io.EOFException;

public class CtrlDomini {
    public enum Alg {LZ78d, LZSSd, LZWd, JPEGd};

    public static Statistics compress(Alg alg, String fileIn, String fileOut, Short quality) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        switch(alg) {
            case LZ78d:
                LZ78.compress(fileIn, fileOut);
                break;
            case LZSSd:
                LZSS.compress(fileIn, fileOut);
                break;
            case LZWd:
                LZW.compress(fileIn, fileOut);
                break;
            case JPEGd:
                JPEG.compress(fileIn,fileOut,quality);
                break;
            default:
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
        return stats;
    }

    public static Statistics decompress(String fileIn, String fileOut) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);

        Alg alg;
        int b;
        try(IO.Byte.reader reader = new IO.Byte.reader(fileIn)){
            b = reader.read();
        }

        if(b == -1) throw new EOFException();

        if(b==LZ78.MAGIC_BYTE) alg = Alg.LZ78d;
        else if(b==LZSS.MAGIC_BYTE) alg = Alg.LZSSd;
        else if(b==LZW.MAGIC_BYTE) alg = Alg.LZWd;
        else if(b==JPEG.MAGIC_BYTE) alg = Alg.JPEGd;
        else throw new Exception("Fitxer invàlid.");

        stats.setStartingTime();
        switch(alg) {
            case LZ78d:
                LZ78.decompress(fileIn, fileOut);
                break;
            case LZSSd:
                LZSS.decompress(fileIn, fileOut);
                break;
            case LZWd:
                LZW.decompress(fileIn, fileOut);
                break;
            case JPEGd:
                JPEG.decompress(fileIn, fileOut);
                break;
            default:
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
        return stats;
    }
}
