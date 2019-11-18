package domini;

import java.io.EOFException;

/**
 * @author ***REMOVED*** Plasencia / ***REMOVED***
 * @brief Controlador del domini
 */
public class CtrlDomini {
    /**
     * @brief Enum para identificar cada algoritmo
     */
    public enum Alg {LZ78d, LZSSd, LZWd, JPEGd};

    /**
     * @brief Dado un algoritmo, el nombre fichero de entrada y el nombre del fichero comprimido, ejecuta la compresión con el algoritmo pertienente
     * @param alg algoritmo con el que comprimir
     * @param fileIn nombre del archivo a comprimir
     * @param fileOut nombre del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @return Devuelve las estadisrticas generadas para la compresión
     * @throws Exception Lanza cualquier excepción generada al comprimir
     */
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

    /**
     * @brief Dado un archivo comprimido y el nombre para el archivo descomprimido, descomprime el archivo usando el mismo algoritmo con el que se comprimió
     * @param fileIn nombre del fichero comprimido
     * @param fileOut nombre del fichero descomprimido
     * @return Estadisticas generadas durante la descompresión
     * @throws Exception Lanza cualquier excepción generada al descomprimir
     */
    public static Statistics decompress(String fileIn, String fileOut) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);

        Alg alg;
        int b;
        try(IO.Byte.reader reader = new IO.Byte.reader(fileIn)){
            b = reader.read();
        }

        if(b == -1) throw new EOFException();

        byte magicByte = (byte) b;

        if(magicByte==LZ78.MAGIC_BYTE) alg = Alg.LZ78d;
        else if(magicByte==LZSS.MAGIC_BYTE) alg = Alg.LZSSd;
        else if(magicByte==LZW.MAGIC_BYTE) alg = Alg.LZWd;
        else if(magicByte==JPEG.MAGIC_BYTE) alg = Alg.JPEGd;
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
