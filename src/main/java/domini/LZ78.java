package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    private static HashMap<String, Integer> compress_dict = new HashMap<String, Integer>();
    private static HashMap<Integer, String> decompress_dict = new HashMap<Integer, String>();

    public static void compress(IO.Char.reader input, IO.Bit.writer output) throws IOException {

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
                boolean [] bits_num = intToNBits(codnum, nbits);
                for(int j = bits_num.length-1; j >= 0; --j) output.write(bits_num[j]); //Escribe el codnum con los bits necesarios
                boolean[] bits_char = intToNBits((int)last_char, 16);
                for(int j = 15; j >= 0; --j) output.write(bits_char[j]);
                ++nchar;
            }
            chin = input.read();
        }
        //Si aun quedan letras por codificar estas ya estan en el diccionario, simplemente
        //se obtiene el value del diccionario y se escribe junto con un byte vacio, 8 bits a 0.
        if (newchar == false) {
            codnum = compress_dict.get(chars);
            int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
            boolean [] bits_num = intToNBits(codnum, nbits);
            for(int j = bits_num.length-1; j >= 0; --j) output.write(bits_num[j]);  //Escribe el codnum con los bits necesarios
            for(int i=0; i<16; ++i) output.write(false);
        }
        input.close();
    }

    private static int bits_needed(int n){
        if (n<=0) return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10)+1;
    }

    private static boolean[] intToNBits(int cInt, int n) {
        boolean[] bits = new boolean[n];
        for(int i = 0; i < n; ++i) {
            int aux = cInt % 2;
            if(aux == 1) bits[i] = true;
            cInt /= 2;
        }
        return bits;
    }

    public static void decompress(IO.Bit.reader input, IO.Char.writer output) throws IOException{

        int num = 1;
        int nchar=0;
        int number;
        boolean first;
        try {
            for (;;) {
                if (input.read()) first=true;
                else first=false;
                String charac="";
                int nbits = bits_needed(nchar);
                boolean[] n = new boolean[nbits];
                for (int i = 1; i<nbits; ++i) {
                    if (input.read()) n[i]=true;
                    else n[i]=false;
                }

                boolean[] character = new boolean[16];
                if (nbits==0){
                    character[0]=first;
                    for(int i=1; i<16; ++i){
                        if (input.read()) character[i]=true;
                        else character[i]=false;
                    }
                    number=bit_to_int(n);
                }else{
                    n[0]=first;
                    number=bit_to_int(n);
                    for(int i=0; i<16; ++i){
                        if (input.read()) character[i]=true;
                        else character[i]=false;
                    }
                }
                int last_char = bit_to_int(character);
                
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
            }

        }catch (EOFException e) {
            //EOF!
        }

        input.close();
    }

    public static int bit_to_int (boolean[] b){
        int result=0;
        for (int i=0; i<b.length; ++i) {
            result *=2;
            if (b[i]) ++result;
        }
        return result;
    }

}
