package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * A generic implementation of a Binary Search Tree (BST).
 * 
 * <p>This class provides standard operations like insertion, deletion, search, 
 * and tree traversal (inorder, preorder, and postorder). It ensures that elements
 * are stored in a sorted binary tree structure where elements in the left subtree 
 * are less than the root, and elements in the right subtree are greater.
 * 
 * @param <E> The type of elements stored in the tree, which must implement Comparable.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public BSTreeNode<E> getRoot() {
        if (root == null) 
            throw new NullPointerException("Tree is empty.");
        return root;
    }

    @Override
    public int getHeight() {
        return getHeightRecursive(root);
    }

    private int getHeightRecursive(BSTreeNode<E> node) {
        if (node == null) 
            return 0;
        return 1 + Math.max(getHeightRecursive(node.getLeft()), getHeightRecursive(node.getRight()));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) {
        if (entry == null) 
            throw new NullPointerException("Entry cannot be null.");
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) {
        if (entry == null) 
            throw new NullPointerException("Entry cannot be null.");
        return searchRecursive(root, entry);
    }

    private BSTreeNode<E> searchRecursive(BSTreeNode<E> node, E entry) {
        if (node == null) 
            return null;
        int comparison = entry.compareTo(node.getElement());
        if (comparison == 0) 
            return node;
        return comparison < 0 ? searchRecursive(node.getLeft(), entry) : searchRecursive(node.getRight(), entry);
    }

    @Override
    public boolean add(E newEntry) {
        if (newEntry == null) 
            throw new NullPointerException("Entry cannot be null.");
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }
        return addRecursive(root, newEntry);
    }

    private boolean addRecursive(BSTreeNode<E> node, E newEntry) {
        int comparison = newEntry.compareTo(node.getElement());
        if (comparison == 0) 
            return false; // No duplicates
        if (comparison < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            return addRecursive(node.getLeft(), newEntry);
        } else {
            if (node.getRight() == null) {
                node.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            return addRecursive(node.getRight(), newEntry);
        }
    }

    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) 
            return null;
        BSTreeNode<E> minNode = getMinNode(root);
        root = removeMinRecursive(root);
        size--;
        return minNode;
    }

    private BSTreeNode<E> getMinNode(BSTreeNode<E> node) {
        while (node.getLeft() != null) 
            node = node.getLeft();
        return node;
    }

    private BSTreeNode<E> removeMinRecursive(BSTreeNode<E> node) {
        if (node == null) 
            return null;
        if (node.getLeft() == null) 
            return node.getRight();
        node.setLeft(removeMinRecursive(node.getLeft()));
        return node;
    }

    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) 
            return null;
        BSTreeNode<E> maxNode = getMaxNode(root);
        root = removeMaxRecursive(root);
        size--;
        return maxNode;
    }

    private BSTreeNode<E> getMaxNode(BSTreeNode<E> node) {
        while (node.getRight() != null) 
            node = node.getRight();
        return node;
    }

    private BSTreeNode<E> removeMaxRecursive(BSTreeNode<E> node) {
        if (node == null) 
            return null;
        if (node.getRight() == null) 
            return node.getLeft();
        node.setRight(removeMaxRecursive(node.getRight()));
        return node;
    }

    @Override
    public Iterator<E> inorderIterator() {
        return new TreeIterator(TreeTraversalOrder.INORDER);
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new TreeIterator(TreeTraversalOrder.PREORDER);
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new TreeIterator(TreeTraversalOrder.POSTORDER);
    }

    private enum TreeTraversalOrder {
        INORDER, PREORDER, POSTORDER
    }

    private class TreeIterator implements Iterator<E> {
        private final Stack<BSTreeNode<E>> stack = new Stack<>();
        private BSTreeNode<E> current;

        public TreeIterator(TreeTraversalOrder order) {
            current = root;
            if (order == TreeTraversalOrder.INORDER) {
                pushLeft(current);
            } else if (order == TreeTraversalOrder.PREORDER) {
                if (current != null) stack.push(current);
            } else if (order == TreeTraversalOrder.POSTORDER) {
                pushPostorder(current);
            }
        }

        private void pushLeft(BSTreeNode<E> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }

        private void pushPostorder(BSTreeNode<E> node) {
            Stack<BSTreeNode<E>> tempStack = new Stack<>();
            if (node != null) tempStack.push(node);
            while (!tempStack.isEmpty()) {
                BSTreeNode<E> n = tempStack.pop();
                stack.push(n);
                if (n.getLeft() != null) tempStack.push(n.getLeft());
                if (n.getRight() != null) tempStack.push(n.getRight());
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            BSTreeNode<E> node = stack.pop();
            if (!stack.isEmpty() && stack.peek() == node.getRight()) {
                pushLeft(node.getRight());
            }
            return node.getElement();
        }
    }
}

