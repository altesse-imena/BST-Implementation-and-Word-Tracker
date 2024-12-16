package implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a single node in a Binary Search Tree (BST).
 * 
 * @param <E> The type of the element stored in the node. It must be comparable.
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;

    // The element stored in the node
    private E element;

    // References to the left and right child nodes
    private BSTreeNode<E> left;
    private BSTreeNode<E> right;

    // A map to track where this element appears in files and line numbers
    private Map<String, Set<Integer>> occurrences;

    /**
     * Creates a new BSTreeNode with a given element.
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
     * Gets the element stored in this node.
     * 
     * @return The element stored in the node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Updates the element stored in this node.
     * 
     * @param element The new element to store in the node.
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Gets the left child of this node.
     * 
     * @return The left child node, or null if there is no left child.
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child of this node.
     * 
     * @param left The node to set as the left child.
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Gets the right child of this node.
     * 
     * @return The right child node, or null if there is no right child.
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child of this node.
     * 
     * @param right The node to set as the right child.
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }

    /**
     * Adds a record of where this element appears in a file and line number.
     * 
     * @param fileName   The name of the file where the element is found.
     * @param lineNumber The line number in the file where the element is found.
     */
    public void addOccurrence(String fileName, int lineNumber) {
        occurrences.computeIfAbsent(fileName, k -> new HashSet<>()).add(lineNumber);
    }

    /**
     * Gets the map of file names and line numbers where this element occurs.
     * 
     * @return A map with file names as keys and sets of line numbers as values.
     */
    public Map<String, Set<Integer>> getOccurrences() {
        return occurrences;
    }
}

