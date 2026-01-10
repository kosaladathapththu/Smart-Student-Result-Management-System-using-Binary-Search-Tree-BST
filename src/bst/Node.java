package bst;

import model.Student;

public class Node {
    public Student data;
    public Node left;
    public Node right;

    public Node(Student data) {
        this.data = data;
    }
}
