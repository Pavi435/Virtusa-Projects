import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class LibraryActions {
    private final String BOOK_FILE = "InventoryData.txt";
    private final String USER_FILE = "RegisteredMembers.txt";
    private final String LOAN_FILE = "LogRecords.txt";

    public void setupDefaultAdmin() {
        List<String> users = FileStorage.readAllRecords(USER_FILE);
        if (users.isEmpty()) {
            LibraryUser admin = new LibraryUser(1, "Admin", "Adm_Pavi", "P@vi123", "System Administrator", "0000000000");
            FileStorage.saveRecord(USER_FILE, admin.toString());
        }
    }

    public LibraryUser authenticate(String user, String pass) {
        List<String> records = FileStorage.readAllRecords(USER_FILE);
        for (String r : records) {
            String[] parts = r.split(",");
            if (parts.length >= 6 && parts[2].equals(user) && parts[3].equals(pass)) {
                return new LibraryUser(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5]);
            }
        }
        return null;
    }

    public void registerNewBook(int id, String title, String author) {
        BookInfo b = new BookInfo(id, title, author, true);
        FileStorage.saveRecord(BOOK_FILE, b.toString());
        System.out.println("Success: Book added to inventory.");
    }

    public void displayAllBooks() {
        List<String> records = FileStorage.readAllRecords(BOOK_FILE);
        System.out.println("\n--- Library Inventory ---");
        for (String r : records) {
            String[] parts = r.split(",");
            String status = Boolean.parseBoolean(parts[3]) ? "Available" : "Issued Out";
            System.out.println("ID: " + parts[0] + " | Title: " + parts[1] + " | Author: " + parts[2] + " | Status: " + status);
        }
    }

    public void removeBookRecord(int bookId) {
        List<String> records = FileStorage.readAllRecords(BOOK_FILE);
        List<String> updatedRecords = new ArrayList<>();
        boolean found = false;

        for (String r : records) {
            if (!r.startsWith(bookId + ",")) {
                updatedRecords.add(r);
            } else {
                found = true;
            }
        }

        if (found) {
            FileStorage.overwriteFile(BOOK_FILE, updatedRecords);
            System.out.println("Success: Book removed from system.");
        } else {
            System.out.println("Error: Book ID not found.");
        }
    }

    public void searchInventory(String keyword) {
        List<String> records = FileStorage.readAllRecords(BOOK_FILE);
        System.out.println("\n--- Search Results ---");
        boolean found = false;
        for (String r : records) {
            String[] parts = r.split(",");
            if (parts[1].toLowerCase().contains(keyword.toLowerCase()) || parts[2].toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("Found: [ID: " + parts[0] + "] " + parts[1] + " by " + parts[2]);
                found = true;
            }
        }
        if (!found) System.out.println("No matching books found.");
    }

    public void registerMember(int id, String role, String user, String pass, String name, String phone) {
        LibraryUser u = new LibraryUser(id, role, user, pass, name, phone);
        FileStorage.saveRecord(USER_FILE, u.toString());
        System.out.println("Success: " + role + " registered.");
    }

    public void lendBook(int transId, int bookId, int userId) {
        List<String> bookRecords = FileStorage.readAllRecords(BOOK_FILE);
        List<String> updatedBooks = new ArrayList<>();
        boolean bookFound = false;
        boolean isAvailable = false;

        for (String r : bookRecords) {
            String[] parts = r.split(",");
            if (Integer.parseInt(parts[0]) == bookId) {
                bookFound = true;
                if (Boolean.parseBoolean(parts[3])) {
                    isAvailable = true;
                    parts[3] = "false";
                    r = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3];
                }
            }
            updatedBooks.add(r);
        }

        if (!bookFound) {
            System.out.println("Error: Book does not exist.");
            return;
        }
        if (!isAvailable) {
            System.out.println("Error: Book is currently issued to someone else.");
            return;
        }

        FileStorage.overwriteFile(BOOK_FILE, updatedBooks);

        String today = LocalDate.now().toString();
        BookTransaction bt = new BookTransaction(transId, bookId, userId, today);
        FileStorage.saveRecord(LOAN_FILE, bt.toString());

        System.out.println("Success: Book issued. Due in 7 days.");
    }

    public void acceptReturn(int bookId) {
        List<String> loanRecords = FileStorage.readAllRecords(LOAN_FILE);
        List<String> updatedLoans = new ArrayList<>();
        BookTransaction activeLoan = null;

        for (String r : loanRecords) {
            String[] parts = r.split(",");
            if (Integer.parseInt(parts[1]) == bookId) {
                activeLoan = new BookTransaction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
            } else {
                updatedLoans.add(r);
            }
        }

        if (activeLoan == null) {
            System.out.println("Error: No active issue record found for this Book ID.");
            return;
        }

        String today = LocalDate.now().toString();
        long fineAmt = activeLoan.calcLateFee(today);
        if (fineAmt > 0) {
            System.out.println("LATE RETURN! You must pay a fine of: Rs " + fineAmt);
        } else {
            System.out.println("Book returned on time. Fine: Rs 0");
        }

        FileStorage.overwriteFile(LOAN_FILE, updatedLoans);

        List<String> bookRecords = FileStorage.readAllRecords(BOOK_FILE);
        List<String> updatedBooks = new ArrayList<>();
        for (String r : bookRecords) {
            String[] parts = r.split(",");
            if (Integer.parseInt(parts[0]) == bookId) {
                parts[3] = "true";
                r = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3];
            }
            updatedBooks.add(r);
        }
        FileStorage.overwriteFile(BOOK_FILE, updatedBooks);
        System.out.println("Success: Book status updated to Available.");
    }

    public void viewMyBooks(int userId) {
        List<String> loans = FileStorage.readAllRecords(LOAN_FILE);
        List<String> books = FileStorage.readAllRecords(BOOK_FILE);
        boolean found = false;
        System.out.println("\n--- My Borrowed Books ---");

        for (String loan : loans) {
            String[] lParts = loan.split(",");
            if (Integer.parseInt(lParts[2]) == userId) {
                int bId = Integer.parseInt(lParts[1]);
                for (String book : books) {
                    String[] bParts = book.split(",");
                    if (Integer.parseInt(bParts[0]) == bId) {
                        System.out.println("Book ID: " + bId + " | Title: " + bParts[1] + " | Issued On: " + lParts[3]);
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("You have no currently borrowed books.");
        }
    }
}