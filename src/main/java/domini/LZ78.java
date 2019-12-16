/**
 * @file ./src/main/java/domini/LZ78.java
 * @author Alex González
*/
package domini;

import persistencia.IO;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @brief Compresor y descompresor de archivos de texto con LZ78
 */
public final class LZ78{

    private LZ78() {}

    //Declaración del HashMap de Decompression
    private static HashMap<Integer, ArrayList<Byte>> decompress_dict = new HashMap<Integer, ArrayList<Byte>>();

    /**Byte escrito al principio del archivo comprimido para saber
     * con que algoritmo ha sido comprimido
     * para poder asi descomprimirlo después con el mismo
     */
    public final static byte MAGIC_BYTE = 0x78;


    /**
     * @brief Classe Pair para poder crear, en este caso, pairs de Integers y bytes
     * @param <First> Tipo de la key del pair
     * @param <Second> Tipo del value del pair
     */
    private static class Pair<First,Second>{
        First first;
        Second second;
        Pair(First first, Second second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * @brief Clase para crear un Nodo del arbol, el cual tien un indice de tipo int y 256 hijos
     */
    private static class Nodo {
        Nodo[] hijos = new Nodo[256];
        int index;

        Nodo(){
            index = 0;
        }
    }

    /**
     * Clase para inicializar un arbol
     */
    private static class Tree{
        Nodo arrel = new Nodo();
        Nodo actual = arrel;
        int codnum = 0;
        boolean last=false;
        int padre = 0;
        ArrayList<Pair<Integer, Byte>> arrayList;

        /**
         * @brief Metodo tipo boolean para crearlo mientras se lee el archivo a comprimir
         * @param input Archivo de entrada que se desea comprimir
         * @return Devuelve true en caso de overflow, se tenga que seguir leyendo y crear otro arbol
         * @throws IOException Excepcion en caso de intentar leer un dato inexistente
         */
        boolean CrearTree(final IO.Byte.reader input) throws IOException{
            Nodo actual = arrel;
            arrayList = new ArrayList<>();
            int chin_aux = input.read();
            byte pre=0;
            byte chin;
            while (chin_aux!=-1 && codnum<1400000) { //Cuando codnum llega a 1.400.000 se produce overflow
                chin = (byte) chin_aux;
                if (actual.hijos[chin+128]!= null){
                    padre = actual.index;
                    actual = actual.hijos[chin+128];
                    last=true;
                }else {
                    actual.hijos[chin+128] = new Nodo();
                    arrayList.add(new Pair<Integer, Byte>(actual.index,chin));
                    ++codnum;
                    actual.hijos[chin+128].index = codnum;
                    actual = arrel;
                    last=false;
                }
                pre = chin;
                if (codnum <1400000) chin_aux = input.read();
            }
            if (last){
                arrayList.add(new Pair<Integer, Byte>(padre,pre));
            }
            if (codnum>=1400000) return true;
            else  return false;
        }
    }

    /**
     * @brief Llama al metodo compress de la clase LZ78 y le pasa como parametros inputFilename y outputFilename
     * @param inputFilename Path y nombre del archivo de entrada que se desea comprimir
     * @param outputFilename Path y nombre del archivo de salida comprimido
     * @throws IOException Excepcion en caso de pasar como parametro inputFilename un archivo inexistente
     */
    public static void compress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename);
             IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            compress(input, output);
        }
    }

    /**
     * @brief Llamada para escribir en el archivo comprimido el array pasado como parametro
     * @param arrayList Array que contiene la codificacion del archivo original
     * @param output Salida de tipo IO.Bit.writer para escribir en el archivo comprimido
     * @throws IOException
     */
    private static void print_array (ArrayList<Pair <Integer, Byte>> arrayList, final IO.Bit.writer output) throws IOException{
        int nbyte = 0;//Num de codificaciones para saber cuantos bits representan la key del HashMap de descompresion
        for (Pair<Integer, Byte> i : arrayList){
            output.write(false); //Señaliza que después hay un byte, no un overflow ni un end of file
            final int nbits = bits_needed(nbyte); // Numero de bits en que hay que codificar el byte
            final BitSetL bs_num = new BitSetL(i.first, nbits);
            output.write(bs_num);
            final BitSetL bs_byte = new BitSetL(i.second, 8);
            output.write(bs_byte);
            ++nbyte;
        }
    }

    /**
     * @brief Comprime el archivo pasado por parametro input y escribe la compresion en el parametro output
     * @param input Archivo de entrada que se desea comprimir
     * @param output Archivo de salida comprimido
     * @throws IOException Excepcion en caso de intentar leer un dato inexistente
     */
    private static void compress(final IO.Byte.reader input, final IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);
        boolean loop = true;
        Tree arbre = new Tree();
        loop = arbre.CrearTree(input);
        print_array(arbre.arrayList, output);
        while (loop){
            output.write(true); //Señaliza que hay un overflow o un end of file
            output.write(false); //Señaliza el overflow en el archivo comprimido
            arbre = new Tree();
            loop = arbre.CrearTree(input);
            print_array(arbre.arrayList, output);
        }

        output.write(true); //Señaliza que hay un overflow o un end of file
        output.write(true); //Señaliza el fianl del archivo comprimido
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
             IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
            decompress(input, output);
        }
    }

    /**
     * @brief Descomprime el archivo comprimido pasado por input y escribe la descompresion por el parametro output
     * @param input  Archivo comprimido que se desea descomprimir
     * @param output  Archivo de salida descomprimido
     * @throws IOException  Excepcion en caso de intentar leer un dato inexistente
     */
    private static void decompress(final IO.Bit.reader input, final IO.Byte.writer output) throws IOException {
        input.readByte();

        int num = 1;//Numero que representa el value de la siguiente entrada en el HashMap
        int nbyte = 0;//Num de codificaciones para saber cuantos bits representan la key del HashMap de descompresion
        int number=0;//Key del HashMap codificada antes de cada byte en el archivo comprimido
        ArrayList<Byte> array = new ArrayList<>();
        boolean of_eof=false;
        boolean eof = false;
        boolean of = false;
        /*
         * El primer byte codificado no tiene ninguna key codifiacada delante por lo que se trata
         * primero y por separado
         */
        try {
            of_eof = input.read();
            byte first_byte = (byte) input.readByte();
            if (!of_eof) {
                output.write(first_byte);
                array.add(first_byte);
                decompress_dict.put(num, array);
                ++num;
                ++nbyte;
                int nbits = bits_needed(nbyte);
                /*
                 * Tras descodificar el primer byte se van leiedo bit a bit las keys y values del HashMap de descompresion
                 * codificados en el archivo comprimido. Siempre que haya bits por codificar se continua leyendo.
                 */
                try {
                    of_eof = input.read();
                    if (!of_eof) {
                        number = input.readBitSet(nbits).asInt();
                    }
                    else eof = input.read();
                    while (!eof) {
                        if (of_eof){
                            decompress_dict.clear();
                            array = new ArrayList<>();
                            num = 1;
                            nbyte = 0;
                            of_eof = input.read();
                            first_byte = (byte)input.readByte();
                            output.write(first_byte);
                            array.add(first_byte);
                            decompress_dict.put(num, array);
                        }else{
                            byte last_byte = (byte)input.readByte();
                            /*
                             * Si se referencie a alguna entrada del HashMap se concatena el value de la entrada
                             * con el nuevo byte leído .
                             */
                            if (number > 0) {
                                array = new ArrayList<>();
                                array.addAll(decompress_dict.get(number));
                            } else {
                                array = new ArrayList<>();
                            }
                            array.add(last_byte);
                            for (Byte i : array) output.write(i);
                            decompress_dict.put(num, array);
                        }
                        ++num;
                        ++nbyte;
                        nbits = bits_needed(nbyte);
                        of_eof = input.read();
                        if (!of_eof) number = input.readBitSet(nbits).asInt();
                        else eof = input.read();
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
