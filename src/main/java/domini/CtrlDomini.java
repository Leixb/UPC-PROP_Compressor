/**
 * @file ./src/main/java/domini/CtrlDomini.java
 * @author Albert Mercadé Plasencia / Aleix Boné
 */
package domini;

import java.io.EOFException;
import java.text.DecimalFormat;

/**
 * @brief Controlador del dominio
 */
public class CtrlDomini {
    private Statistics stats;

    /**
     * @brief Dado un algoritmo, el nombre fichero de entrada y el nombre del fichero comprimido, ejecuta la compresión con el algoritmo pertienente
     * @param alg algoritmo con el que comprimir
     * @param fileIn nombre del archivo a comprimir
     * @param fileOut nombre del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @return Devuelve las estadisrticas generadas para la compresión
     * @throws Exception Lanza cualquier excepción generada al comprimir
     */
    public void compress(int alg, String fileIn, String fileOut, Short quality) throws Exception {
        stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        fileOut = fileIn.substring(0, fileIn.lastIndexOf('/')+1) + fileOut;

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
    }

    /**
     * @brief Dado un archivo comprimido y el nombre para el archivo descomprimido, descomprime el archivo usando el mismo algoritmo con el que se comprimió
     * @param fileIn nombre del fichero comprimido
     * @param fileOut nombre del fichero descomprimido
     * @return Estadisticas generadas durante la descompresión
     * @throws Exception Lanza cualquier excepción generada al descomprimir
     */
    public void decompress(String fileIn, String fileOut) throws Exception {
        stats = new Statistics();
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
    }

    public String getTime() {
        return String.format("%.2f s", stats.getTime());
    }

    public String getInflated() {
        String iniFileSize = readableFileSize(stats.getIniFileSize());
        String finFileSize = readableFileSize(stats.getFinFileSize());
        return String.format("%s -> %s (%.2f%%)", iniFileSize, finFileSize, stats.getPercentageCompressed());
    }

    public String getSpeedCompress() {
        String compSpeed = readableFileSize(stats.getSpeedCompressed());
        return String.format("%sps", compSpeed);
    }

    /**
     * @brief Da el formato correcto al tamaño de un fichero (B,kB,etc)
     * @param d Tamaño de un fichero en bytes
     * @return Tamaño del fichero en la magnitud que le corresponda
     */
    private static String readableFileSize(double d) {
        if (d <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(d) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(d / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
