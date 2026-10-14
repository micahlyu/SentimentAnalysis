/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author micah
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SentimentAnalysis {

    private static final String SENTIMENT_FILE =
            "sentiments.txt";

    private static final int INITIAL_CAPACITY = 100;

    public static void main(String[] args) {

        HashTable<String, Integer> sentiments =
                new HashTable<>(INITIAL_CAPACITY);

        if (!loadSentiments(sentiments)) {
            return;
        }

        Scanner keyboard = new Scanner(System.in);

        int wordCount = 0;
        int totalSentiment = 0;

        String previousWord = null;

        System.out.println("Enter text:");

        while (keyboard.hasNextLine()) {

            String input = keyboard.nextLine();

            if (input.trim().equalsIgnoreCase("END")) {
                break;
            }

            String cleaned =
                    input.replaceAll(
                            "[^a-zA-Z ]", "")
                            .toLowerCase()
                            .trim();

            if (cleaned.isEmpty()) {
                continue;
            }

            String[] words =
                    cleaned.split("\\s+");

            for (String word : words) {

                wordCount++;

                // Check the individual word.
                Integer wordScore =
                        sentiments.get(word);

                if (wordScore != null) {
                    totalSentiment += wordScore;
                }

                // Check the previous word and
                // current word as a phrase.
                if (previousWord != null) {

                    String phrase =
                            previousWord
                                    + " "
                                    + word;

                    Integer phraseScore =
                            sentiments.get(phrase);

                    if (phraseScore != null) {
                        totalSentiment
                                += phraseScore;
                    }
                }

                previousWord = word;
            }
        }

        double average;

        if (wordCount == 0) {
            average = 0.0;
        } else {
            average =
                    (double) totalSentiment
                            / wordCount;
        }

        System.out.println();
        System.out.println(
                "Words: " + wordCount);

        System.out.println(
                "Sentiment: "
                        + totalSentiment);

        System.out.printf(
                "Overall: %.2f%n",
                average);

        keyboard.close();
    }

    /**
     * Loads each word or phrase and its
     * sentiment score from sentiments.txt.
     */
    private static boolean loadSentiments(
            HashTable<String, Integer> sentiments) {

        try (Scanner fileInput =
                     new Scanner(
                             new File(
                                     SENTIMENT_FILE))) {

            while (fileInput.hasNextLine()) {

                String line =
                        fileInput.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts =
                        line.split(",", 2);

                if (parts.length != 2) {
                    continue;
                }

                String phrase =
                        parts[0]
                                .trim()
                                .toLowerCase();

                try {
                    int score =
                            Integer.parseInt(
                                    parts[1].trim());

                    sentiments.put(
                            phrase,
                            score);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Skipping invalid "
                                    + "sentiment line: "
                                    + line);
                }
            }

            return true;

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Could not find "
                            + SENTIMENT_FILE
                            + ".");

            return false;
        }
    }
}
