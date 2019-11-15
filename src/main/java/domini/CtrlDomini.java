package domini;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CtrlDomini {
    public enum Alg {AUTOd, LZ78d, LZSSd, LZWd, JPEGd};

    public static Statistics compress(Alg alg, String fileIn, String fileOut, Short quality) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        switch(alg) {
            case AUTOd:
                break;
            case LZ78d:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                    LZ78.compress(input,output);
                }
                break;
            case LZSSd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                    LZSS.compress(input,output);
                }
                break;
            case LZWd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    LZW.compress(input,output);
                }
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
        else if(b==0x11) alg = Alg.LZWd;
        else if(b==0x92) alg = Alg.JPEGd;
        else throw new Exception("Fitxer invàlid.");

        stats.setStartingTime();
        switch(alg) {
            case LZ78d:
                try(IO.Bit.reader input = new IO.Bit.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    LZ78.decompress(input,output);
                }
                break;
            case LZSSd:
                try(IO.Bit.reader input = new IO.Bit.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    LZSS.decompress(input,output);
                }
                break;
            case LZWd:
                try(IO.Char.reader input = new IO.Char.reader(fileIn); IO.Char.writer output = new IO.Char.writer(fileOut)) {
                    LZW.decompress(input,output);
                }
                break;
            case JPEGd:
                JPEG.decompress(fileIn,fileOut,(short)0);
                break;
            default:
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
        return stats;
    }
}
