package appDomain;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

import java.io.*;
import java.util.Scanner;

public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser";
    private BSTree<String> wordTree;

    public WordTracker() {
        this.wordTree = loadTree();
    }

    public void processFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber++;
                String[] words = line.split("\\W+");
                for (String word : words) {
                    word = word.toLowerCase();
                    if (!word.isEmpty()) {
                        if (!wordTree.contains(word)) {
                            wordTree.add(word);
                        }
                        BSTreeNode<String> node = wordTree.search(word);
                        node.addOccurrence(fileName, lineNumber);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
    }

    public void saveTree() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
        } catch (IOException e) {
            System.err.println("Error saving tree: " + e.getMessage());
        }
    }

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

