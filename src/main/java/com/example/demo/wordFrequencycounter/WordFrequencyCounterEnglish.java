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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFrequencyCounterEnglish {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Word Frequency Counter");
            System.out.println("1. Enter text manually");
            System.out.println("2. Read from a text file");
            System.out.print("Choose an option (1 or 2): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            String text = "";
            // /Users/sothea007/Desktop/AEU\'s\ Master\ Program/AEU\ MSIT\ Programming\
            // Principles/App/src/test01.txt
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
                case 2:
                    // "/Users/sothea007/Desktop/AEU's Master Program/AEU MSIT Programming
                    // Principles/App.java/'/Users/sothea007/Desktop/AEU\'s Master Program/AEU MSIT
                    // Programming Principles/App.java/src/test01.txt"
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine().trim();

                    // Remove any surrounding quotes
                    if (filePath.startsWith("'") && filePath.endsWith("'")) {
                        filePath = filePath.substring(1, filePath.length() - 1);
                    }
                    if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
                        filePath = filePath.substring(1, filePath.length() - 1);
                    }

                    // Remove any escape characters
                    filePath = filePath.replace("\\", "");

                    try {
                        Path path = Paths.get(filePath).toAbsolutePath().normalize();
                        System.out.println("Attempting to read: " + path);

                        if (!Files.exists(path)) {
                            System.err.println("\nERROR: File not found at:");
                            System.err.println(path);
                            System.err.println("\nPlease verify the path is correct.");
                            System.err.println("Try dragging the file into Terminal.");
                            return;
                        }

                        text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        System.err.println("Error reading file: " + e.getMessage());
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid option");
                    return;
            }

            Map<String, Integer> frequencyMap = countWordFrequencies(text);

            // Display results in different formats
            System.out.println("\nResults:");
            System.out.println("\n1. Raw frequency count (unsorted):");
            displayRawFrequency(frequencyMap);

            System.out.println("\n2. Sorted by word (alphabetical):");
            displaySortedByWord(frequencyMap);

            System.out.println("\n3. Sorted by frequency (descending):");
            displaySortedByFrequency(frequencyMap);

            System.out.println("\n4. Filtered (words appearing more than once):");
            displayFilteredResults(frequencyMap, 1);
        }
    }

    private static Map<String, Integer> countWordFrequencies(String text) {
        // Split text into words, ignoring case and punctuation
        return Stream.of(text.split("\\W+"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toMap(
                        word -> word,
                        word -> 1,
                        Integer::sum,
                        HashMap::new));
    }

    private static void displayRawFrequency(Map<String, Integer> frequencyMap) {
        frequencyMap.forEach((word, count) -> System.out.printf("%-15s: %d%n", word, count));
    }

    private static void displaySortedByWord(Map<String, Integer> frequencyMap) {
        frequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue()));
    }

    private static void displaySortedByFrequency(Map<String, Integer> frequencyMap) {
        frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue()));
    }

    private static void displayFilteredResults(Map<String, Integer> frequencyMap, int minFrequency) {
        frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() > minFrequency)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue()));
    }
}