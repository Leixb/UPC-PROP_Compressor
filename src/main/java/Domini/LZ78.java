package Domini;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    private static HashMap<String, Byte> compress_dict = new HashMap<String, Byte>();
    private static HashMap<Byte, String> decompress_dict = new HashMap<Byte, String>();

    public static void compressor(String filename) throws IOException {
        try (IO.reader input = new IO.reader(filename);
             IO.writer output = new IO.writer("texts/out.txt")) {

            String chars = "";
            byte num = 1;
            int nchar=0;
            byte codnum=0;
            boolean newchar = true;

            int chin = input.read();
            while (chin != -1) {
                char last_char = (char) chin;
                String charac = chars + last_char;
                if (compress_dict.get(charac)!=null) {
                    newchar=false;
                    chars=charac;
                } else {
                    if (newchar) {
                        codnum=0;
                    }
                    else {
                        newchar=true;
                        codnum=compress_dict.get(chars);
                        chars = "";
                    }
                    compress_dict.put(charac,num);
                    ++num;

                    byte letter = (byte)last_char; //String convertido a bytes
                    int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
                    BitSetL bs = new BitSetL (codnum, nbits);
                    //for(int i=0; i<bs.l; ++i) output.writeBit(bs.get(i)); //Escribe el codnum con los bits necesarios
                    output.write((byte) (codnum + 0x30));
                    output.write(letter); //Escribe el string como un vector de bytes en el archivo comprimido
                    ++nchar;
                }
                chin = input.read();
            } //Si aun quedan letras por codificar estas ya estan en el diccionario, simplemente
              //se obtiene el value del diccionario y se escribe junto con un byte vacio, 8 bits a 0.
            if (newchar==false){
                codnum=compress_dict.get(chars);
                int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
                BitSetL bs = new BitSetL (codnum, nbits);
                //for(int i=0; i<bs.l; ++i) output.writeBit(bs.get(i)); //Escribe el codnum con los bits necesarios
                output.write((byte) (codnum + 0x30));
                output.write((byte)0x30);
            }
        }
    }

    private static int bits_needed(int n){
        if (n<=0) return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10)+1;
    }

    public static void decompressor(String filename) throws IOException{
        try (IO.reader input = new IO.reader(filename);
             IO.writer output = new IO.writer("texts/dec_out.txt")) {

            //String chars = "";
            //byte num = 1;
            //int nchar=0;
            //byte codnum=0;
            //boolean newchar = true;
            //int number=0;
            String charac="";
            //boolean bitin = input.readBit();
            int number = input.read();
            while (number!=-1) { //cambiar para que sea a nivel de un bit
                /*int nbits = bits_needed(nchar);
                for (int i = nbits; i > 0; --i) {
                    if (input.readBit()) {
                        int bint = 1;
                        for (int j = 0; j < i; ++j) bint *= 2;
                        number += bint;
                    }
                }*/
                //int chin = input.read();
                int last_char = input.read();
                if ((char)last_char==0x30) {
                    //output.write((byte) 0x40);
                    charac=decompress_dict.get((byte) number);
                    output.write(charac.getBytes());
                }
                else {
                    //int last_char = input.read();
                    if ((char)number > 0x30) {
                        output.write((byte)number);
                        output.write(decompress_dict.get((byte)number).getBytes());
                        charac = decompress_dict.get((byte) number) + (char) last_char;
                        output.write((byte) 0x40);
                        output.write(charac.getBytes());
                    } else {
                        charac += (char) last_char;
                        output.write(charac.getBytes());
                    }
                    byte nume=1;
                    output.write(nume);
                    //output.write(nume);
                    output.write(charac.getBytes());
                    decompress_dict.put(nume, charac);
                    charac="";
                    ++nume;
                    //++nchar;
                    number = 0;
                }
                number = input.read();
            }
        }
    }

}
