<?php
 
  include "conexion.php";

  $numeroMesa=$_REQUEST["numeroMesa"];
    
  $datos=array();
  $rs = sqlsrv_query($conn,"SELECT numeroMesa,surtido,cantidad,producto,numeroSilla,carne,tipo FROM TB_Pedido WHERE numeroMesa = '$numeroMesa'");
  while($row=sqlsrv_fetch_object($rs))
  {
   	  $datos[] = $row;
  }
  echo json_encode($datos);

?>