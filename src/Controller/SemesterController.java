package Controller;

import Model.SemesterProfile;
import Utils.SPException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * Created by Didac on 01/05/2017.
 */
public class SemesterController {

    // Constant queries ------------------------------------------------------------------------------------------------

    //TODO-IMPORTANT: Queries for semester. What data will be stored in the database?

    //? Query to insert a module after reading the file
    //? Query to

    private static final String QUERY_COUNT_SEMESTERS =
            "SELECT *, count *";
    private static final String QUERY_ALL_SEMESTERS =
            "SELECT start_date, end_date FROM SemesterProfile WHERE semester_id = ?";
    private static final String QUERY_USER_SEMESTER =
            "SELECT * FROM Semester_Profile WHERE Semester_Profile.user_id = ?";
    private static final String QUERY_INSERT_SEMESTER =
            "INSERT INTO Semester_Profile (start_date,end_date,user_id) VALUES (?,?,?)";
    private static final String QUERY_GET_SEMESTER_ID =
            "SELECT Semester_ID FROM SemesterProfile WHERE user_id = ? ";


    // Variables -------------------------------------------------------------------------------------------------------

    protected static DatabaseHandler dbhandler = DatabaseHandler.getInstance();

    // Constructor -----------------------------------------------------------------------------------------------------


    /**
     * Find semesters for a given user
     *
     * @param
     * @return
     */
    public static SemesterProfile find(int userId) {
        return find(QUERY_USER_SEMESTER, userId);
    }

    private static SemesterProfile find(String sql, Object... properties) {
        SemesterProfile semesterProfile = null;

        try (
                PreparedStatement statement = dbhandler.prepareStatement(sql, properties);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) semesterProfile = formSemester(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semesterProfile;
    }

    public static boolean insertSemester(SemesterProfile semester) {
        Date start = semester.getStartDate();
        Date end = semester.getEndDate();
        int userid = dbhandler.getUserSession().getId();
        try (
                PreparedStatement statement =
                        dbhandler.prepareStatement(QUERY_INSERT_SEMESTER, start, end,userid)

        ) {
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) throw new SPException("Failed to add Semester. No rows affected");
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static SemesterProfile formSemester(ResultSet resultSet) throws SQLException {
        int semesterId = resultSet.getInt("semester_id");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");

        return new SemesterProfile(semesterId, startDate, endDate);
    }
    // Load semester file?
    // TODO : add file checking or rely on SQL checks
    // TODO : One function for checking and parsing or TWO separate ones ?
//    public boolean checkFile(File file) throws IOException {
//        boolean valid = false;
//        String line;
//        final String separator = ",";
//        Module aModule;
//        Assessment assessment;
//        BufferedReader reader = openFile(file);
//        SemesterProfile semesterProfile = new SemesterProfile();
//        User user= dbhandler.getUserSession();
//
//        reader.readLine();
//        reader.readLine();
//        JSONObject obj = new JSONObject();
//        if (reader != null && file.getTitle().contains(".csv")) {
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(separator);
//                if(data.length == 2)
//                if(data.length == 2) {
//                    aModule = new Module(data[0], data[1]);
//                    ModuleController.insertModule(aModule);
//                }
//                else {
//                    reader.readLine();
//                    assessment = new Assessment(data[0],data[1],
//                            Integer.parseInt(data[2]), makeDate(data[3]));
//
//                }
//            }
//        }
//            return valid;
//
//    }
    // Helper functions ------------------------------------------------------------------------------------------------


}
