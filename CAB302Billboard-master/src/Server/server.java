package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.time.LocalDateTime;


public class server {

    /**
     * Main method for the server, upon first run it will check whether the tables exist in the database and create them
     * if they don't. Following this, it awaits a connection from either the viewer or the control panel. Depending on
     * what the ObjectInputStream receives, it will then connect to the database and return the requested information
     * in the form of an ObjectOutputStream.
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, ParseException {
        checkCreateDatabase();
        ServerSocket serverSocket = new ServerSocket(4444);
        String currentUser = "";
        for (;;) {
            //listens for a connection
            Socket socket = serverSocket.accept();
            System.out.println("Connected to: " + socket.getInetAddress());
            InputStream inputStream = socket.getInputStream();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //prepare to receive a message from the client
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            //read the message
            String fromClient = ois.readUTF();

            //print the received message and determine what the server needs to do
            System.out.println("received message from client: " + fromClient);
            if (fromClient.equals("listUsers")) {
                ArrayList<String> listOfValues;
                listOfValues = listUsername();
                int size = listOfValues.size();

                oos.writeInt(size);
                for (int i = 0; i < size; i++) {
                    oos.writeUTF(listOfValues.get(i));
                }
                oos.close();
            }
            else if (fromClient.equals("createUser")) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                String newUsername = ois.readUTF();
                String hashedPassword = ois.readUTF();
                //generate a salt
                byte[] saltBytes = generateSalt();
                String saltString = bytesToString(saltBytes);

                String saltedPassword = bytesToString(md.digest((hashedPassword + saltString).getBytes()));
                boolean createBillboard = ois.readBoolean();
                boolean editAllBillboards = ois.readBoolean();
                boolean scheduleBillboards = ois.readBoolean();
                boolean editUsers = ois.readBoolean();

                if (checkUsername(newUsername)) {
                    oos.writeUTF("bad");
                }
                else {
                    oos.writeUTF("good");
                    createUser(newUsername, saltedPassword, saltString, createBillboard, editAllBillboards, scheduleBillboards, editUsers);
                }
                oos.close();
                ois.close();

            }
            else if (fromClient.equals("clickedEditUsers")) {
                oos.writeBoolean(checkPermission(currentUser, "editUsers"));
                oos.close();
            }
            else if (fromClient.equals("deleteUser")) {
                //check that they're not trying to delete themselves
                String wantToDeleteUser = ois.readUTF();

                if (wantToDeleteUser.equals(currentUser)) {
                    oos.writeUTF("bad");
                }
                else {
                    oos.writeUTF("good");
                    //delete the user

                    deleteUser(wantToDeleteUser);
                }
//                ois.close();
//                oos.close();
            }
            else if (fromClient.equals("currentBillboard")) {
                //get any billboard from the database
                //will later need to get the billboard for the current time
                //foreach row in the database check if the current time is between that start time and end time

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                LocalDateTime now = LocalDateTime.now();
                LocalDate date = LocalDate.now();
                DayOfWeek dow = date.getDayOfWeek();


                ArrayList<String> listOfValues;
                listOfValues = getBillboards();
                //get billboard needs to check the day of the week as well


                int currentDay = 0;


                if (dow.toString() == "MONDAY") {
                    currentDay = 0;
                }
                else if (dow.toString() == "TUESDAY") {
                    currentDay = 1;
                }
                else if (dow.toString() == "WEDNESDAY") {
                    currentDay = 2;
                }
                else if (dow.toString() == "THURSDAY") {
                    currentDay = 3;
                }
                else if (dow.toString() == "FRIDAY") {
                    currentDay = 4;
                }

                String titleOfCurrentBillboard = "";
                for (int i = 0; i < listOfValues.size(); i+=4) {

                    String dayFromDatabase = listOfValues.get(i);
                    String starttime = listOfValues.get(i+ 1);
                    String endtime = listOfValues.get(i + 2);
                    String now1 = now.toString();


                    Date date_from = sdf.parse(starttime);
                    Date date_to = sdf.parse(endtime);
                    Date n = sdf.parse(now1.substring(11, 16));

                    //needs to first check the day of the week from the database

                    //check if there is a row in the database that matches the current time
                    //need to check for the day of the week now



                    if (Integer.parseInt(dayFromDatabase) == currentDay) {
                        if (date_from.before(n) && date_to.after(n)) {
                            //this is the name of the billboard that the billboard that the viewer needs to display
                            titleOfCurrentBillboard = listOfValues.get(i + 3);
                        }
                    }


                }

                //a method to get the xml string from the billboards table
                String currentXML = getCurrentXML(titleOfCurrentBillboard);


                //list of values has the time start and end time of every billboard so now just check if the current
                //time sits inside any of these

                //write the current billboard xml as a string
                oos.writeObject(currentXML);

                oos.close();



            }
            else if (fromClient.equals("scheduleBillboard")) {
                int dayOfWeek = ois.readInt();
                String startTime = ois.readUTF();
                String endTime = ois.readUTF();
                String title = ois.readUTF();

                submitSchedule(Integer.toString(dayOfWeek), startTime, endTime, title);
                ois.close();
            }
            else if (fromClient.equals("loginRequest")) {
                String username = ois.readUTF();
                String password = ois.readUTF();

                //check that the username exists in the system
                if (checkUsername(username)) {
                    //retrieve salt string from the database for the associated username
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    String salt = getSaltString(username);
                    String password1 = bytesToString(md.digest((password + salt).getBytes()));
                    if(verifyLogin(username, password1)) {
                        currentUser = username;
                        oos.writeUTF("verified");
                    }
                    else {
                        oos.writeUTF("login failed from server");

                    }

                }
                else {
                    oos.writeUTF("login failed from server");
                }




                oos.close();
                ois.close();
            }
            else if (fromClient.equals("submitBillboard")) {
                String title = ois.readUTF();
                String xml = ois.readUTF();

                if (checkBillboard(title)) {
                    oos.writeUTF("bad");
                }
                else {
                    oos.writeUTF("good");
                    submitBillboard(title, xml);

                }
                oos.close();
                ois.close();
            }
            else if (fromClient.equals("listBillboard")) {
                ArrayList<String> listOfValues;
                listOfValues = listBillboards();
                int size = listOfValues.size();

                oos.writeInt(size);
                for (int i = 0; i < size; i++) {
                    oos.writeUTF(listOfValues.get(i));
                }
                oos.close();
            }
            else if (fromClient.equals("clickedCreateUser")) {
                oos.writeBoolean(checkPermission(currentUser, "editUsers"));

                oos.close();
            }
            else if (fromClient.equals("clickedCreateBillboard")) {
                oos.writeBoolean(checkPermission(currentUser, "createBillboard"));
                oos.close();
            }
            else if (fromClient.equals("clickedScheduleBillboard")) {
                oos.writeBoolean(checkPermission(currentUser, "scheduleBillboard"));
                oos.close();
            }
            else if (fromClient.equals("clickedDeleteUser")) {
                oos.writeBoolean(checkPermission(currentUser, "editUsers"));
                oos.close();
            }
            else if (fromClient.equals("clickedListUsers")) {
                oos.writeBoolean(checkPermission(currentUser, "editUsers"));
                oos.close();
            }


            socket.close();




        }
    }

    /**
     * This method is called when the server receives a request to list users. It connects to the database and returns
     * an array list full of usernames from the database.
     * @return returns an array list of String usernames
     * @throws SQLException
     */
    public static ArrayList<String> listUsername() throws SQLException {

        ArrayList<String> userNames = new ArrayList<>();

        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from users");

        while (resultSet.next()) {
            userNames.add(resultSet.getString(1));
        }
        statement.close();
        return userNames;

    }

