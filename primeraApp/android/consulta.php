<?php
	include "conexion.php";
	
	$datos=array();
	$consulta = "SELECT vchDescripcion,tynTipo,img,tynEstatus FROM TB_Producto WHERE tynEstatus =1";
	$rs = sqlsrv_query($conn,$consulta);
	while($row=sqlsrv_fetch_object($rs))
	{
   		$datos[] = $row;
	}
	echo json_encode($datos);


?>

