<?php

header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "bike_rental";

// Read POST data
$postData = file_get_contents('php://input');
$request = json_decode($postData);

if (!isset($request->BikeID) || !isset($request->BikeModel)) {
    http_response_code(400);
    echo json_encode(array("error" => "Missing required parameters."));
    exit();
}

// Generate a new RentalID (assuming AUTO_INCREMENT is handled by the database)
$rentalID = null; // Let the database handle this with auto_increment
$bikeID = $request->BikeID;
$rentalStatus = 1; // RentalStatus 1 means rented

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(array("error" => "Connection failed: " . $conn->connect_error));
    exit();
}

// Prepare and bind the SQL statement for inserting into rentallist
$stmt = $conn->prepare("INSERT INTO rentallist (BikeID, RentalStatus) VALUES (?, ?)");
$stmt->bind_param("ii", $bikeID, $rentalStatus);

if ($stmt->execute() === TRUE) {
    $last_id = $stmt->insert_id; // Get the last inserted ID
    http_response_code(200);
    echo json_encode(array("success" => "Booking saved successfully.", "RentalID" => $last_id));
} else {
    http_response_code(500);
    echo json_encode(array("error" => "Error saving booking: " . $stmt->error));
}

$stmt->close();
$conn->close();

?>
