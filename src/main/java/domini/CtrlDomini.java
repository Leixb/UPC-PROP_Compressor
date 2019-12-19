/**
 * @file ./src/main/java/domini/CtrlDomini.java
 * @author Albert Mercadé Plasencia / Aleix Boné
 */
package domini;

import persistencia.IO;

import java.io.EOFException;
import java.io.File;
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
    public void compress(int alg, String fi, String fo, Short quality) throws Exception {
        fileIn = fi;
        fileOut = fo;

        stats = new Statistics();
        stats.setIniFileSize(fileIn);
        stats.setStartingTime();

        if (new File(fileIn).isDirectory()) {
            try(IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                output.write(Folder.MAGIC_BYTE);
                Folder.compress(fileIn, output);
            }
        } else {
            try (IO.Byte.reader input = new IO.Byte.reader(fileIn);
                 IO.Bit.writer output = new IO.Bit.writer(fileOut)) {
                compress(alg, input, output, quality);
            }
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
    }

    void compress(int alg, IO.Byte.reader input, IO.Bit.writer output, Short quality) throws Exception {
        CompressionAlg comp;
        switch(alg) {
            case 0:
                if(fileIn.endsWith(".ppm")){
                    comp = new JPEG((short) 80);
                }
                else if(fileIn.endsWith(".txt")){
                    if(fileIn.length()<1000000){ //1MB
                        comp = new LZ78();
                    }
                    else {
                        comp = new LZW();
                    }
                }
                else {
                    comp = new LZSS();
                }
                break;
            case 1:
                comp = new LZ78();
                break;
            case 2:
                comp = new LZSS();
                break;
            case 3:
                comp = new LZW();
                break;
            case 4:
                comp = new JPEG(quality);
                break;
            default:
                throw new Exception("No hauries de veure aixó.");
        }
        output.write(comp.getMagicByte());
        comp.compress(input, output);
    }

    /**
     * @brief Dado un archivo comprimido y el nombre para el archivo descomprimido, descomprime el archivo usando el mismo algoritmo con el que se comprimió
     * @param fi nombre del fichero comprimido
     * @param fo nombre del fichero descomprimido
     * @return Estadisticas generadas durante la descompresión
     * @throws Exception Lanza cualquier excepción generada al descomprimir
     */
    public void decompress(String fi, String fo) throws Exception {
        fileIn = fi;
        fileOut = fo;

        CompressionAlg decomp;

        try (IO.Bit.reader input = new IO.Bit.reader(fileIn)) {

            byte magicByte = (byte) input.readByte();

            int alg;
            if (magicByte == LZ78.MAGIC_BYTE) alg = 1;
            else if (magicByte == LZSS.MAGIC_BYTE) alg = 2;
            else if (magicByte == LZW.MAGIC_BYTE) alg = 3;
            else if (magicByte == JPEG.MAGIC_BYTE) alg = 4;
            else if (magicByte == Folder.MAGIC_BYTE) alg = 5;
            else throw new Exception("Fitxer a descomprimir invàlid.");

            if (!fileOut.endsWith(".ppm") && alg == 4) fileOut += ".ppm";

            stats = new Statistics();
            stats.setIniFileSize(fileIn);
            stats.setStartingTime();

            if (alg == 5) {
                Folder.decompress(fileOut, input);
            } else {
                try (IO.Byte.writer output = new IO.Byte.writer(fileOut)) {

                    switch (alg) {
                        case 1:
                            decomp = new LZ78();
                            break;
                        case 2:
                            decomp = new LZSS();
                            break;
                        case 3:
                            decomp = new LZW();
                            break;
                        case 4:
                            decomp = new JPEG((short)0);
                            break;
                        default:
                            throw new Exception("Fitxer a descomprimir invàlid.");
                    }
                    decomp.decompress(input, output);
                }
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
