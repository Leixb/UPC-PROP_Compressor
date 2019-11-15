package presentacio;

import domini.Statistics;

public class CtrlPresentacio {
    public static void printStatsCompress(Statistics stats) {
        System.out.println();
        System.out.println("Compressió amb èxit!");
        System.out.println("Temps trigat: " + stats.getTime() + "s");
        System.out.print("Desinflat: " + stats.getPercentageCompressed() + "% ");
        double iniFileSize = stats.getIniFileSize();
        if(iniFileSize < 1000) System.out.print(" (" +(double)Math.round(iniFileSize*100)/100+" kB => ");
        else System.out.print(" (" +(double)Math.round(iniFileSize/10)/100+" MB => ");
        double finFileSize = stats.getFinFileSize();
        if(finFileSize < 1000) System.out.print((double)Math.round(finFileSize*100)/100+" kB)\n");
        else System.out.print((double)Math.round(finFileSize/10)/100+" MB)\n");
        double compSpeed = stats.getSpeedCompressed();
        if(compSpeed < 1000) System.out.println("Velocitat mitjana compressió : " + (double)Math.round(compSpeed*100)/100 + " kBps");
        else System.out.println("Velocitat mitjana compressió : " + (double)Math.round(compSpeed/10)/100 + " MBps");
        System.out.println();
    }

    public static void printStatsDecompress(Statistics stats) {
        System.out.println();
        System.out.println("Descompressió amb èxit!");
        System.out.println("Temps trigat: " + stats.getTime() + "s");
        System.out.print("Inflat: " + stats.getPercentageDecompressed() + "%");
        double iniFileSize = stats.getIniFileSize();
        if(iniFileSize < 1000) System.out.print(" (" +(double)Math.round(iniFileSize*100)/100+" kB => ");
        else System.out.print(" (" +(double)Math.round(iniFileSize/10)/100+" MB => ");
        double finFileSize = stats.getFinFileSize();
        if(finFileSize < 1000) System.out.print((double)Math.round(finFileSize*100)/100+" kB)\n");
        else System.out.print((double)Math.round(finFileSize/10)/100+" MB)\n");
        double decompSpeed = stats.getSpeedDecompressed();
        if(decompSpeed < 1000) System.out.println("Velocitat mitjana descompressió : " + (double)Math.round(decompSpeed*100)/100 + " kBps");
        else System.out.println("Velocitat mitjana descompressió : " + (double)Math.round(decompSpeed/10)/100 + " MBps");
        System.out.println();
    }
}
