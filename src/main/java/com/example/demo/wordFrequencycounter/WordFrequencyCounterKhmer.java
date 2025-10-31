/* 2025-08-09 Saturday
 * # Assignment – Word Frequency Counter

- Create a comprehensive word frequency analyzer that demonstrates multiple programming principles and data structures.

## Core Requirements:
- Input: Accept either user input or read from a text file
- Processing: Count word frequency ignoring case and punctuation
- Output: Display results in multiple formats
- Data Structures: Use HashMap for frequency counting, ArrayList for sorting
- Lambda Usage: Implement filtering and sorting using Lambda expressions
- Reflection: How do principles like DRY and clarity improve your design?
 */

package com.example.demo.wordFrequencycounter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrequencyCounterKhmer {
    public static void main(String[] args) {
        // Force UTF-8 output
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setProperty("console.encoding", "UTF-8");
            System.setProperty("file.encoding", "UTF-8");
            Locale.setDefault(Locale.US);

            // Display the menu and get user input
            displayMenu();
            // Read the user's choice
            Scanner scanner = new Scanner(System.in, "UTF-8");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            String text = "";

            // Switch the user's choice
            switch (choice) {

                case 1:
                    System.out.println("Enter your text (press Enter twice to finish):");
                    StringBuilder inputText = new StringBuilder();
                    String line;
                    while (!(line = scanner.nextLine()).isEmpty()) {
                        inputText.append(line).append("\n");
                    }
                    text = inputText.toString();
                    break;
                // Read from a file
                case 2:
                    text = getFileInput(scanner);
                    if (text == null)
                        return; // Exit if file reading failed
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }
            // Process the text and get word frequencies
            Map<String, Integer> wordFrequencies = countWordFrequencies(text);

            // Display results in different formats
            System.out.println("\nWord Frequencies:");
            displayResults(wordFrequencies);
            scanner.close();
        } catch (Exception e) {
            

            System.err.println("Initialization error: " + e.getMessage());
        }

    }

    /**
     * Reads text from a file, handling various input formats and cleaning the path.
     * 
     * @param scanner The Scanner object for user input
     * @return The text read from the file, or null if an error occurs
     */
    private static String getFileInput(Scanner scanner) {
        System.out.print("Enter file path (you can drag & drop file here): ");
        String rawPath = scanner.nextLine().trim();

        // Clean the path string
        String cleanedPath = cleanFilePath(rawPath);

        try {
            Path fullPath = Paths.get(cleanedPath).toAbsolutePath().normalize();
            System.out.println("Reading file: " + fullPath);

            if (!Files.exists(fullPath)) {
                System.err.println("Error: File does not exist at " + fullPath);
                return null;
            }

            return new String(Files.readAllBytes(fullPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cleans the file path by removing surrounding quotes, escape characters, and
     * replacing ~ with the user's home directory.
     * 
     * @param rawPath The raw file path input by the user
     * @return The cleaned file path
     */
    private static String cleanFilePath(String rawPath) {
        // Remove surrounding quotes if present
        String cleaned = rawPath.replaceAll("^['\"]|['\"]$", "");

        // Handle escaped characters
        cleaned = cleaned.replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");

        // Replace ~ with home directory
        cleaned = cleaned.replace("~", System.getProperty("user.home"));

        return cleaned;
    }

    /**
     * Displays the Menu in list
     * Word Frequency Counter, Choose input method
     */
    public static void displayMenu() {
        System.out.println("Word Frequency Counter");
        System.out.println("Choose input method:");
        System.out.println("1. Enter text manually");
        System.out.println("2. Read from a file");
        System.out.print("Your choice: ");
    }

    /**
     * Counts word frequencies in the given text, supporting both English and Khmer
     * 
     * @param text The input text to analyze
     * @return A map of words to their frequencies
     */
    public static Map<String, Integer> countWordFrequencies(String text) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        // This pattern matches:
        // 1. English words (letters and apostrophes) OR
        // 2. Complete Khmer syllables (consonant + optional vowel/diacritics)
        String englishWordPattern = "[a-zA-Z']+";
        String khmerWordPattern = "[\\u1780-\\u17FF\\u19E0-\\u19FF]+";
        String wordPattern = englishWordPattern + "|" + khmerWordPattern;

        Pattern pattern = Pattern.compile(wordPattern);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String word = matcher.group();
            if (!word.isEmpty()) {
                // Only lowercase English words
                String normalizedWord = word.matches(englishWordPattern) ? word.toLowerCase() : word;
                frequencyMap.put(normalizedWord, frequencyMap.getOrDefault(normalizedWord, 0) + 1);
            }
        }

        return frequencyMap;
    }

    /**
     * Displays the results in multiple formats
     * 
     * @param wordFrequencies The map of word frequencies
     */
    public static void displayResults(Map<String, Integer> wordFrequencies) {
        // Calculate maximum word length for alignment
        int maxWordLength = wordFrequencies.keySet().stream()
                .mapToInt(word -> word.length()) // Approximate length
                .max()
                .orElse(15) + 2;

        System.out.println("\n1. Raw frequency count:");
        wordFrequencies.forEach((word, count) -> System.out.printf("%-" + maxWordLength + "s: %d%n", word, count));

        System.out.println("\n2. Sorted alphabetically:");
        wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("%-" + maxWordLength + "s: %d%n",
                        entry.getKey(), entry.getValue()));

        System.out.println("\n3. Sorted by frequency (highest first):");
        wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("%-" + maxWordLength + "s: %d%n",
                        entry.getKey(), entry.getValue()));

        System.out.println("\n4. Top 10 most frequent words:");
        wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> System.out.printf("%-" + maxWordLength + "s: %d%n",
                        entry.getKey(), entry.getValue()));

        System.out.println("\n5. Words appearing only once:");
        wordFrequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("%-" + maxWordLength + "s: %d%n",
                        entry.getKey(), entry.getValue()));
    }

}
