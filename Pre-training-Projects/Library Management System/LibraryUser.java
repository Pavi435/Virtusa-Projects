public class LibraryUser {
    private int memId;
    private String userRole;
    private String username;
    private String password;
    private String memName;
    private String phoneNum;

    public LibraryUser(int id, String role, String user, String pass, String name, String phone) {
        this.memId = id;
        this.userRole = role;
        this.username = user;
        this.password = pass;
        this.memName = name;
        this.phoneNum = phone;
    }

    public int getMemId() { return memId; }
    public String getUserRole() { return userRole; }
    public String getUsername() { return username; }
    public String getMemName() { return memName; }

    @Override
    public String toString() {
        return memId + "," + userRole + "," + username + "," + password + "," + memName + "," + phoneNum;
    }
}