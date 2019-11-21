import domini.*;

import java.io.File;

class AutoSelectionTest {
    public static void main(String[] args) {
        new File("generated/").mkdirs();
        final String[] fileIn = {"texts/Blank.txt","texts/DonQuijote.txt","texts/Large.txt"};
        final String fileComp = "generated/compressedFile.piz";
        final String fileOut = "generated/out.txt";

        final CtrlDomini.Alg[] alg = {CtrlDomini.Alg.LZWd,CtrlDomini.Alg.LZ78d,CtrlDomini.Alg.LZSSd};
        final String algName[] = {"LZW ","LZ78","LZSS"};

        for (int i=0; i<fileIn.length; ++i) {
            System.out.println("[" + (i+1) + "] Archivo: " + fileIn[i]);
            System.out.println("+-----------+--------+----------------+----------------+--------+----------+---------+");
            System.out.println("| Algoritmo | % comp |    vel comp    |   vel decomp   | t comp | t decomp | t total |");
            double maxPercent = 0;
            int bestPercent = 0;
            double maxSpeedComp = 0;
            int bestSpeedComp = 0;
            double maxSpeedDecomp = 0;
            int bestSpeedDecomp = 0;
            double minTimeComp = Double.POSITIVE_INFINITY;
            int bestTimeComp = 0;
            double minTimeDecomp = Double.POSITIVE_INFINITY;
            int bestTimeDecomp = 0;
            double minTotalTime = Double.POSITIVE_INFINITY;
            int bestTotalTime = 0;

            for (int j = 0; j < alg.length; ++j) {
                System.out.println("|-----------+--------+----------------+----------------+--------+----------+---------|");
                System.out.printf("|   %s    |", algName[j]);

                Statistics statsComp = comprimir(alg[j],fileIn[i],fileComp);
                Statistics statsDecomp = descomprimir(fileComp, fileOut);

                //HAY QUE CONTROLAR QUE SI DA ERROR NO HAGA ESTO (*)
                double percent = statsComp.getPercentageCompressed();
                double speedComp = statsComp.getSpeedCompressed();
                double speedDecomp = statsDecomp.getSpeedDecompressed();
                double timeComp = statsComp.getTime();
                double timeDecomp = statsDecomp.getTime();
                double totalTime = timeComp + timeDecomp;

                System.out.printf(" %.2f%% |  %.2fps  |  %.2fps  |  %.2fs  |  %.2fs  |  %.2fs  |\n", percent, speedComp, speedDecomp, timeComp, timeDecomp, totalTime);

                if (maxPercent <= percent) {
                    maxPercent = percent;
                    bestPercent = j;
                }
                if (maxSpeedComp <= speedComp) {
                    maxSpeedComp = speedComp;
                    bestSpeedComp = j;
                }
                if (maxSpeedDecomp <= speedDecomp) {
                    maxSpeedDecomp = speedDecomp;
                    bestSpeedDecomp = j;
                }
                if (minTimeComp >= timeComp) {
                    minTimeComp = timeComp;
                    bestTimeComp = j;
                }
                if (minTimeDecomp >= timeDecomp) {
                    minTimeDecomp = timeDecomp;
                    bestTimeDecomp = j;
                }
                if (minTotalTime >= totalTime) {
                    minTotalTime = totalTime;
                    bestTotalTime = j;
                }
                //(*)
            }

            System.out.println("+-----------+--------+----------------+----------------+--------+----------+---------+");
            System.out.println();
            System.out.println("Best results:");
            System.out.println(" - % comp: " + algName[bestPercent]);
            System.out.println(" - vel comp: " + algName[bestSpeedComp]);
            System.out.println(" - vel decomp: " + algName[bestSpeedDecomp]);
            System.out.println(" - t comp: " + algName[bestTimeComp]);
            System.out.println(" - t decomp: " + algName[bestTimeDecomp]);
            System.out.println(" - t total: " + algName[bestTotalTime]);
            System.out.println();
        }
    }

    private static Statistics comprimir(CtrlDomini.Alg alg, String fileIn, String fileOut) {
        try {
            return CtrlDomini.compress(alg, fileIn, fileOut, (short) 0);
        } catch (Exception e) {
            System.out.println("                        ERROR EN LA COMPRESIÓN                          |" + e.getMessage());
            return new Statistics();
        }
    }

    private static Statistics descomprimir(String fileIn, String fileOut) {
        try{
            return CtrlDomini.decompress(fileIn,fileOut);
        } catch (Exception e) {
            System.out.println("                       ERROR EN LA DESCOMPRESIÓN                        |" + e.getMessage());
            return new Statistics();
        }
    }
}