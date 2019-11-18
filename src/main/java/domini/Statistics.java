package domini;

import java.io.File;

/**
 * @author ***REMOVED*** Pons / ***REMOVED*** / ***REMOVED*** Plasencia
 *
 * @brief Genera estadísticas de compresión/descompresión.
 */

public class Statistics {
    private long startingTime;
    private long endingTime;
    private long iniFileSize;
    private long finFileSize;

    /**
     * @brief Constructora de la clase
     */
    public Statistics () {
        startingTime = 0;
        endingTime = 0;
        iniFileSize = 0;
        finFileSize = 0;
    }

    /**
     * @brief Setter de startingTime
     */
    public void setStartingTime () {
        startingTime = System.currentTimeMillis();
    }

    /**
     * @brief Setter de endingTime
     */
    public void setEndingTime () {
        endingTime = System.currentTimeMillis();
    }

    /**
     * @brief Setter de iniFileSize
     * @param filename nombre del archivo de entrada cuyo tamaño se quiere consultar
     */
    public void setIniFileSize (final String filename) {
        iniFileSize = new File(filename).length();
    }

    /**
     * @brief Setter de finFileSize
     * @param filename nombre del archivo de salida cuyo tamaño se quiere consultar
     */
    public void setFinFileSize(final String filename) {
        finFileSize = new File(filename).length();
    }

    /**
     * @brief Getter de la duración de la compresión/descompresión
     * @return Tiempo de ejecución de la compresión/descompresión en segundos
     */
    public double getTime () {
        return (double)(endingTime - startingTime)/1000.0;
    }

    /**
     * @brief Getter del tamaño del fichero de entrada
     * @return Tamaño del archivo de entrada en bytes
     */
    public long getIniFileSize() {
        return iniFileSize;
    }

    /**
     * @brief Getter del tamaño del fichero de salida
     * @return Tamaño del archivo de salida en bytes
     */
    public long getFinFileSize() {
        return finFileSize;
    }

    /**
     * @brief Getter de los bytes comprimidos
     * @return Cantidad de bytes comprimidos
     */
    private long getBytesCompressed () {
        return iniFileSize - finFileSize;
    }

    /**
     * @brief Getter de los bytes descomprimidos
     * @return Cantidad de bytes descomprimidos
     */
    private long getBytesDecompressed () {
        return finFileSize - iniFileSize;
    }

    /**
     * @brief Getter del porcentage de compresión
     * @return Porcentage de compresión
     */
    public double getPercentageCompressed () {
        return (double) getBytesCompressed()*100.0 / iniFileSize;
    }

    /**
     * @brief Getter del porcentage de descompresión
     * @return Porcentage de descompresión
     */
    public double getPercentageDecompressed () {
        return (double) getBytesDecompressed()*100.0 / iniFileSize;
    }

    /**
     * @brief Getter de la velocidad de compresión
     * @return Velocidad de compresión en bytes por segundo
     */
    public double getSpeedCompressed () {
        return (double)iniFileSize / getTime();
    }

    /**
     * @brief Getter de la velocidad de descompresión
     * @return Velocidad de descompresión en bytes por segundo
     */
    public double getSpeedDecompressed () {
        return (double)finFileSize / getTime();
    }
}
