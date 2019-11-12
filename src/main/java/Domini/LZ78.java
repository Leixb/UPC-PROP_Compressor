package Domini;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    HashMap<String, Byte> dict = new HashMap<String, Byte>();

    public void LZ78_compressor(String filename) throws IOException {
        try (IO.reader input = new IO.reader(filename);
             IO.writer output = new IO.writer("Compressed_" + filename)) {

            String chars = "";
            byte num = 1;
            int nchar=1;
            byte codnum=0;
            boolean newchar = true;

            int chin = input.read();
            while (chin != -1) {
                String charac = chars + (char) chin;
                if (dict.get(charac)!=null) {
                    newchar=false;
                    chars=charac;
                } else {
                    if (newchar) codnum=0;
                    else {
                        newchar=true;
                        codnum=dict.get(chars);
                        chars = "";
                    }
                    dict.put(charac,num);
                    ++num;

                    byte[] letter = charac.getBytes(); //String convertido a array de bytes
                    int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el value
                    BitSetL bs = new BitSetL (codnum, nbits);
                    for(int i=0; i<bs.l; ++i) output.writeBit(bs.get(i)); //Escribe el codnum con los bits necesarios
                    output.write(letter); //Escribe el string como un vector de bytes en el archivo comprimido
                    ++nchar;
                }
                chin = input.read();
            }
        }
    }

    private static int bits_needed(int n){
        return (int) (Math.log(n) / Math.log(2) + 1e-10);
    }
/*
    public void LZ78_decompressor(String filename) throws IOException{
        //En caso que se haya codificado en binario descodificarlo antes de leerlo
        try (IO.reader input = new IO.reader(filename);
             IO.writer output = new IO.writer("Decompressed_" + filename)) {

            String chars = "";
            int chin = input.read();
            while (chin != -1) {
                String charac = chars + (char) chin;
                if (charac == "0") {
                    //Escribir el siguiente caracter por la salida ya que no referencia ninguna entrada del diccionario
                } else {
                    chars=dict[charac];
                }
            }
        }
    }
    */
}
