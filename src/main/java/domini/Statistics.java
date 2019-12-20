/**
 * @file ./src/main/java/domini/Statistics.java
 * @author ***REMOVED***
*/
package domini;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @brief Genera estadísticas de compresión/descompresión.
 */
public class Statistics {

    /// Tiempo de inicio
    private long startingTime;

    /// Tiempo de finalización
    private long endingTime;

    /// Tamaño inicial
    private long iniFileSize;

    /// Tamaño final
    private long finFileSize;

    /** Constructora vacía Statistics */
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
     *
     * @param filename nombre del archivo de entrada cuyo tamaño se quiere consultar
     * @throws IOException Lanza cualquier excepcion generada al acceder al archivo
     */
    public void setIniFileSize (final String filename) throws IOException {
        File file = new File(filename);
        if (file.isFile()) iniFileSize = file.length();
        else if (file.isDirectory()){
            // carpeta
            iniFileSize = Files.walk(Paths.get(filename))
              .filter(p -> p.toFile().isFile())
              .mapToLong(p -> p.toFile().length())
              .sum();
        }
    }

    /**
     * @brief Setter de finFileSize
     *
     * @param filename nombre del archivo de salida cuyo tamaño se quiere consultar
     * @throws IOException Lanza cualquier excepcion generada al acceder al archivo
     */
    public void setFinFileSize(final String filename) throws IOException {
        File file = new File(filename);
        if (file.isFile()) finFileSize = file.length();
        else if (file.isDirectory()){
            // carpeta
            finFileSize = Files.walk(Paths.get(filename))
              .filter(p -> p.toFile().isFile())
              .mapToLong(p -> p.toFile().length())
              .sum();
        }
    }

    /**
     * @brief Getter de la duración de la compresión/descompresión
     *
     * @return Tiempo de ejecución de la compresión/descompresión en segundos
     */
    public double getTime () {
        return (double)(endingTime - startingTime)/1000.0;
    }

    /**
     * @brief Getter del tamaño del fichero de entrada
     *
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
