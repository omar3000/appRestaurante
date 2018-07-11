<?php

	include "conexion.php";
	
	$estado = 1;
	
	$numeroMesa = $_POST['json'];

	date_default_timezone_set('America/Mexico_City');
	$hoy = date("Y-m-d H:i:s");

	$consulta = "SELECT id FROM venta WHERE Serie = 'TO'  AND noMesa = '$numeroMesa' Limit 1";
	$result = mysqli_query($conn, $consulta); 
	$value = mysqli_fetch_assoc($result);
	//si existe la venta con estatus TO quiere decir que esta abierta en otro lado
	if($value  == null)
	{
		$consulta = "SELECT id FROM venta WHERE Serie = 'TM'  AND noMesa = '$numeroMesa' Limit 1";
		$result = mysqli_query($conn, $consulta); 
		$value = mysqli_fetch_assoc($result);
		//si no existe la venta crea una temporal para bloquear la mesa con el estatus TO
		if($value== null)
		{
			$sql = "INSERT INTO venta (Cliente_id,fecha,catalogo_status_id, sucursal_id, usuario_id,
			Folio, Serie, noMesa) VALUES (1,'$hoy',1,2,1,0,'TO','$numeroMesa')";
			mysqli_query($conn, $sql);
		}
		//si ya existe actualiza la venta para bloquear la mesa con el estatus TO
		else
		{
			$idVenta = $value['id'];
			$sql = "UPDATE venta SET Serie='TO' WHERE id = '$idVenta'";
			mysqli_query($conn, $sql);
		}
		
		echo 'abierto';
		
	}
	else
	{		
		echo 'cerrado';
	}


?>