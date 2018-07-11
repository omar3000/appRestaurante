<?php
  include "conexion.php";
  
  
  $numeroMesa = $_POST['json'];

  $consulta = "SELECT id FROM venta WHERE Serie = 'TO'  AND noMesa = '$numeroMesa' Limit 1";
	$result = mysqli_query($conn, $consulta); 
  $value = mysqli_fetch_assoc($result);
  $idVenta = $value['id'];

  $sql = "SELECT id FROM conceptos_por_venta WHERE Venta_id = '$idVenta'";
  $result6 = mysqli_query($conn, $sql); 
  $value4 = mysqli_fetch_assoc($result6);

  //si no existen conceptos relacionados con la venta la elimina para liberar la mesa
  if($value4 == null)
  {
    $sql = "DELETE FROM venta WHERE id = '$idVenta'";
    mysqli_query($conn, $sql);	
  }
  //si ya existen conceptos pedidos en la mesa solo liera la mesa
  else
  {
    $idVenta = $value['id'];
    $sql = "UPDATE venta SET Serie='TM' WHERE id = '$idVenta'";
    mysqli_query($conn, $sql);
  }
	
?>  