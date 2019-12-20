/**
 * @file ./src/main/java/domini/CtrlDomini.java
 * @author ***REMOVED***
 */
package domini;

import persistencia.IO;

import java.io.IOException;
import java.io.File;
import java.text.DecimalFormat;

/**
 * @brief Controlador del dominio
 */
public class CtrlDomini {
    private String fileIn;
    private String fileOut;
    private Statistics stats;
    private static CtrlDomini instance = null;

    /** Constructora vacía CtrlDomini */
    private CtrlDomini () {}

    public static CtrlDomini getInstance() {
        if(instance == null) instance = new CtrlDomini();
        return instance;
    }

    /**
     * @brief Dado un algoritmo, el nombre fichero de entrada y el nombre del fichero comprimido, ejecuta la compresión con el algoritmo pertienente
     *
     * @param alg algoritmo con el que comprimir
     * @param fi nombre del archivo a comprimir
     * @param fo nombre del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @return Devuelve las estadisticas generadas para la compresión
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    public void compress(int alg, String fi, String fo, Short quality) throws IOException {
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

    /**
     * @brief Dado un algoritmo, el objeto de lectura de la entrada y el objeto de escritura del comprimido, ejecuta la compresión con el algoritmo pertienente
     *
     * @param alg algoritmo con el que comprimir
     * @param input objeto de lectura del archivo a comprimir
     * @param output objeto de escritura del archivo comprimido
     * @param quality calidad de compresión para el JPEG
     * @throws IOException Lanza cualquier excepción generada al comprimir
     */
    void compress(int alg, IO.Byte.reader input, IO.Bit.writer output, Short quality) throws IOException {
        CompressionAlg comp;
            if (alg == 0) {
                String filename = input.getFilename();
                if(filename.endsWith(".ppm")) {
                    comp = new JPEG((short) 90);
                } else {
                    if (stats.getIniFileSize() > 1048576) comp = new LZW(); // 1 MiB
                    else comp = new LZ78();
                }
            } else if (alg == 1) comp = new LZ78();
              else if (alg == 2) comp = new LZSS();
              else if (alg == 3) comp = new LZW();
              else if (alg == 4) comp = new JPEG(quality);
              else throw new RuntimeException("No s'hauria de poder arribar aqui.");
             
        output.write(comp.getMagicByte());
        comp.compress(input, output);
    }

    /**
     * @brief Dado un archivo comprimido y el nombre para el archivo descomprimido, descomprime el archivo usando el mismo algoritmo con el que se comprimió
     *
     * @param fi nombre del fichero comprimido
     * @param fo nombre del fichero descomprimido
     * @return Estadisticas generadas durante la descompresión
     * @throws IOException Lanza cualquier excepción generada al descomprimir
     */
    public void decompress(String fi, String fo) throws IOException {
        fileIn = fi;
        fileOut = fo;

        try (IO.Bit.reader input = new IO.Bit.reader(fileIn)) {
            byte magicByte = (byte) input.readByte();

            stats = new Statistics();
            stats.setIniFileSize(fileIn);
            stats.setStartingTime();

            if (magicByte == Folder.MAGIC_BYTE) {
                Folder.decompress(fileOut, input);
            } else {
                if (!fileOut.endsWith(".ppm") && magicByte == JPEG.MAGIC_BYTE) fileOut += ".ppm";
                try (IO.Byte.writer output = new IO.Byte.writer(fileOut)) {
                    decompress(input, output, magicByte);
                }
            }
        }

        stats.setEndingTime();
        stats.setFinFileSize(fileOut);
    }

    /**
     * @brief Dado un objeto de lectura del archivo comprimido, el objeto de escritura para el archivo descomprimido y un magicbyte, descomprime el archivo usando el algoritmo identificado por el magicbyte
     *
     * @param input objeto de lectura del fichero comprimido
     * @param output objeto de escritura del fichero descomprimido
     * @param magicByte byte que identifica el algoritmo a utilizar
     * @throws IOException Lanza cualquier excepción generada al descomprimir
     */
    void decompress(IO.Bit.reader input, IO.Byte.writer output, byte magicByte) throws IOException {
        CompressionAlg decomp;

        if(magicByte == 0) magicByte = (byte)input.readByte();

        if (magicByte == LZ78.MAGIC_BYTE) decomp = new LZ78();
        else if (magicByte == LZSS.MAGIC_BYTE) decomp = new LZSS();
        else if (magicByte == LZW.MAGIC_BYTE) decomp = new LZW();
        else if (magicByte == JPEG.MAGIC_BYTE) decomp = new JPEG((short)0);
        else throw new MagicByteException(magicByte);

        decomp.decompress(input, output);
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
     *
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

    /**
     * @brief Excepción de MagicByte
     */
    public static class MagicByteException extends IOException {
        private static final long serialVersionUID = 89896782363268431L;

        MagicByteException(byte b) {
            super(String.format("Unknown magic byte: 0x%02x", b));
        }
    }
}
