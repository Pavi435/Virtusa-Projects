import java.util.Scanner;

public class StartApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        LibraryActions libEngine = new LibraryActions();

        libEngine.setupDefaultAdmin();

        while (true) {
            System.out.println("\n==================================");
            System.out.println("    LIBRARY LOGIN SYSTEM          ");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Exit System");
            System.out.print("Choose an option: ");

            String mainChoice = input.nextLine();

            if (mainChoice.equals("2")) {
                System.out.println("System shutting down. Goodbye!");
                break;
            } else if (mainChoice.equals("1")) {
                System.out.print("Username: ");
                String user = input.nextLine();
                System.out.print("Password: ");
                String pass = input.nextLine();

                LibraryUser loggedInUser = libEngine.authenticate(user, pass);

                if (loggedInUser == null) {
                    System.out.println("Invalid credentials. Try again.");
                    continue;
                }

                System.out.println("\nWelcome, " + loggedInUser.getMemName() + "!");

                if (loggedInUser.getUserRole().equals("Admin")) {
                    adminMenu(input, libEngine);
                } else if (loggedInUser.getUserRole().equals("Student")) {
                    studentMenu(input, libEngine, loggedInUser.getMemId());
                }
            } else {
                System.out.println("Invalid option.");
            }
        }
        input.close();
    }

    private static void adminMenu(Scanner input, LibraryActions libEngine) {
        int menuChoice = 0;
        while (menuChoice != 8) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Add a New Book");
            System.out.println("2. Display All Books");
            System.out.println("3. Search for a Book");
            System.out.println("4. Remove a Book");
            System.out.println("5. Register New User (Admin/Student)");
            System.out.println("6. Issue a Book");
            System.out.println("7. Return a Book");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            try {
                menuChoice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (menuChoice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int bId = Integer.parseInt(input.nextLine());
                    System.out.print("Enter Book Title: ");
                    String bTitle = input.nextLine();
                    System.out.print("Enter Author Name: ");
                    String bAuthor = input.nextLine();
                    libEngine.registerNewBook(bId, bTitle, bAuthor);
                    break;
                case 2:
                    libEngine.displayAllBooks();
                    break;
                case 3:
                    System.out.print("Enter Title or Author to search: ");
                    String keyword = input.nextLine();
                    libEngine.searchInventory(keyword);
                    break;
                case 4:
                    System.out.print("Enter Book ID to remove: ");
                    int delId = Integer.parseInt(input.nextLine());
                    libEngine.removeBookRecord(delId);
                    break;
                case 5:
                    System.out.print("Enter New User ID: ");
                    int mId = Integer.parseInt(input.nextLine());
                    System.out.print("Enter Role (Admin/Student): ");
                    String mRole = input.nextLine();
                    System.out.print("Enter Username: ");
                    String mUser = input.nextLine();
                    System.out.print("Enter Password: ");
                    String mPass = input.nextLine();
                    System.out.print("Enter Full Name: ");
                    String mName = input.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String mPhone = input.nextLine();
                    libEngine.registerMember(mId, mRole, mUser, mPass, mName, mPhone);
                    break;
                case 6:
                    System.out.print("Enter Transaction ID: ");
                    int tId = Integer.parseInt(input.nextLine());
                    System.out.print("Enter Book ID to Issue: ");
                    int issueBookId = Integer.parseInt(input.nextLine());
                    System.out.print("Enter Student ID: ");
                    int issueMemId = Integer.parseInt(input.nextLine());
                    libEngine.lendBook(tId, issueBookId, issueMemId);
                    break;
                case 7:
                    System.out.print("Enter Book ID being returned: ");
                    int retBookId = Integer.parseInt(input.nextLine());
                    libEngine.acceptReturn(retBookId);
                    break;
                case 8:
                    System.out.println("Logging out of Admin Dashboard...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void studentMenu(Scanner input, LibraryActions libEngine, int studentId) {
        int menuChoice = 0;
        while (menuChoice != 4) {
            System.out.println("\n=== STUDENT DASHBOARD ===");
            System.out.println("1. View All Books");
            System.out.println("2. Search for a Book");
            System.out.println("3. View My Borrowed Books");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            try {
                menuChoice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (menuChoice) {
                case 1:
                    libEngine.displayAllBooks();
                    break;
                case 2:
                    System.out.print("Enter Title or Author to search: ");
                    String keyword = input.nextLine();
                    libEngine.searchInventory(keyword);
                    break;
                case 3:
                    libEngine.viewMyBooks(studentId);
                    break;
                case 4:
                    System.out.println("Logging out of Student Dashboard...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}