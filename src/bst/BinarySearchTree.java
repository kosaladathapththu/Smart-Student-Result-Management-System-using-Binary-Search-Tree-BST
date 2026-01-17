package bst;

import model.Student;
import java.util.ArrayList;
import java.util.List;


public class BinarySearchTree {
    private Node root;

    public void insert(Student student) {
        root = insertRec(root, student);
    }

    public Node insertRec(Node current, Student student) {
        if (current == null) return new Node(student);

        int cmp = student.getIndexNo().compareTo(current.data.getIndexNo());
        if (cmp < 0) current.left = insertRec(current.left, student);
        else if (cmp > 0) current.right = insertRec(current.right, student);
        else current.data = student; // replace if same key
        return current;
    }

    public Student search(String indexNo) {
        Node node = searchRec(root, indexNo);
        return node == null ? null : node.data;
    }

    private Node searchRec(Node current, String indexNo) {
        if (current == null) return null;

        int cmp = indexNo.compareTo(current.data.getIndexNo());
        if (cmp == 0) return current;
        if (cmp < 0) return searchRec(current.left, indexNo);
        return searchRec(current.right, indexNo);
    }

    public void delete(String indexNo) {
    root = deleteRec(root, indexNo);
}

private Node deleteRec(Node current, String indexNo) {
    if (current == null) return null;

    int cmp = indexNo.compareTo(current.data.getIndexNo());

    if (cmp < 0) {
        current.left = deleteRec(current.left, indexNo);
    } else if (cmp > 0) {
        current.right = deleteRec(current.right, indexNo);
    } else {
        // Case 1: no child
        if (current.left == null && current.right == null) {
            return null;
        }

        // Case 2: one child
        if (current.left == null) return current.right;
        if (current.right == null) return current.left;

        // Case 3: two children
        Node successor = findMin(current.right);
        current.data = successor.data;
        current.right = deleteRec(
                current.right,
                successor.data.getIndexNo()
        );
    }
    return current;
}

public boolean update(Student updated) {
    Node node = searchRec(root, updated.getIndexNo());
    if (node == null) return false;
    node.data = updated; // replace student object (BST key unchanged)
    return true;
}


private Node findMin(Node node) {
    while (node.left != null) {
        node = node.left;
    }
    return node;
}

public void displayInOrder() {
    inorderRec(root);
}

public void clear() {
    root = null;
}

private void inorderRec(Node current) {
    if (current == null) return;

    inorderRec(current.left);
    System.out.println(current.data);
    inorderRec(current.right);
}

public List<Student> inorderList() {
    List<Student> list = new ArrayList<>();
    inorderToList(root, list);
    return list;
}

private void inorderToList(Node current, List<Student> list) {
    if (current == null) return;
    inorderToList(current.left, list);
    list.add(current.data);
    inorderToList(current.right, list);
}


}
