package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
//TESTS: recieve data in correct format, create/access current schedule from DB?, make changes, save changes.
//so will take construcotrs of teh time and id
//will need to store time in LocalDateTime and teh ID as an int
// need to create schedule object, edit then seraialise and send to DB

// 1. recieves new calendar event
// 2. retrieves cal from the DB and deserialsizes
// 3. edits
// 4. serialises and sends to DB


class SchedulingTestDrivenDevelopment {
    private  Scheduling sched;


    @Test
    public void normalCase() throws SQLException {
        sched = new Scheduling(LocalDateTime.now(), LocalDateTime.of(2020,6,10,0,0),1);
        sched.updateDBCal();
        sched.removeOverlap();
    }

    public void futureInput() throws SQLException {
        sched = new Scheduling(LocalDateTime.of(2020,6,4,0,0), LocalDateTime.of(2020,6,10,0,0),1);
        sched.updateDBCal();
        sched.removeOverlap();

    }
    @Test
    public void OldTimeInput() throws SQLException {
        sched = new Scheduling(LocalDateTime.of(2019,6,10,0,0), LocalDateTime.now(),1);
        sched.updateDBCal();
        sched.removeOverlap();
    }



    @Test
    public void illegalBBID() throws SQLException {
        sched = new Scheduling(LocalDateTime.now(), LocalDateTime.of(2020,6,10,0,0),-1);
        sched.updateDBCal();
        sched.removeOverlap();
    }



    @Test
    public void unusedBBID () throws SQLException {
        sched = new Scheduling(LocalDateTime.now(), LocalDateTime.of(2020,6,10,0,0),1000);
        sched.updateDBCal();
        sched.removeOverlap();
    }

    @Test
    public void EndAndStartTimesbackwards () throws SQLException {


            sched = new Scheduling(LocalDateTime.now(), LocalDateTime.of(2019,6,10,0,0), 1);
            sched.updateDBCal();
            sched.removeOverlap();

    }





}