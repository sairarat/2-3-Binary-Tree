import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TwoThreeTree tree = new TwoThreeTree();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the 2-3 Tree Program!");
        System.out.println("Available commands:");
        System.out.println("  insert <value> - Insert a value into the tree");
        System.out.println("  delete <value> - Delete a value from the tree");
        System.out.println("  search <value> - Search for a value in the tree");
        System.out.println("  print          - Print the current tree");
        System.out.println("  exit           - Exit the program");

        while (running) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                running = false;
                System.out.println("Exiting program...");
                continue;
            }

            String[] parts = input.split("\\s+");
            if (parts.length == 0) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }

            String command = parts[0];

            try {
                switch (command) {
                    case "insert":
                        if (parts.length != 2) {
                            System.out.println("Usage: insert <value>");
                            break;
                        }
                        int insertValue = Integer.parseInt(parts[1]);
                        tree.insert(insertValue);
                        System.out.println("Inserted " + insertValue);
                        tree.printTreeHorizontal();
                        break;

                    case "delete":
                        if (parts.length != 2) {
                            System.out.println("Usage: delete <value>");
                            break;
                        }
                        int deleteValue = Integer.parseInt(parts[1]);
                        tree.delete(deleteValue);
                        System.out.println("Deleted " + deleteValue);
                        tree.printTreeHorizontal();
                        break;

                    case "search":
                        if (parts.length != 2) {
                            System.out.println("Usage: search <value>");
                            break;
                        }
                        int searchValue = Integer.parseInt(parts[1]);
                        boolean found = tree.search(searchValue);
                        System.out.println("Value " + searchValue + " found: " + found);
                        break;

                    case "print":
                        tree.printTreeHorizontal();
                        break;

                    default:
                        System.out.println("Unknown command. Available commands: insert, delete, search, print, exit");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Please enter a valid integer.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        scanner.close();
    }
}