    public static ArrayList<String> getBillboards () throws SQLException {
        ArrayList<String> userNames = new ArrayList<>();

        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from schedule");

        while (resultSet.next()) {
            userNames.add(resultSet.getString(2));

            userNames.add(resultSet.getString(3));
            userNames.add(resultSet.getString(4));
            userNames.add(resultSet.getString(5));
        }
        statement.close();
        return  userNames;
    }

    /**
     * This method is called when the control panel needs to check if a user has the permissions to perform the action
     * they just clicked by checking whether the database has a true or false value
     * @param username takes the username of the current user as a string
     * @param permissionBeingChecked takes the permission currently being checked as a string
     * @return returns true if the user has the permission in the database, and false if they do not
     * @throws SQLException
     */
    public static boolean checkPermission(String username, String permissionBeingChecked) throws SQLException {

        boolean permissionStatus;
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username= '" + username + "';");


        if (permissionBeingChecked == "createBillboard") {
            return rs.getBoolean(4);
        }
        else if (permissionBeingChecked == "editBillBoard") {
            return rs.getBoolean(5);

        }
        else if (permissionBeingChecked == "scheduleBillboard") {
            return rs.getBoolean(6);
        }
        else if (permissionBeingChecked == "editUsers") {
            return rs.getBoolean(7);

        }
        else {
            return false;
        }


    }


