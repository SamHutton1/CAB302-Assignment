package Server;
//will need to store time in LocalDateTime and teh ID as an int
import java.sql.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Scheduling {
    LocalDateTime scheduleS;
    LocalDateTime scheduleE;
    int BBID;
    Timestamp startSQL;
    Timestamp endSQL;
    public Scheduling(LocalDateTime schedStartTime, LocalDateTime schedEndTime, int BillBoardID){
        scheduleS=schedStartTime;
        scheduleE=schedEndTime;
        BBID=BillBoardID;
        startSQL = Timestamp.valueOf(scheduleS);
        endSQL = Timestamp.valueOf(scheduleE);
    }
    /*
    public Calendar retrieveDBCal() throws SQLException, IOException {
        //retieve byte[]
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();
        String query = "SELECT cal FROM calendar WHERE id ="+ BBID;
        ResultSet results = statement.executeQuery(query);
        byte[] calenderBytes = results.getBytes("cal");
        ByteArrayInputStream calByteInput = new ByteArrayInputStream(calenderBytes);
        ObjectInputStream calObject = new ObjectInputStream(calByteInput);
        calObject.read
        //turn to calendar

        return DBCal;
    }

    */
    public void updateDBCal() throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();



        String insertation = "INSERT INTO calendar(StartTime,EndTime,BBIDs) " +
                "VALUES("+ startSQL +","+ endSQL +","+BBID+");";


        statement.executeUpdate(insertation);
        String BBSchedUpdate = "UPDATE Billboards SET scheduled = True WHERE BBID = "+BBID+";";

    }

    public void removeOverlap() throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();
        

        String queryS = "SELECT ID FROM calendar WHERE StartTime BETWEEN "+ startSQL+"  AND "+ endSQL+";";
        String SdeleteStatement = "DELETE FROM calendar WHERE ID IN("+queryS+");";
        statement.executeUpdate(SdeleteStatement);



        String queryE = "SELECT ID FROM calendar WHERE EndTime BETWEEN "+ startSQL+"  AND "+ endSQL+";";
        String EdeleteStatement = "DELETE FROM calendar WHERE ID IN("+queryE+");";
        statement.executeUpdate(EdeleteStatement);


    }





}
