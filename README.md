# CAB302-Assignment
A billboard display system comprised of three core systems - Control Panel GUI, Billboard Viewer, Command Line Server + Database

High-level Design: 
 
There are three packages - control panel, server and viewer 
 
**Control panel has 11 classes:**  
A majority are just different windows of the control panel GUI and are used to traverse through the actions 
you can perform within it. From most of these classes, there is also communication with the server inbuilt 
into each of them 

**controlPanel** - this is the main GUI screen and from here you can choose to enter any of the other screens  
**createBillboard** - if permitted, opens up to reveal the screen where billboards can be created and saved  
**createUser** - if permitted, opens up to reveal the screen where users can be created and saved  
**deleteUsers** - if permitted, opens up to reveal the screen where users can be deleted from the database  
**graphicalViewBB** - Renders a preview of the billboard to inspect before submitting it to the server or 
schedule  
**listBillboards** - no permissions required, shows the title of every billboard stored in the database  
**listUsers** - if permitted opens to reveal the username of every user in the database  
**loginPage** - the first screen opened and where credentials must be entered in order to progress further into 
the control panel program  
**Main** - is called to run the login page  
**saveToXML** - Saves user field inputs to XML for export to server  
**scheduleBillboard** - if permitted, opens up the screen to allow you to schedule any of the saved billboard 
to display at any time during the week  
 
 
**Server has 3 classes and a test class**  

**Server** - the main class in this package. All connections from other programs connect here and this 
connects to the database. This class has a plethora methods as every single command send to the server 
runs through it so it did all the searching, editing and deleting of information stored in the database  
**DBConnection** - used so that the db.props file can work  
**Scheduling** - A result of the test driven development, a class designed around scheduling billboards and 
storing time in the database  
**SchedulingTestDrivenDevelopment** - scheduling was built through a test driven development and this is 
the class where the tests were built  
 
**Viewer has 8 classes and 2 test classes**  

**allFields** - Renders the layout for the structure of a billboard if all fields are entered  
**imageSolo** - Renders the layout for the structure of a billboard if only the picture field is entered  
**Main** - communicated with the server and received the XML data. Displayed the error message  
**MessInfo** - Renders the layout for the structure of a billboard if the message and info fields are entered  
**PicInfo** - Renders the layout for the structure of a billboard if the picture and info fields are entered  
**PicMess - Renders the layout for the structure of a billboard if the picture and message fields are entered  
**readRender** - does the XML parsing and rendering. Determines the render variables from the XML  
**ResizeLabelFont** - extension of Jlabel  used to maximum the font size of the message field- kept the text 
on 1 line  
**mainTest** - tests for running the main file functions  
**readRenderTest** - tests for running the render and read functions 


Instructions to Run
1. Open source code in intellij or other java interpreter  
2. Download sqlite-jdbc  
3. Put sqlite file into the very top folder which should be labelled CAB302 Billboard master 
4. Go to the project structure tab, which can be found in the file tab of intellij go to libraries and 
add the sqlite jdbc 
5. Create or copy a db.props file and place it in the same location as the sqlite file 
Structure  used 
6. Now navigate to the server folder in the src folder, from there open the main and run it to 
start up the server 
7. Once the server is running you can run both the viewer and control panel by going to their 
respective main files  
8. Initialisation and installation should be complete and the program should function 

Administrator user login: 
Username: firstUser 
Password: password 