    /**
     * This method is called when a login request is made to check whether or not the username provided in the request is
     * currently stored in the database or not
     * @param username the string of the username send in the request
     * @return returns true if the username is in the database and false if their username isn't
     * @throws SQLException
     */
    public static boolean checkUsername(String username) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();


        ResultSet rs = statement.executeQuery("SELECT * FROM users where username='" + username +"';");

        if (!rs.next()) {
            statement.close();
            return false;
        }
        else {
            statement.close();
            return true;
        }
    }

    /**
     * This method is called when a login request is made to confirm that the username and password are both stored in
     * the database in the same row.
     * @param username the string of the username sent in the request
     * @param password the string of the salted password for the database request
     * @return returns true if the username and password match an existing user and false if it does not
     * @throws SQLException
     */
    public static boolean verifyLogin(String username, String password) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM users where username='" + username + "' and password='" +
                password + "';");
        if (!rs.next()) {
            statement.close();
            return false;
        }
        else {
            statement.close();
            return true;
        }
    }

    /**
     * This method gets and returns the salt string associated with a given users username from the database
     * @param username the string of the username being checked
     * @return returns the salt string associated with the given users password
     * @throws SQLException
     */
    public static String getSaltString(String username) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        String saltString = "";

        ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username='" + username +"';");

        saltString = rs.getString(3);

        statement.close();

        return saltString;


    }

    /**
     * This is the method called whenever a new user is created. It takes the values provided from the control panel
     * and inputs them into the database.
     * @param userName string
     * @param password string
     * @param salt string
     * @param createBillboard boolean
     * @param editBillboard boolean
     * @param scheduleBillboard boolean
     * @param editUsers boolean
     * @throws SQLException
     */
    public static void createUser(String userName, String password, String salt, boolean createBillboard, boolean editBillboard,
                                  boolean scheduleBillboard, boolean editUsers) throws SQLException {
        //neds an exception to ensure that the username is unique
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();



        statement.executeUpdate("INSERT INTO users VALUES ('" + userName + "', '" + password + "', '" + salt + "', " + createBillboard +
                "," + editBillboard + "," + scheduleBillboard + "," + editUsers +");");
        statement.close();
    }


    /**
     * This method checks whether the tables exist in the database and if they do not exist, creates 3 tables and inputs
     * an administrator user intot he users table with the username "firstUser" and the password "password"
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static void checkCreateDatabase() throws SQLException, NoSuchAlgorithmException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();




       //instantiate a table for users if one doesnt already exist
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar(100) NOT NULL UNIQUE," +
                    "password varchar(45) NOT NULL, salt varchar(32) NOT NULL, createBillboard BOOL default '0', " +
                    "editBillboards BOOL default '0', scheduleBillboards BOOL default '0'," +
                    "editUsers BOOL default '0')");



       //create a table for billboards if doesnt already exist
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS billboards (uniqueID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "title varchar(100) NOT NULL, billBoardXML varchar(1000))");

        //create a table for scheduling if doesnt already exist
        //day of the week where 1 = monday
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS schedule (uniqueEntryNumber INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "dayOfTheWeek int (1) NOT NULL, startTime string(20) NOT NULL, endTime string(20), title varchar(100) NOT NULL);");

        createDefaultUser();
        statement.close();
    }

    public static void createDefaultUser() throws SQLException, NoSuchAlgorithmException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM users");
        if (rs.next()) {

        }
        else {
            //encode the first users password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String pass = "password";
            byte[] hashedPassword = md.digest(pass.getBytes());

            byte[] saltBytes = generateSalt();
            String saltString = bytesToString(saltBytes);

            String receivedHashedPassword = bytesToString(hashedPassword);
            String saltedPassword = bytesToString(md.digest((receivedHashedPassword + saltString).getBytes()));


            //create a default user with all permissions
            statement.executeUpdate("INSERT INTO users VALUES ('firstUser','" + saltedPassword + "','" + saltString +"', 1, 1, 1, 1)");
        }
    }

    /**
     * Code from the week 9 Q&A lecture that takes bytes and converts them to a string
     * @param hash a byte array that needs to be turned into a string
     * @return returns a string from the given byte array
     */
    public static String bytesToString(byte[] hash) {
        //code is from week 9 q & a lecture
        StringBuffer sb = new StringBuffer();
        for (byte b: hash) {
            sb.append(String.format("%02x", b & 0xFF));

        }
        return sb.toString();
    }

    /**
     * Also code from the week 9 Q&A lecture that fills a byte array with random bytes
     * @return returns a byte array with 32 random bytes
     */
    public static byte[] generateSalt() {
        Random rng = new Random();
        byte[] saltBytes = new byte[32];
        rng.nextBytes(saltBytes);
        return saltBytes;
    }

    /**
     * This method is used to enter a billboard's specifications into the database
     * @param title string title of the billboard
     * @param xml a string of the billboard's xml
     * @throws SQLException
     */
    public static void submitBillboard(String title, String xml) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO billboards (title, billBoardXML) VALUES ('" + title + "', '" + xml + "');");
        statement.close();
    }

    /**
     * This method is used to send the scheduling information for a billboard to the database
     * @param dayOfWeek takes string value 0-4 for each day of the work week
     * @param startTime string HH:mm format for the time the billboard should start displaying
     * @param endTime string HH:mm format for the time the billboard shoudl stop displaying
     * @param title string title of the billboard thats being put into the schedule
     * @throws SQLException
     */
    public static void submitSchedule(String dayOfWeek, String startTime, String endTime, String title) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO schedule (dayOfTheWeek, startTime, endTime, title) VALUES ('" + dayOfWeek
                + "', '" + startTime + "', '" + endTime + "', '" + title + "');");
        statement.close();
    }

    /**
     * This method returns a list of all the billboard titles to be displayed when a user clicks list billboards
     * @return return an array list string with the titles of the billboards
     * @throws SQLException
     */
    public static ArrayList<String> listBillboards() throws SQLException {

        ArrayList<String> userNames = new ArrayList<>();

        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from billboards");

        while (resultSet.next()) {
            userNames.add(resultSet.getString(2));
        }
        statement.close();
        return userNames;

    }

    /**
     * this method will get and return the XML for the title of the billboard entered into the parameters from the
     * database
     * @param title string title of the billboard you wish to get the xml for
     * @return returns the XML as a string
     * @throws SQLException
     */
    public static String getCurrentXML(String title) throws SQLException {
        //need to rewrite this to lookup the title of the actual current billboard
        String currentXML = "";

        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("select * from billboards where title='" + title + "';");

        if (rs.next()) {
            currentXML = rs.getString(3);

        }
        else {
            return "noBillboardsScheduled";
        }

        statement.close();
        return currentXML;
    }

    /**
     * This method is used to check if a billboard with a title already exists in the database and return true if
     * it does
     * @param title string title of the billboard you wish to check
     * @return return true if the billboard exists in the database and false if it doesn't
     * @throws SQLException
     */
    public static boolean checkBillboard(String title) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();


        ResultSet rs = statement.executeQuery("SELECT * FROM billboards where title='" + title +"';");

        if (!rs.next()) {
            statement.close();
            return false;
        }
        else {
            statement.close();
            return true;
        }
    }

    public static void deleteUser(String username) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();

        statement.execute("DELETE FROM users WHERE username='" + username + "';");

        statement.close();
    }








}