<?php

	include "conexion.php";
	
	$estado = 1;
	
	$numeroMesa=$_REQUEST["noMesa"];
	$producto = "borrar";
	$rs = sqlsrv_query($conn,"SELECT numeroMesa,abierto FROM TB_Pedido WHERE numeroMesa = '$numeroMesa' and abierto = '$estado'");
	if(sqlsrv_fetch_array($rs,SQLSRV_FETCH_ASSOC) == null)
	{
		$consulta = sqlsrv_query($conn,"SELECT numeroMesa,abierto FROM TB_Pedido WHERE numeroMesa = '$numeroMesa'");
		if(sqlsrv_fetch_array($consulta,SQLSRV_FETCH_ASSOC) == null)
		{
			$sql="INSERT INTO TB_Pedido (numeroMesa, surtido,abierto,cantidad,producto,numeroSilla) VALUES ('$numeroMesa','0','$estado','0','$producto','0')"; 
	 		sqlsrv_query($conn,$sql);
			
		}
		else
		{
			$sql="UPDATE TB_Pedido SET numeroMesa ='$numeroMesa', abierto='$estado' WHERE numeroMesa = '$numeroMesa'"; 
	 		sqlsrv_query($conn,$sql);
		}
		
		echo "abierto";
		
	}
	else
	{		
		echo "cerrado";
	}


?>