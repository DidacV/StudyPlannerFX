package Model;

/**
 * Created by Didac on 30/04/2017.
 */
public class User {

    // Properties ------------------------------------------------------------------------------------------------------

    private int id;
    private String email;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private boolean isStaff;

    public User(){}

    public User(String email, String username, String password, String firstname, String lastname, boolean isStaff){
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isStaff = isStaff;
    }

    public User(int id, String email, String username, String password, String firstname, String lastname, boolean isStaff){
        this(email,username,password,firstname,lastname,isStaff);
        this.id = id;
    }

    // Getters and setters----------------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    // Overrides--------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("User[id=%d,email=%s,firstname=%s,lastname=%s,username=%s]",
                id, email, firstname, lastname, username);
    }
}
