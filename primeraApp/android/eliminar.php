<?php
  include "conexion.php";
  
  $numeroMesa=$_REQUEST["numeroMesa"];
	
  $producto = "borrar";
  $estado = 0;
  $consulta = sqlsrv_query($conn,"DELETE FROM TB_Pedido WHERE producto = '$producto' and numeroMesa = '$numeroMesa'");
  if(sqlsrv_fetch_array($consulta,SQLSRV_FETCH_ASSOC) == null)
  {
	   $sql="UPDATE TB_Pedido SET abierto='$estado' WHERE numeroMesa = '$numeroMesa'"; 
	   $result = sqlsrv_query($conn,$sql);		
  }
	
?>  