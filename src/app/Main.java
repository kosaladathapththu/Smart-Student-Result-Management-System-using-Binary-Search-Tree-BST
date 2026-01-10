public static void main(String[] args) {
    BinarySearchTree bst = new BinarySearchTree();

    bst.insert(new Student("2025A002", "Saman", 65, 60, 72));
    bst.insert(new Student("2025A001", "Kamal", 80, 70, 60));
    bst.insert(new Student("2025A003", "Nimal", 30, 40, 35));

    System.out.println("=== Sorted Students ===");
    bst.displayInOrder();

    System.out.println("\nDeleting 2025A002...\n");
    bst.delete("2025A002");

    System.out.println("=== After Delete ===");
    bst.displayInOrder();
}
