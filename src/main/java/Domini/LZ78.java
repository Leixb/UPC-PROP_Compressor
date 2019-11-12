package Domini;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    HashMap<String, Integer> dict = new HashMap<String, Integer>();

    public void LZ78_compressor(String filename) throws IOException {
        try (IO.reader input = new IO.reader(filename);
             IO.writer output = new IO.writer("Compressed_" + filename)) {

            String chars = "";
            int num = 0;

            int chin = input.read();
            while (chin != -1) {
                String charac = chars + (char) chin;
                if (dict.get(charac) == null) {
                    dict.put(charac, num);
                    ++num;
                    byte[] letter = charac.getBytes(); //String convertido a array de bytes
                    //Escribir la entrada del dict en el archivo comprimido
                    output.write(letter);
                    output.write((byte) num);
                    //o guardarlo para despues codificarlo en binario
                } else {
                    chars = charac;
                }
            }
        }
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
