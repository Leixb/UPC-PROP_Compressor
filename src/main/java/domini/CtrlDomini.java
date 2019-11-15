package domini;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CtrlDomini {
    public enum Alg {AUTOd, LZ78d, LZSSd, LZWd, JPEGd};

    public static Statistics compress(Alg alg, String fileIn, String fileOut, Short quality) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);

        switch(alg) {
            case AUTOd:
                break;
            case LZ78d:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                    stats.setStartingTime();
                    LZ78.compress(input,output);
                    stats.setEndingTime();
                }
                break;
            case LZSSd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                    stats.setStartingTime();
                    LZSS.compress(input,output);
                    stats.setEndingTime();
                }
                break;
            case LZWd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    stats.setStartingTime();
                    LZW.compress(input,output);
                    stats.setEndingTime();
                }
                break;
            case JPEGd:
                stats.setStartingTime();
                JPEG.compress(fileIn,fileOut,quality);
                stats.setEndingTime();
                break;
            default:
        }

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
        else if(b==0x11) alg = Alg.LZWd;
        else if(b==0x92) alg = Alg.JPEGd;
        else throw new Exception("Fitxer invàlid.");

        switch(alg) {
            case LZ78d:
                try(IO.Bit.reader input = new IO.Bit.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    stats.setStartingTime();
                    LZ78.decompress(input,output);
                    stats.setEndingTime();
                }
                break;
            case LZSSd:
                try(IO.Bit.reader input = new IO.Bit.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    stats.setStartingTime();
                    LZSS.decompress(input,output);
                    stats.setEndingTime();
                }
                break;
            case LZWd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    stats.setStartingTime();
                    LZW.decompress(input,output);
                    stats.setEndingTime();
                }
                break;
            case JPEGd:
                stats.setStartingTime();
                JPEG.decompress(fileIn,fileOut,(short)0);
                stats.setEndingTime();
                break;
            default:
        }

        stats.setFinFileSize(fileOut);
        return stats;
    }
}
