<?php

header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "bike_rental"; // Database name as per the SQL dump

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Variable for updating the rental status
    $bookingId = $_POST['bookingId'];

    // Prepare and bind the SQL statement for updating the rental status
    $stmt = $conn->prepare("UPDATE rentallist SET RentalStatus = 0 WHERE RentalID = ?");
    $stmt->bind_param("i", $bookingId);

    if ($stmt->execute() === TRUE) {
        echo json_encode(array("success" => "Status updated successfully"));
    } else {
        echo json_encode(array("error" => "Error updating status: " . $conn->error));
    }

    $stmt->close();
} else {
    echo json_encode(array("error" => "Unsupported request method"));
}

$conn->close();

?>
