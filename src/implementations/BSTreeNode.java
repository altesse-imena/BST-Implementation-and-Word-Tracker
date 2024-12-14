package implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * BSTreeNode.java
 *
 * A class representing a node in a Binary Search Tree (BST).
 * Each node stores an element, references to its left and right children,
 * and metadata to track occurrences of the element (file names and line numbers).
 * 
 * @param <E> The type of element stored in this node, which must be comparable.
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;

    private E element;                       // The data element stored in the node
    private BSTreeNode<E> left;              // Reference to the left child
    private BSTreeNode<E> right;             // Reference to the right child
    private Map<String, Set<Integer>> occurrences; // Metadata: file names and line numbers

    /**
     * Constructs a new BSTreeNode with the specified element.
     * Initializes the left and right children to null, and sets up an empty metadata map.
     * 
     * @param element The element to store in the node.
     */
    public BSTreeNode(E element) {
        this.element = element;
        this.left = null;
        this.right = null;
        this.occurrences = new HashMap<>();
    }

    /**
     * Retrieves the element stored in the node.
     * 
     * @return The element stored in the node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Sets the element stored in the node.
     * 
     * @param element The new element to store in the node.
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Retrieves the left child of the node.
     * 
     * @return The left child node, or null if no left child exists.
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child of the node.
     * 
     * @param left The node to set as the left child.
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Retrieves the right child of the node.
     * 
     * @return The right child node, or null if no right child exists.
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child of the node.
     * 
     * @param right The node to set as the right child.
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }

    /**
     * Adds an occurrence of the element in a specified file and line number.
     * 
     * @param fileName The name of the file where the element occurs.
     * @param lineNumber The line number where the element occurs in the file.
     */
    public void addOccurrence(String fileName, int lineNumber) {
        occurrences.computeIfAbsent(fileName, k -> new HashSet<>()).add(lineNumber);
    }

    /**
     * Retrieves the metadata (file names and line numbers) associated with the element.
     * 
     * @return A map where keys are file names and values are sets of line numbers.
     */
    public Map<String, Set<Integer>> getOccurrences() {
        return occurrences;
    }
}

