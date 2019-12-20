/**
 * @file ./src/main/java/presentacio/CtrlPresentacio.java
 * @author ***REMOVED***
*/
package presentacio;

import domini.CtrlDomini;

/**
 * @brief Imprime por pantalla
 */
public class CtrlPresentacio {
    private CtrlDomini cd;
    private static CtrlPresentacio instance = new CtrlPresentacio();

    private CtrlPresentacio() {
        cd = CtrlDomini.getInstance();
    }

    public static CtrlPresentacio getInstance() {
        return instance;
    }

    /**
     * @brief Da a elegir al usuario el algoritmo para la compresión y le pide el nombre del archivo a comprimir y
     *        el nombre del archivo comprimido que luego pasa al CtrlDomini.
     */
    public void compress(int alg, String fileIn, String fileOut, short qualityJPEG) throws Exception {
        cd.compress(alg, fileIn, fileOut, qualityJPEG);
    }

    /**
     * @brief Le pide al usuario el nombre del archivo comprimido y del archivo destino y se los pasa a CtrlDomini.
     */
    public void decompress(String fileIn, String fileOut) throws Exception {
            cd.decompress(fileIn,fileOut);
    }

    public String getTime() {
        return cd.getTime();
    }

    String getDeflated() {
        return cd.getDeflated();
    }

    String getSpeedCompress() {
        return cd.getSpeedCompress();
    }

    String getInflated() {
        return cd.getInflated();
    }

    String getSpeedDecompress() {
        return cd.getSpeedDecompress();
    }

    public String getFileOut() {
        return cd.getFileOut();
    }
}
