package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Stack;

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
        return new Iterator<E>() {
            private final Stack<BSTreeNode<E>> stack = new Stack<>();
            private BSTreeNode<E> current = root;

            {
                while (current != null) {
                    stack.push(current);
                    current = current.getLeft();
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) 
                    throw new NoSuchElementException();
                BSTreeNode<E> node = stack.pop();
                E data = node.getElement();
                if (node.getRight() != null) {
                    current = node.getRight();
                    while (current != null) {
                        stack.push(current);
                        current = current.getLeft();
                    }
                }
                return data;
            }
        };
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new Iterator<E>() {
            private final Stack<BSTreeNode<E>> stack = new Stack<>();

            {
                if (root != null) 
                    stack.push(root);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) 
                    throw new NoSuchElementException();
                BSTreeNode<E> node = stack.pop();
                E data = node.getElement();
                if (node.getRight() != null) 
                    stack.push(node.getRight());
                if (node.getLeft() != null) 
                    stack.push(node.getLeft());
                return data;
            }
        };
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new Iterator<E>() {
            private final Stack<BSTreeNode<E>> stack1 = new Stack<>();
            private final Stack<BSTreeNode<E>> stack2 = new Stack<>();

            {
                if (root != null) 
                    stack1.push(root);
                while (!stack1.isEmpty()) {
                    BSTreeNode<E> node = stack1.pop();
                    stack2.push(node);
                    if (node.getLeft() != null) 
                        stack1.push(node.getLeft());
                    if (node.getRight() != null) 
                        stack1.push(node.getRight());
                }
            }

            @Override
            public boolean hasNext() {
                return !stack2.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) 
                    throw new NoSuchElementException();
                return stack2.pop().getElement();
            }
        };
    }
}
