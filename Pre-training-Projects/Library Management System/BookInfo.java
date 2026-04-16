public class BookInfo {
    private int bookId;
    private String bTitle;
    private String bAuthor;
    private boolean availableStatus;

    public BookInfo(int id, String title, String author, boolean status) {
        this.bookId = id;
        this.bTitle = title;
        this.bAuthor = author;
        this.availableStatus = status;
    }

    public int getBookId() { return bookId; }
    public String getBTitle() { return bTitle; }
    public String getBAuthor() { return bAuthor; }
    public boolean isAvailable() { return availableStatus; }

    public void setAvailable(boolean status) { this.availableStatus = status; }

    @Override
    public String toString() {
        return bookId + "," + bTitle + "," + bAuthor + "," + availableStatus;
    }
}