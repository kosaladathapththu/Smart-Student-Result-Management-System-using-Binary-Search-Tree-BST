package app;

import bst.BinarySearchTree;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Student;

public class StudentUI extends Application {

    private final BinarySearchTree bst = new BinarySearchTree();

    private final TextField tfIndex = new TextField();
    private final TextField tfName  = new TextField();
    private final TextField tfPdsa  = new TextField();
    private final TextField tfSe    = new TextField();
    private final TextField tfDm2   = new TextField();

    private final TextArea output = new TextArea();

    @Override
    public void start(Stage stage) {
        tfIndex.setPromptText("Index No");
        tfName.setPromptText("Name");
        tfPdsa.setPromptText("PDSA");
        tfSe.setPromptText("SE");
        tfDm2.setPromptText("DM2");

        output.setEditable(false);
        output.setPrefHeight(250);

        Button btnAdd = new Button("Add");
        Button btnSearch = new Button("Search");
        Button btnDelete = new Button("Delete");
        Button btnShowAll = new Button("Show All (Sorted)");

        btnAdd.setOnAction(e -> addStudent());
        btnSearch.setOnAction(e -> searchStudent());
        btnDelete.setOnAction(e -> deleteStudent());
        btnShowAll.setOnAction(e -> showAll());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Index No"), 0, 0); form.add(tfIndex, 1, 0);
        form.add(new Label("Name"),    0, 1); form.add(tfName,  1, 1);
        form.add(new Label("PDSA"),    0, 2); form.add(tfPdsa,  1, 2);
        form.add(new Label("SE"),      0, 3); form.add(tfSe,    1, 3);
        form.add(new Label("DM2"),     0, 4); form.add(tfDm2,   1, 4);

        HBox buttons = new HBox(10, btnAdd, btnSearch, btnDelete, btnShowAll);

        VBox root = new VBox(12, form, buttons, new Label("Output"), output);
        root.setPadding(new Insets(15));

        stage.setTitle("Student Result Management System (BST)");
        stage.setScene(new Scene(root, 650, 520));
        stage.show();
    }

    private void addStudent() {
        String index = tfIndex.getText().trim();
        String name  = tfName.getText().trim();

        if (index.isEmpty() || name.isEmpty()) {
            write("Index and Name cannot be empty.");
            return;
        }
        if (bst.search(index) != null) {
            write("Student already exists: " + index);
            return;
        }

        Double pdsa = parseMark(tfPdsa.getText());
        Double se   = parseMark(tfSe.getText());
        Double dm2  = parseMark(tfDm2.getText());
        if (pdsa == null || se == null || dm2 == null) return;

        bst.insert(new Student(index, name, pdsa, se, dm2));
        write("Added: " + index);
        clearMarks();
    }

    private void searchStudent() {
        String index = tfIndex.getText().trim();
        if (index.isEmpty()) {
            write("Enter an Index No to search.");
            return;
        }
        Student s = bst.search(index);
        write(s == null ? "Not found: " + index : s.toString());
    }

    private void deleteStudent() {
        String index = tfIndex.getText().trim();
        if (index.isEmpty()) {
            write("Enter an Index No to delete.");
            return;
        }
        if (bst.search(index) == null) {
            write("Cannot delete. Not found: " + index);
            return;
        }
        bst.delete(index);
        write("Deleted: " + index);
    }

    private void showAll() {
        output.clear();
        for (Student s : bst.inorderList()) {
            output.appendText(s + "\n");
        }
        if (bst.inorderList().isEmpty()) write("No records.");
    }

    private Double parseMark(String text) {
        try {
            double v = Double.parseDouble(text.trim());
            if (v < 0 || v > 100) {
                write("Marks must be 0–100.");
                return null;
            }
            return v;
        } catch (Exception e) {
            write("Enter valid numeric marks (0–100).");
            return null;
        }
    }

    private void write(String msg) {
        output.appendText(msg + "\n");
    }

    private void clearMarks() {
        tfPdsa.clear(); tfSe.clear(); tfDm2.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
