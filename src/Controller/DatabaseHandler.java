package Controller;

import Model.SemesterProfile;
import Model.User;
import Utils.SPException;
import Utils.SPProperties;
import View.AlertDialog;
import javafx.scene.control.Alert;

import java.sql.*;

/**
 * Created by Didac on 29/04/2017.
 */
public class DatabaseHandler {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final String DB_PROPERTY_PREFIX = "xdn15mcu_studyplanner.jdbc";
    private static final String DB_PROPERTY_URL = "url";
    private static final String DB_PROPERTY_USERNAME = "username";
    private static final String DB_PROPERTY_PASSWORD = "password";


    // Methods ---------------------------------------------------------------------------------------------------------

    private static final DatabaseHandler DATABASE_HANDLER = new DatabaseHandler();

    private Connection connection;

    public static DatabaseHandler getInstance(){
        // Testing
        if (DATABASE_HANDLER.connection == null) {
            DATABASE_HANDLER.openConnection(DB_PROPERTY_PREFIX);
        }

        return DATABASE_HANDLER;
    }

    public Connection getConnection(){
        return DATABASE_HANDLER.connection;
    }

    private void openConnection(String propertiesPrefix){
        if (propertiesPrefix == null) {
            throw new SPException("Database name is null");
        }

        SPProperties properties = new SPProperties(propertiesPrefix);
        String url = properties.getProperty(DB_PROPERTY_URL);
        String username = properties.getProperty(DB_PROPERTY_USERNAME);
        String password = properties.getProperty(DB_PROPERTY_PASSWORD);

        try {
            DATABASE_HANDLER.connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            new AlertDialog(Alert.AlertType.ERROR,
                    "Couldn't connect to the internet:\n" + e.getMessage());
        }
    }

    //TODO: close connection on logging out? Or keep connection alive
    public void closeConnection() {
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of database handler for the given name.
     *
     * @param //name Database name it will look for in the properties
     * @return An instance of DatabaseHandler
     * @throws SPException If the database name doesn't exist or properties file is missing.
     */
//    public static DatabaseHandler getInstance(String name) throws SPException {
//        if (name == null) {
//            throw new SPException("Database name is null");
//        }
//
//        SPProperties properties = new SPProperties(name);
//        String url = properties.getProperty(DB_PROPERTY_URL);
//        //String driver = properties.getProperty(DB_PROPERTY_DRIVER);
//        String username = properties.getProperty(DB_PROPERTY_USERNAME);
//        String password = properties.getProperty(DB_PROPERTY_PASSWORD);
//
//        DatabaseHandler instance;
//        /*
//        // Load driver
//        try {
//            Class.forName(driver);
//        } catch (ClassNotFoundException e) {
//            throw new SPException("Driver missing from properties", e);
//        }
//        */
//        Connection getConnection() throws SQLException{
//            if (connection == null || connection.isClosed()) {
//                this.connection = DriverManager.getConnection(url,username,password);
//            }
//            return this.connection;
//        }
//
//        instance = new DriverManagerSP(url, username, password);
//
//        return instance;
//    }
    public PreparedStatement prepareStatement(String sql, boolean returnGeneratedKeys, Object... values) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        for (int i = 0; i < values.length; i++){
            statement.setObject(i + 1, values[i]);
        }
        return statement;
    }

    /**
     * Returns a connection to database.
     * @return Connection to database.
     * @throws SQLException If the connection fails.
     */
    //abstract Connection getConnection() throws SQLException;

    // Getters & Setters -----------------------------------------------------------------------------------------------

    /**
     * Returns the User Controller associated with the database handler
     * @return
     */
//    public UserController getUserController(){
//        return new UserController(this);
//    }

//    public SemesterController getSemesterController() {
//        return new SemesterController(this); }

    public User getUserSession(){ return Session.getSession().getUser(); }

    public SemesterProfile getSemesterSession() { return Session.getSession().getSemester(); }

    public void createSession(User user) { Session.createSession(user); }

    public void addSemesterToUser(SemesterProfile semesterProfile) {Session.addSemesterToUser(semesterProfile);}

    public void deleteSession() {Session.deleteSession();}
    //----------------------------------------------- ADD MORE CONTROLLERS HERE ----------------------------------------



}

// Session Class ----------------------------------------------------------------------------------------------


class Session {
    private static final Session SESSION = new Session();
    private User user;
    private SemesterProfile semesterProfile;

    public static void createSession(User user){
        SESSION.user = user;
    }

    public static void addSemesterToUser(SemesterProfile semesterProfile) { SESSION.semesterProfile = semesterProfile; }

    public static void deleteSession() {
        SESSION.semesterProfile = null;
        SESSION.user = null;
    }

    public static Session getSession(){
        return SESSION;
    }

    public SemesterProfile getSemester() {
        return semesterProfile;
    }

    public User getUser(){
        return user;
    }
}

