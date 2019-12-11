/**
 * @file ./src/main/java/presentacio/CtrlPresentacio.java
 * @author Albert Mercadé Plasencia / Aleix Boné
*/
package presentacio;

import java.text.DecimalFormat;
import java.util.Scanner;

import domini.CtrlDomini;
import domini.Statistics;

/**
 * @brief Imprime por pantalla
 */
public class CtrlPresentacio {
    CtrlDomini cd = new CtrlDomini();

    /**
     * @brief Da a elegir al usuario el algoritmo para la compresión y le pide el nombre del archivo a comprimir y
     *        el nombre del archivo comprimido que luego pasa al CtrlDomini.
     */
    public void compress(int alg, String fileIn, String fileOut, short qualityJPEG) {
        try {
            cd.compress(alg, fileIn, fileOut, qualityJPEG);
        } catch (Exception e) {
            System.out.println("Error en la compressió:" + e.getMessage());
        }
    }

    /**
     * @brief Le pide al usuario el nombre del archivo comprimido y del archivo destino y se los pasa a CtrlDomini.
     */
    public void decompress(String fileIn, String fileOut) {
        try{
            cd.decompress(fileIn,fileOut);
        } catch (Exception e) {
            System.out.println("Error en la descompressió:" + e.getMessage());
        }
    }

    public String getTime() {
        return cd.getTime();
    }

    public String getDeflated() {
        return cd.getDeflated();
    }

    public String getSpeedCompress () {
        return cd.getSpeedCompress();
    }

    public String getInflated() {
        return cd.getInflated();
    }

    public String getSpeedDecompress() {
        return cd.getSpeedDecompress();
    }

    public String getFileOut() {
        return cd.getFileOut();
    }
}
