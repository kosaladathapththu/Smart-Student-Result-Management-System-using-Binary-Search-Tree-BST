package app;

import bst.BinarySearchTree;
import model.Student;

import java.io.*;
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
            System.out.println("8. Update Student Marks");
            System.out.println("9. Save to CSV");
            System.out.println("10. Load from CSV");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": addStudent(); break;
                case "2": searchStudent(); break;
                case "3": deleteStudent(); break;
                case "4": displayAll(); break;
                case "5": showAtRisk(); break;
                case "6": showScholarshipEligible(); break;
                case "7": showTopN(); break;
                case "8": updateStudent(); break;
                case "9": saveToCsv(); break;
                case "10": loadFromCsv(); break;
                case "0":
                    System.out.println("Program terminated.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Student Index Number: ");
        String index = sc.nextLine().trim();

        if (index.isEmpty()) {
            System.out.println("Index number cannot be empty.");
            return;
        }

        if (bst.search(index) != null) {
            System.out.println("A student with this index already exists.");
            return;
        }

        System.out.print("Student Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        double pdsa = readMark("PDSA Marks: ");
        double se = readMark("SE Marks: ");
        double dm2 = readMark("DM2 Marks: ");

        bst.insert(new Student(index, name, pdsa, se, dm2));
        System.out.println("Student record added successfully.");
    }

    private static void searchStudent() {
        System.out.print("Enter Index Number to search: ");
        String index = sc.nextLine().trim();

        if (index.isEmpty()) {
            System.out.println("Index number cannot be empty.");
            return;
        }

        Student student = bst.search(index);
        System.out.println(student == null ? "Student not found." : student);
    }

    private static void updateStudent() {
        System.out.print("Enter Index Number to update: ");
        String index = sc.nextLine().trim();

        if (index.isEmpty()) {
            System.out.println("Index number cannot be empty.");
            return;
        }

        Student existing = bst.search(index);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Current Record: " + existing);

        System.out.print("New Name (press Enter to keep same): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = existing.getName();

        double pdsa = readMark("New PDSA Marks: ");
        double se = readMark("New SE Marks: ");
        double dm2 = readMark("New DM2 Marks: ");

        Student updated = new Student(index, name, pdsa, se, dm2);
        bst.update(updated);

        System.out.println("Student record updated successfully.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Index Number to delete: ");
        String index = sc.nextLine().trim();

        if (index.isEmpty()) {
            System.out.println("Index number cannot be empty.");
            return;
        }

        if (bst.search(index) == null) {
            System.out.println("Student not found. Cannot delete.");
            return;
        }

        bst.delete(index);
        System.out.println("Student deleted successfully.");
    }

    private static void displayAll() {
        System.out.println("\n--- Student Results (Sorted by Index Number) ---");
        bst.displayInOrder();
    }

    private static double readMark(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value < 0 || value > 100) {
                    System.out.println("Marks must be between 0 and 100.");
                    continue;
                }
                return value;
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
        int n = (int) readMark("Enter N (e.g., 5 or 10): ");
        if (n <= 0) {
            System.out.println("N must be greater than 0.");
            return;
        }

        List<Student> list = new ArrayList<>(bst.inorderList());
        if (list.isEmpty()) {
            System.out.println("No records to rank.");
            return;
        }

        list.sort(Comparator.comparingDouble(Student::getAverage).reversed());

        System.out.println("\n--- Top " + n + " Students (By Average) ---");
        for (int i = 0; i < Math.min(n, list.size()); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }

    private static void saveToCsv() {
        String fileName = "students.csv";
        List<Student> list = bst.inorderList();

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("indexNo,name,pdsa,se,dm2");
            for (Student s : list) {
                pw.printf("%s,%s,%.1f,%.1f,%.1f%n",
                        s.getIndexNo(),
                        s.getName().replace(",", " "),
                        s.getPdsa(),
                        s.getSe(),
                        s.getDm2()
                );
            }
            System.out.println("Saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void loadFromCsv() {
        String fileName = "students.csv";
        File f = new File(fileName);

        if (!f.exists()) {
            System.out.println("No CSV found. Please save first (students.csv).");
            return;
        }

        bst.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String indexNo = parts[0].trim();
                String name = parts[1].trim();
                double pdsa = Double.parseDouble(parts[2].trim());
                double se = Double.parseDouble(parts[3].trim());
                double dm2 = Double.parseDouble(parts[4].trim());

                bst.insert(new Student(indexNo, name, pdsa, se, dm2));
            }
            System.out.println("Loaded successfully from " + fileName);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
