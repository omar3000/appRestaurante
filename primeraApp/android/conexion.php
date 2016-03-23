<?php
$serverName = "OMAR\SQLEXPRESS"; //serverName\instanceName

// Puesto que no se han especificado UID ni PWD en el array  $connectionInfo,
// La conexión se intentará utilizando la autenticación Windows.
$connectionInfo = array( "Database"=>"TORTASdb","UID"=>"usrencuesta", "PWD"=>"usr", "CharacterSet" => "UTF-8");
$conn = sqlsrv_connect( $serverName, $connectionInfo);
if( $conn ) {
     //echo "Conexión establecida";
}else{
     //echo "Conexión no se pudo establecer";
     //die( print_r( sqlsrv_errors(), true));
}
?>

