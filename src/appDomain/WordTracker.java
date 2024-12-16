package appDomain;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

import java.io.*;
import java.util.Scanner;

/**
 * WordTracker tracks the occurrences of words in text files, including the file names and line numbers where they appear.
 * The data is stored in a binary search tree for efficient organization and retrieval.
 */
public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser"; // File to save/load the word tree
    private BSTree<String> wordTree; // Binary search tree to store words and their occurrences

    /**
     * Constructor initializes the WordTracker by loading a saved tree or creating a new one.
     */
    public WordTracker() {
        this.wordTree = loadTree();
    }

    /**
     * Processes a text file, extracting words and tracking their occurrences in the tree.
     * 
     * @param fileName The name of the file to process.
     */
    public void processFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber++;
                String[] words = line.split("\\W+"); // Split by non-word characters
                for (String word : words) {
                    word = word.toLowerCase(); // Normalize to lowercase
                    if (!word.isEmpty()) {
                        if (!wordTree.contains(word)) {
                            wordTree.add(word); // Add new word to the tree
                        }
                        BSTreeNode<String> node = wordTree.search(word); // Find the node for the word
                        node.addOccurrence(fileName, lineNumber); // Track where the word appears
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
    }

    /**
     * Saves the current state of the word tree to a file for future use.
     */
    public void saveTree() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
        } catch (IOException e) {
            System.err.println("Error saving tree: " + e.getMessage());
        }
    }

    /**
     * Loads the word tree from a file if it exists; otherwise, creates a new tree.
     * 
     * @return The loaded or newly created tree.
     */
    private BSTree<String> loadTree() {
        File file = new File(REPOSITORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (BSTree<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading tree: " + e.getMessage());
            }
        }
        return new BSTree<>();
    }

    /**
     * The main method processes command-line arguments and generates reports based on user input.
     * 
     * @param args Command-line arguments specifying input file, report type, and optional output file.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java WordTracker <input.txt> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        String inputFile = args[0];
        String option = args[1];
        String outputFile = args.length == 4 && args[2].equals("-f") ? args[3] : null;

        WordTracker tracker = new WordTracker();
        tracker.processFile(inputFile);

        // Generate report based on the option
        StringBuilder report = new StringBuilder();
        switch (option) {
            case "-pf": // Print words and file names
                Iterator<String> inorderIteratorPF = tracker.wordTree.inorderIterator();
                while (inorderIteratorPF.hasNext()) {
                    String word = inorderIteratorPF.next();
                    BSTreeNode<String> node = tracker.wordTree.search(word);
                    report.append(word).append(" -> ").append(node.getOccurrences().keySet()).append("\n");
                }
                break;
            case "-pl": // Print words, file names, and line numbers
                Iterator<String> inorderIteratorPL = tracker.wordTree.inorderIterator();
                while (inorderIteratorPL.hasNext()) {
                    String word = inorderIteratorPL.next();
                    BSTreeNode<String> node = tracker.wordTree.search(word);
                    node.getOccurrences().forEach((file, lines) -> {
                        report.append(word).append(" -> ").append(file).append(": ").append(lines).append("\n");
                    });
                }
                break;
            case "-po": // Print words, file names, line numbers, and frequencies
                Iterator<String> inorderIteratorPO = tracker.wordTree.inorderIterator();
                while (inorderIteratorPO.hasNext()) {
                    String word = inorderIteratorPO.next();
                    BSTreeNode<String> node = tracker.wordTree.search(word);
                    node.getOccurrences().forEach((file, lines) -> {
                        report.append(word).append(" -> ").append(file).append(": ").append(lines)
                                .append(", Frequency: ").append(lines.size()).append("\n");
                    });
                }
                break;
            default:
                System.err.println("Invalid option: " + option);
                return;
        }

        // Output report
        if (outputFile != null) {
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                writer.write(report.toString());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println(report);
        }

        tracker.saveTree();
    }
}

