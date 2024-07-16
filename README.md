DAD Project – Bike Rental

Total Applications involved for this project.  

 1. Bike Rental Application  

    -main function: Rent Booking  

2. ⁠Bike Return Application  

    -main function: Change bike status from “rented” to “returned” 
-------
 
Brief explanation for each application 

i. Admin Application 


   Purpose: The Admin application is designed to help administrators manage bike rentals. 


Key Features: 

   - Fetching Rental Data: 

     Fetches current rental data from a server and displays it in a table. 

     The data includes rental ID, bike ID, and rental status (e.g., rented or returned).

   - Changing Rental Status: 

     Allows the admin to change the status of a rental by entering a rental ID. 

     Sends a request to the server to update the rental status in the database.
     
- User Interface: 

     Displays a table of current rentals. 

     Provides input fields and buttons to update rental statuses.
  

Technical Details: 

   - The application communicates with a backend server using HTTP GET and POST requests. 

   - Data is fetched in JSON format and parsed into Java objects. 

   - The GUI is built using Swing, with components like JFrame, JTable, JTextField, and JButton.
  

Class Structure: 

   - Admin: Main class that sets up the GUI and handles user interactions. 

   - RentalEntry: Helper class to represent individual rental entries with rental ID, bike ID, and rental status.



-----------
ii. Customer Application 

   Purpose: The Customer application is designed to allow customers to view and rent available bikes. 

   Key Features: 

   - Fetching Available Bikes: 

  Fetches the list of available bikes from a server and displays them in a table. 

  The data includes bike ID and bike model. 
  

  - Displaying Bike Details: 

  When a bike is selected in the table, its details are displayed in a text area. 
  

 - Renting Selected Bike: 

  Allows the customer to rent a selected bike. 

  Sends a request to the server to save the rental record in the database. 

  Displays a confirmation dialog and a success message upon successful rental. 
  

Technical Details: 

- The application communicates with a backend server using HTTP GET and POST requests. 

- Data is fetched in JSON format and parsed into Java objects. 

- The GUI is built using Swing, with components like JFrame, JTable, JTextArea, and JButton.
  

Class Structure: 

- Customer: Main class that sets up the GUI and handles user interactions. 

- BikeEntry: Helper class to represent individual bike entries with bike ID and model.

-------------
iii. Middleware Functions 

Both applications use middleware functions to handle server communication, data parsing, and user interface updates. These functions include: 

  - fetchData(): Fetches data from the server and updates the GUI. 

  - parseJsonResponse(): Parses JSON responses from the server into Java objects. 

  - saveRentalRecord() (Customer app): Sends rental data to the server to save a rental record. 

  - changeStatus() (Admin app): Sends rental status updates to the server.
    
----------
iv. Summary 

   - Admin Application: Focused on managing rentals, updating rental statuses, and displaying rental data. 

   - Customer Application: Focused on viewing available bikes, displaying bike details, and renting bikes. 

   - Both applications are built with Swing for the GUI and use HTTP communication to interact with a backend server, handling data in JSON format.

----------

Architecture/Layer diagram for each of the apps including the middleware 




----------
List of URL end points middleware RESTful/SOAP/Socket  

1. http://localhost/RESTFUL/FetchFromDB.php 
2. http://localhost/RESTFUL/SaveToDB.php 
3. http://localhost/RESTFUL/FetchAllBikes.php 
4. http://localhost/RESTFUL/SaveBooking.php

---------
Functions/Features in the middleware 

1. Fetching Rental Data (fetchData)
   
- HTTP Method: GET 
- Description: Fetches current rental data from the server and updates the table in the GUI. 

  Implementation: 

- Establishes a GET request to the server. 

- Reads the JSON response. 

- Parses the JSON response into RentalEntry objects. 

- Updates the GUI table with the fetched data.

---------
2. Changing Rental Status (changeStatus)
 
- HTTP Method: POST 

- Description: Changes the status of a rental based on the provided booking ID. 

  Implementation: 

- Establishes a POST request to the server with the booking ID as a parameter. 

- Sends the booking ID to the server. 

- Checks the HTTP response code to determine if the request was successful. 

- Displays a success or error message based on the response. 

- Refreshes the rental data in the GUI upon successful status change. 

--------
3. Parsing JSON Response (parseJsonResponse) 

- Description: Parses the JSON response received from the server into a list of RentalEntry objects. 

  Implementation: 

- Converts the JSON response string into a JSONArray. 

- Iterates through the JSONArray and creates RentalEntry objects for each JSON object. 

- Returns a list of RentalEntry objects. 


-------
4. Helper Classes 

- RentalEntry Class 

  Attributes: 

- int rentalID 

- int bikeID 

- int rentalStatus 

  Methods: 

- Constructor to initialize the attributes. 

- Getters for each attribute. 

- getRentalStatusAsString() to convert the numerical rental status to a human-readable string ("Rented" or "Returned"). 

 
---------
5. User Interface Components 

 - Admin Class (Main GUI Class) 

  Attributes: 

- JFrame frame 

- DefaultTableModel tableModel 

- JTable rentalTable 

- JTextField bookingIdTextField 

  Methods: 

- initialize(): Sets up the GUI components and layout. 

- fetchData(): Fetches and displays rental data from the server. 

- changeStatus(): Handles the rental status change logic. 

- parseJsonResponse(String jsonResponse): Parses the JSON response to create a list of RentalEntry objects. 

 ---------
6. GUI Components 

- Main Frame (JFrame) 

Components: 

- Header label ("Bike Rentals Management") 

- Center panel containing the rental table 

- Bottom panel containing input field for booking ID and change status button 

- Rental Table (JTable) 

 Columns: 

- "Rental ID" 

- "Bike ID" 

- "Rental Status" 

Change Status Section 

  Components: 

- Input field for booking ID 

- Button to trigger status change 

These functions and features together create a comprehensive interface for managing bike rentals, enabling data fetching, status updates, and real-time display updates within a graphical user interface. 


------
The database and tables involve in the projects 
 
Database: bike_rental.sql 

Tables Involve:  
i. bike 
ii. rentallist 
 
---------
Video Presentation Link:  
 https://youtu.be/hIrxoaE7Khc
