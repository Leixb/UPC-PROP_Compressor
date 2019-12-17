/**
 * @file ./src/main/java/domini/CtrlDomini.java
 * @author Albert Mercadé Plasencia / Aleix Boné
 */
package domini;

import persistencia.IO;

import java.io.EOFException;
import java.text.DecimalFormat;

/**
 * @brief Controlador del dominio
 */
public class CtrlDomini {
    private String fileIn;
    private String fileOut;
    private Statistics stats;

    /**
     * @brief Dado un algoritmo, el nombre fichero de entrada y el nombre del fichero comprimido, ejecuta la compresión con el algoritmo pertienente
     * @param alg algoritmo con el que comprimir
     * @param fi nombre del archivo a comprimir
     * @param fo nombre del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @return Devuelve las estadisrticas generadas para la compresión
     * @throws Exception Lanza cualquier excepción generada al comprimir
     */
    public void compress(int alg, String fileIn, String fileOut, Short quality) throws Exception {
        stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        try (IO.Byte.reader input = new IO.Byte.reader(fileIn);
             IO.Bit.writer output = new IO.Bit.writer(fileOut)) {

            switch(alg) {
                case 0:
                    if(fileIn.endsWith(".ppm")){
                        quality = 80; // auto JPEG qualitat 80.
                        JPEG.compress(input, output, quality);
                    }
                    else {
                        LZ78.compress(input, output);
                    }
                    break;
                case 1:
                    LZ78.compress(input, output);
                    break;
                case 2:
                    LZSS.compress(input, output);
                    break;
                case 3:
                    LZW.compress(input, output);
                    break;
                case 4:
                    JPEG.compress(input, output, quality);
                    break;
                default:
            }
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
    }

    /**
     * @brief Dado un archivo comprimido y el nombre para el archivo descomprimido, descomprime el archivo usando el mismo algoritmo con el que se comprimió
     * @param fi nombre del fichero comprimido
     * @param fo nombre del fichero descomprimido
     * @return Estadisticas generadas durante la descompresión
     * @throws Exception Lanza cualquier excepción generada al descomprimir
     */
    public void decompress(String fileIn, String fileOut) throws Exception {
        int b;
        try(IO.Byte.reader reader = new IO.Byte.reader(fileIn)){
            b = reader.read();
        }

        if(b == -1) throw new EOFException();

        byte magicByte = (byte) b;

        int alg;
        if(magicByte==LZ78.MAGIC_BYTE) alg = 1;
        else if(magicByte==LZSS.MAGIC_BYTE) alg = 2;
        else if(magicByte==LZW.MAGIC_BYTE) alg = 3;
        else if(magicByte==JPEG.MAGIC_BYTE) alg = 4;
        else throw new Exception("Fitxer a descomprimir invàlid.");

        if(!fileOut.endsWith(".ppm") && alg == 4) fileOut += ".ppm";

        stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        try (IO.Bit.reader input = new IO.Bit.reader(fileIn);
             IO.Byte.writer output = new IO.Byte.writer(fileOut)) {

            switch(alg) {
                case 1:
                    LZ78.decompress(input, output);
                    break;
                case 2:
                    LZSS.decompress(input, output);
                    break;
                case 3:
                    LZW.decompress(input, output);
                    break;
                case 4:
                    JPEG.decompress(input, output);
                    break;
                default:
            }
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
    }

    public String getTime() {
        return String.format("%.2f s", stats.getTime());
    }

    public String getDeflated() {
        String iniFileSize = readableFileSize(stats.getIniFileSize());
        String finFileSize = readableFileSize(stats.getFinFileSize());
        return String.format("%s -> %s (%.2f%%)", iniFileSize, finFileSize, stats.getPercentageCompressed());
    }

    public String getSpeedCompress() {
        String compSpeed = readableFileSize(stats.getSpeedCompressed());
        return String.format("%s/s", compSpeed);
    }

    public String getInflated() {
        String iniFileSize = readableFileSize(stats.getIniFileSize());
        String finFileSize = readableFileSize(stats.getFinFileSize());
        return String.format("%s -> %s (%.2f%%)", iniFileSize, finFileSize, stats.getPercentageDecompressed());
    }

    public String getSpeedDecompress() {
        String decompSpeed = readableFileSize(stats.getSpeedDecompressed());
        return String.format("%s/s", decompSpeed);
    }

    public String getFileIn() {
        return fileIn;
    }

    public String getFileOut() {
        return fileOut;
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
