

<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "tienda_online_donas";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
mysqli_set_charset($conn, 'utf8');
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


?>

