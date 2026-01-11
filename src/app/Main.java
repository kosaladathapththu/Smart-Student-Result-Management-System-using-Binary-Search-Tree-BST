package app;

import bst.BinarySearchTree;
import model.Student;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final BinarySearchTree bst = new BinarySearchTree();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== Student Result Management System (BST) =====");
            System.out.println("1. Add Student Result");
            System.out.println("2. Search Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Display All Results (Sorted)");
            System.out.println("5. Show At-Risk Students");
            System.out.println("6. Show Scholarship Eligible Students");
            System.out.println("7. Show Top N Students (Ranking)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");


            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    searchStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    displayAll();
                    break;

                    case "5":
                    showAtRisk();
                    break;
                case "6":
                    showScholarshipEligible();
                    break;
                case "7":
                    showTopN();
                    break;
                case "0":
                    System.out.println("Program terminated.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ---------------- MENU FUNCTIONS ----------------

    private static void addStudent() {
        System.out.print("Student Index Number: ");
        String index = sc.nextLine().trim();

        System.out.print("Student Name: ");
        String name = sc.nextLine().trim();

        double pdsa = readDouble("PDSA Marks: ");
        double se = readDouble("SE Marks: ");
        double dm2 = readDouble("DM2 Marks: ");

        Student student = new Student(index, name, pdsa, se, dm2);
        bst.insert(student);

        System.out.println("Student record added successfully.");
    }

    private static void searchStudent() {
        System.out.print("Enter Index Number to search: ");
        String index = sc.nextLine().trim();

        Student student = bst.search(index);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private static void deleteStudent() {
        System.out.print("Enter Index Number to delete: ");
        String index = sc.nextLine().trim();

        bst.delete(index);
        System.out.println("Delete operation completed.");
    }

    private static void displayAll() {
        System.out.println("\n--- Student Results (Sorted by Index Number) ---");
        bst.displayInOrder();
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    private static void showAtRisk() {
    List<Student> list = bst.inorderList();
    boolean found = false;

    System.out.println("\n--- At-Risk Students (Average < 40) ---");
    for (Student s : list) {
        if (s.isAtRisk()) {
            System.out.println(s);
            found = true;
        }
    }
    if (!found) System.out.println("No at-risk students found.");
}

private static void showScholarshipEligible() {
    List<Student> list = bst.inorderList();
    boolean found = false;

    System.out.println("\n--- Scholarship Eligible Students ---");
    for (Student s : list) {
        if (s.isScholarshipEligible()) {
            System.out.println(s);
            found = true;
        }
    }
    if (!found) System.out.println("No eligible students found.");
}

private static void showTopN() {
    int n = (int) readDouble("Enter N (e.g., 5 or 10): ");

    List<Student> list = new ArrayList<>(bst.inorderList());
    if (list.isEmpty()) {
        System.out.println("No records to rank.");
        return;
    }

    // Sort by average DESC (highest first)
    list.sort(Comparator.comparingDouble(Student::getAverage).reversed());

    System.out.println("\n--- Top " + n + " Students (By Average) ---");
    for (int i = 0; i < Math.min(n, list.size()); i++) {
        System.out.println((i + 1) + ". " + list.get(i));
    }
}

}
