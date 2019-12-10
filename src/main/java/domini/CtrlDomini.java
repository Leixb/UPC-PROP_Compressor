/**
 * @file ./src/main/java/domini/CtrlDomini.java
 * @author Albert Mercadé Plasencia / Aleix Boné
 */
package domini;

import java.io.EOFException;

/**
 * @brief Controlador del dominio
 */
public class CtrlDomini {
    /**
     * @brief Dado un algoritmo, el nombre fichero de entrada y el nombre del fichero comprimido, ejecuta la compresión con el algoritmo pertienente
     * @param alg algoritmo con el que comprimir
     * @param fileIn nombre del archivo a comprimir
     * @param fileOut nombre del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @return Devuelve las estadisrticas generadas para la compresión
     * @throws Exception Lanza cualquier excepción generada al comprimir
     */
    public static Statistics compress(int alg, String fileIn, String fileOut, Short quality) throws Exception {
        Statistics stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        switch(alg) {
            case 0:
                if(fileIn.endsWith(".ppm")){
                    quality = 80; // auto JPEG qualitat 80.
                    JPEG.compress(fileIn,fileOut,quality);
                }
                else {
                    LZ78.compress(fileIn, fileOut);
                }
                break;
            case 1:
                LZ78.compress(fileIn, fileOut);
                break;
            case 2:
                LZSS.compress(fileIn, fileOut);
                break;
            case 3:
                LZW.compress(fileIn, fileOut);
                break;
            case 4:
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

        int alg;
        int b;
        try(IO.Byte.reader reader = new IO.Byte.reader(fileIn)){
            b = reader.read();
        }

        if(b == -1) throw new EOFException();

        byte magicByte = (byte) b;

        if(magicByte==LZ78.MAGIC_BYTE) alg = 1;
        else if(magicByte==LZSS.MAGIC_BYTE) alg = 2;
        else if(magicByte==LZW.MAGIC_BYTE) alg = 3;
        else if(magicByte==JPEG.MAGIC_BYTE) alg = 4;
        else throw new Exception("Fitxer invàlid.");

        stats.setStartingTime();
        switch(alg) {
            case 1:
                LZ78.decompress(fileIn, fileOut);
                break;
            case 2:
                LZSS.decompress(fileIn, fileOut);
                break;
            case 3:
                LZW.decompress(fileIn, fileOut);
                break;
            case 4:
                JPEG.decompress(fileIn, fileOut);
                break;
            default:
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
        return stats;
    }

}
