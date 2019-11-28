/**
 * @file ./src/main/java/domini/LZ78.java
 * @author ***REMOVED***
*/
package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;


/**
 * @brief Compresor y descompresor de archivos de texto con LZ78
 */
public final class LZ78{

    private LZ78() {}

    //Declaración de los HashMaps de Compression y Decompression
    private static HashMap<String, Integer> compress_dict = new HashMap<String, Integer>();
    private static HashMap<Integer, String> decompress_dict = new HashMap<Integer, String>();

    /**Byte escrito al principio del archivo comprimido para saber
     * con que algoritmo ha sido comprimido
     * para poder asi descomprimirlo después con el mismo
     */
    public final static byte MAGIC_BYTE = 0x78;

    /// Pseudo EOF
    private static char EOF = 0x7FFF;;



    /**
     * @brief Llama al metodo compress de la clase LZ78 y le pasa como parametros inputFilename y outputFilename
     * @param inputFilename Path y nombre del archivo de entrada que se desea comprimir
     * @param outputFilename Path y nombre del archivo de salida comprimido
     * @throws IOException Excepcion en caso de pasar como parametro inputFilename un archivo inexistente
     */
    public static void compress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Char.reader input = new IO.Char.reader(inputFilename);
                IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            compress(input, output);
        }
    }

    /**
     * @brief Comprime el archivo pasado por parametro input y escribe la compresion en el parametro output
     * @param input Archivo de entrada que se desea comprimir
     * @param output Archivo de salida comprimido
     * @throws IOException Excepcion en caso de intentar leer un dato inexistente
     */
    private static void compress(final IO.Char.reader input, final IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        String chars = "";
        int num = 1;//Numero que representa el value de la siguiente entrada en el HashMap
        int nchar = 0;//Num de codificaciones para saber cuantos bits representan la key del HashMap de descompresion
        int codnum = 0;//Entrada del HashMap necesaria para codificar los chars que se esta leyendo
        boolean newchar = true;//Indicador de si el char leído es nuevo o no
        int chin = input.read();
        while (chin != -1) {
            final char last_char = (char) chin;
            final String charac = chars + last_char;
            if (compress_dict.get(charac) != null) {
                newchar = false;
                chars = charac;
            } else {
                if (newchar) {
                    codnum = 0;
                } else {
                    newchar = true;
                    codnum = compress_dict.get(chars);
                    chars = "";
                }
                compress_dict.put(charac, num);
                ++num;
                final int nbits = bits_needed(nchar); // Numero de bits en que hay que codificar el nchar
                final BitSetL bs_num = new BitSetL(codnum, nbits);
                output.write(bs_num);
                final BitSetL bs_char = new BitSetL((int) last_char, 16);
                output.write(bs_char);
                ++nchar;
            }
            chin = input.read();
        }
        /*
         * Si aun quedan letras por codificar estas ya estan en el diccionario, simplemente se obtiene el value
         * del diccionario y se escribe junto con un char vacio, 16 bits a 0.
         */
        if (newchar == false) {
            codnum = compress_dict.get(chars);
            final int nbits = bits_needed(nchar); // Numero de bits en que hay que codificar el nchar
            final BitSetL bs_num = new BitSetL(codnum, nbits);
            output.write(bs_num);
            for (int i = 0; i < 16; ++i)
                output.write(false);
        }
        if (nchar>0) {
            ++nchar;
            final int nbits = bits_needed(nchar);
            final BitSetL bs_num = new BitSetL(codnum, nbits);
            output.write(bs_num);
        }
        output.write(EOF);
    }

    /**
     * @brief  Devuelve el numero de bits necesarios para codificar en base 2 el int pasado por parametro
     * @param n  Numero integer del que se va a calcular cuantos bits son necesarios para codificarlo en base 2
     * @return
     */
    private static int bits_needed(final int n) {
        if (n <= 0)
            return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10) + 1;
    }

    /**
     * @brief Llama al metodo decompress de la clase LZ78 y le pasa como parametros inputFilename y outputFilename
     * @param inputFilename Path y nombre del archivo de entrada que se desea descomprimir
     * @param outputFilename Path y nombre del archivo de salida descomprimido
     * @throws IOException Excepcion en caso de pasar como parametro inputFilename un archivo inexistente
     */
    public static void decompress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
                IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            decompress(input, output);
        }
    }

    /**
     * @brief Descomprime el archivo comprimido pasado por input y escribe la descompresion por el parametro output
     * @param input  Archivo comprimido que se desea descomprimir
     * @param output  Archivo de salida descomprimido
     * @throws IOException  Excepcion en caso de intentar leer un dato inexistente
     */
    private static void decompress(final IO.Bit.reader input, final IO.Char.writer output) throws IOException {
        input.readByte();

        int num = 1;//Numero que representa el value de la siguiente entrada en el HashMap
        int nchar = 0;//Num de codificaciones para saber cuantos bits representan la key del HashMap de descompresion
        int number;//Key del HashMap codificada antes de cada char en el archivo comprimido
        String charac = "";
        /*
         * El primer char codificado no tiene ninguna key codifiacada delante por lo que se trata
         * primero y por separado
         */
        try {
            final int first_char = input.readChar();
            if (first_char != EOF) {
                charac += (char) first_char;
                output.write((char) first_char);
                decompress_dict.put(num, charac);
                ++num;
                ++nchar;
                int nbits = bits_needed(nchar);
                /*
                 * Tras descodificar el primer char se van leiedo bit a bit las keys y values del HashMap de descompresion
                 * codificados en el archivo comprimido. Siempre que haya bits por codificar se continua leyendo.
                 */
                try {

                    number = input.readBitSet(nbits).asInt();
                    int last_char = input.readChar();

                    while (last_char != EOF) {

                        charac = "";
                        //Si se referencie a alguna entrada del HashMap se concatena el value String con el char leido
                        if (number > 0) {
                            //Si el ultimo char que se ha leido son 16 bits a 0, indica el final del archivo
                            if (last_char > 0)
                                charac = decompress_dict.get(number) + (char) last_char;
                            else
                                charac = decompress_dict.get(number);
                            output.write(charac);
                        } else {
                            charac += (char) last_char;
                            output.write((char) last_char);
                        }
                        decompress_dict.put(num, charac);
                        ++num;
                        ++nchar;
                        nbits = bits_needed(nchar);
                        number = input.readBitSet(nbits).asInt();
                        last_char = input.readChar();
                    }
                } catch (EOFException e) {
                    //EOF!
                }
            }
        } catch (EOFException e){
            //EOF!
        }

    }

}