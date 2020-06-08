import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FindingGenes {
    public static int findStopCodon(String dna, int startIndex, String stopCodon){
        int currIndex = dna.indexOf(stopCodon, startIndex + 3);

        while (currIndex != -1) {
            if ((currIndex - startIndex) % 3 == 0) {
                return currIndex;
            } else {
                currIndex = dna.indexOf(stopCodon, currIndex + 1);
            }
        }
        return -1;
    }

    public static String findGene(String dna, int where) {
        int startIndex = dna.indexOf("ATG", where);

        if (startIndex == -1) {
            return "";
        }

        int taaIndex = findStopCodon(dna, startIndex, "TAA");
        int tagIndex = findStopCodon(dna, startIndex, "TAG");
        int tgaIndex = findStopCodon(dna, startIndex, "TGA");

        int minIndex = 0;

        if (taaIndex == -1 || tgaIndex != -1 && tgaIndex < taaIndex) {
            minIndex = tgaIndex;
        } else {
            minIndex = taaIndex;
        }

        if (minIndex == -1 || tagIndex != -1 && tagIndex < minIndex) {
            minIndex = tagIndex;
        }

        if (minIndex == -1) {
            return "";
        }
        return dna.substring(startIndex, minIndex + 3);
    }

    public void printAllGenes(String dna) {
        int startIndex = 0;

        while(true) {
            String currentGene = findGene(dna, startIndex);
            if (currentGene.isEmpty()) {
                break;
            }
            System.out.println("currentGene: " + currentGene);
            startIndex = dna.indexOf(currentGene, startIndex) + currentGene.length();
        }
    }

    public static List<String> getAllGenes(String dna) {
        List<String> geneList = new ArrayList<String>();
        int startIndex = 0;

        while(true) {
            String currentGene = findGene(dna, startIndex);

            if (currentGene.isEmpty()) {
                return geneList;
            }
            geneList.add(currentGene);
            startIndex = dna.indexOf(currentGene, startIndex) + currentGene.length();
        }
    }

    public static float cgRatio(String dna) {
        int len = dna.length();
        int count = 0;

        for (int i = 0; i < len; i++) {
            if (dna.charAt(i) == 'C' || dna.charAt(i) == 'G') {
                count++;
            }
        }
        return (float)count / len;
    }

    public static int countCTG(String dna) {
        int count = 0;
        int currIndex = 0;
        String toCount = "CTG";

        while ((currIndex = dna.indexOf(toCount, currIndex)) != -1) {
            count++;
            currIndex++;
        }
        return count;
    }

    public static int findLongest(List<String> sr) {
        int maxLength = 0;

        for (String s: sr) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    public static void processGenes(List<String> sr) {
        int countLong = 0;
        int countCG = 0;

        // print all the Strings in sr that are longer than 60 characters
        for (String s: sr) {
            if (s.length() > 60) {
                countLong++;
            }
        }

        // print the number of Strings in sr that are longer than 60 characters
        System.out.println("Number of genes in DNA that are longer than 60 characters is " + countLong);

        // count the Strings in sr whose C-G-ratio is higher than 0.35 (and optionally print it)
        for (String s: sr) {
            if (cgRatio(s) > 0.35) {
                //System.out.println("C-G-ratio is higher than 0.35 " + s);
                countCG++;
            }
        }

        // print the number of strings in sr whose C-G-ratio is higher than 0.35
        System.out.println("Number of genes in DNA whose C-G-ratio is higher than 0.35 is " + countCG);

        // print the length of the longest gene
        System.out.println("Length of the longest gene in DNA is " + findLongest(sr));
    }

    public static void main(String args[]) throws IOException {
        InputStream is = new FileInputStream("src/main/dna.txt");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String dna = sb.toString();

        System.out.println("Given data is: " + dna);

        List<String> genes = getAllGenes(dna);

        processGenes(genes);
        System.out.println("In total " + genes.size() + " genes");
        System.out.println("\"CTG\" occurs " + countCTG(dna) + " times");
    }
}
