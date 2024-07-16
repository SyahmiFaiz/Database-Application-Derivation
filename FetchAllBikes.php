<?php

header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "bike_rental";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(array("error" => "Connection failed: " . $conn->connect_error));
    exit();
}

$sql = "SELECT * FROM bike";
$result = $conn->query($sql);

if ($result === false) {
    http_response_code(500);
    echo json_encode(array("error" => "Error executing query: " . $conn->error));
    exit();
}

$data = array();

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

$result->free();
$conn->close();

http_response_code(200);
echo json_encode($data);

?>
