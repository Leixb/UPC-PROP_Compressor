package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    private static HashMap<String, Integer> compress_dict = new HashMap<String, Integer>();
    private static HashMap<Integer, String> decompress_dict = new HashMap<Integer, String>();
    public final static byte MAGIC_BYTE = 0x78;

    public static void compress(IO.Char.reader input, IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        String chars = "";
        int num = 1;
        int nchar = 0;
        int codnum = 0;
        boolean newchar = true;
        int chin = input.read();
        while (chin != -1) {
            char last_char = (char) chin;
            String charac = chars + last_char;
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
                int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
                BitSetL bs_num = new BitSetL(codnum,nbits);
                output.write(bs_num);
                BitSetL bs_char = new BitSetL((int)last_char,16);
                output.write(bs_char);
                ++nchar;
            }
            chin = input.read();
        }
        //Si aun quedan letras por codificar estas ya estan en el diccionario, simplemente
        //se obtiene el value del diccionario y se escribe junto con un byte vacio, 8 bits a 0.
        if (newchar == false) {
            codnum = compress_dict.get(chars);
            int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
            BitSetL bs_num = new BitSetL(codnum,nbits);
            //for (int i=0; i<nbits; ++i) output.write(bs_num.get(i)); //Escribe el codnum con los bits necesarios
            output.write(bs_num);
            for(int i=0; i<16; ++i) output.write(false);
        }
        input.close();
    }

    private static int bits_needed(int n){
        if (n<=0) return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10)+1;
    }


    public static void decompress(IO.Bit.reader input, IO.Char.writer output) throws IOException{
        input.readByte();

        int num = 1;
        int nchar=0;
        int number;
        String charac = "";
        int first_char=input.readBitSet(16).asInt();
        charac+= (char) first_char;
        output.write((char)first_char);
        decompress_dict.put(num,charac);
        ++num;
        ++nchar;
        int nbits = bits_needed(nchar);
        try {
            for (;;) {
                number = input.readBitSet(nbits).asInt();

                int last_char = input.readBitSet(16).asInt();
                charac = "";

                if (number > 0){
                    if (last_char>0) charac = decompress_dict.get(number) + (char) last_char;
                    else charac = decompress_dict.get(number);
                    output.write(charac);
                } else {
                    charac += (char) last_char;
                    output.write((char) last_char);
                }
                decompress_dict.put(num, charac);
                ++num;
                ++nchar;
                nbits = bits_needed(nchar);
            }

        }catch (EOFException e) {
            //EOF!
        }

        input.close();
    }

}
