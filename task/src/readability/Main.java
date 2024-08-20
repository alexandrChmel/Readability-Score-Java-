package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(args[0]));
        Scanner sc = new Scanner(System.in);
        System.out.println("The text is:");
        String text = scanner.nextLine();
        System.out.println(text + "\n");

        int words = text.split(" ").length;
        int sentences = text.split("[.?!]").length;
        int characters = text.replace(" ", "").length();
        int[] sylArr = countSyllables(text);
        int syllables = sylArr[0];
        int polysyllables = sylArr[1];

        System.out.printf("""
                Words: %d
                Sentences: %d
                Characters: %d
                Syllables: %d
                Polysyllables: %d
                Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all
                
                """, words, sentences, characters, syllables, polysyllables);

        String which = sc.next();

        switch (which){
            case "ARI":
                System.out.println(ari(characters, words, sentences));
                break;
            case "FK":
                System.out.println(fk(words, sentences, syllables));
                break;
            case "SMOG":
                System.out.println(smog(sentences, polysyllables));
                break;
            case "CL":
                System.out.println(cl(characters, words, sentences));
                break;
            case "all":
                System.out.println(ari(characters, words, sentences));
                System.out.println(fk(words, sentences, syllables));
                System.out.println(smog(sentences, polysyllables));
                System.out.println(cl(characters, words, sentences));
                break;
        }

        scanner.close();
    }

    public static String cl(int characters, int words, int sentences){
        double l = (double) characters / words * 100;
        double s = (double) sentences / words * 100;
        double score = 0.0588 * l - 0.296 * s - 15.8;
        int years = countYears(score);
        return String.format("Coleman–Liau index: %.2f (about %d-year-olds).", score, years) ;
    }

    public static String smog(int sentences, int polysyllables){
        double score = 1.043 * Math.sqrt(polysyllables * ((double) 30 / sentences)) + 3.1291;
        int years = countYears(score);
        return String.format("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).", score, years) ;
    }
    public static String fk (int words, int sentences, int syllables){
        double score = 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
        int years = countYears(score);
        return String.format("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).", score, years) ;
    }
    public static int countYears (double score){
        int intScore = (int) Math.ceil(score);
        if (intScore < 14){
            return intScore + 5;
        } else{
            return intScore + 8;
        }

    }
    public static int[] countSyllables(String text) {
        int count = 0;
        int countPoly = 0;
        text = text.toLowerCase();
        String vowels = "aeiouy";
        String[] words = text.split(" ");

        for (int i = 0; i < words.length; i++) {
            int thisCount = 0;
            for (int j = 0; j < words[i].length(); j++){

                char current = words[i].charAt(j);

                if (vowels.contains(String.valueOf(current))){
                    if (current != 'e' || j + 1 < words[i].length()){
                        count++;
                        thisCount++;
                        while(j + 1 < words[i].length() - 1 && vowels.contains(String.valueOf(words[i].charAt(j + 1)))){
                            j++;
                        }
                    }
                }

            }
            if (thisCount == 0){
                count++;
            }
            if (thisCount > 2){
                countPoly++;
            }
        }
        return new int[]{count, countPoly};
    }
    public static String ari(int characters, int words, int sentences){
        double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        int years = countYears(score);
        return String.format("Automated Readability Index: %.2f (about %d-year-olds).", score, years) ;
    }
}