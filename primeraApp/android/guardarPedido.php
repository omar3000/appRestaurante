<?php

  include "conexion.php";

  $numeroMesa=$_REQUEST["numeroMesa"];
  $cantidad=$_REQUEST["cantidad"];
  $producto = $_REQUEST["producto"];
  $estado = $_REQUEST["estado"];
  $silla = $_REQUEST["numeroSilla"];
	$carne = $_REQUEST["tipo"];
  $noEmpelado = $_REQUEST["empleado"];
  $tipo = $_REQUEST["tipoPro"];

  date_default_timezone_set('America/Mexico_City');
  $hoy = date("mdHis");

  $surtido = 0;
	$abierto = 0;
	if($estado == 1)
	{
 	   sqlsrv_query($conn,"DELETE FROM TB_Pedido WHERE surtido = '$surtido' and numeroMesa = '$numeroMesa'");
     $sql="UPDATE TB_Pedido SET abierto='$abierto' WHERE numeroMesa = '$numeroMesa'"; 
     sqlsrv_query($conn,$sql);
	}
	if ($producto != "borrar")
	{
     /*$consulta = "SELECT vchDescripcion,tynTipo FROM TB_Producto WHERE vchDescripcion = '$producto'";
     $rs = sqlsrv_query($conn,$consulta);
     while($row=sqlsrv_fetch_object($rs))
     {
       $tipo = $row["tynTipo"];
     }*/
     
     if($estado == 100)
     {
       echo $sql="INSERT INTO TB_Pedido (numeroMesa, surtido,abierto,cantidad,producto,fecha,numeroSilla,carne,noEmpleado,tipo) VALUES ('$numeroMesa','$surtido','$abierto','$cantidad','$producto','$hoy','$silla','$carne','$noEmpelado','$tipo')"; 
     }
     else
     {
       echo $sql="INSERT INTO TB_Pedido (numeroMesa, surtido,abierto,cantidad,producto,numeroSilla,carne,tipo) VALUES ('$numeroMesa','$surtido','$abierto','$cantidad','$producto','$silla','$carne','$tipo')"; 
     }
	 	
	 	 $result = sqlsrv_query($conn,$sql);
		 $conn.close();
		
	 	 echo $result;
	}

?>