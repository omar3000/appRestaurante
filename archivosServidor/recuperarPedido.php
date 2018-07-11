<?php
 
  include "conexion.php";

  
  $noMesa = $_POST['json'];

  $datos =array();
  $i =0;
  
  //regresa los conceptos del pedido en la mesa actual 
  
  $consulta = "SELECT id FROM venta WHERE (Serie = 'TM' OR Serie = 'TO') AND noMesa = '$noMesa' ORDER BY id DESC LIMIT 1";
  $result = mysqli_query($conn, $consulta);
  $value = mysqli_fetch_assoc($result);

  $consulta = "SELECT conceptos_por_venta.id, cantidad,noSilla,preparado,observaciones, p.Descripcion FROM  conceptos_por_venta 
  INNER JOIN producto as p ON conceptos_por_venta.Producto_id = p.id WHERE conceptos_por_venta.Venta_id = '$value[id]'";
  
  $result = mysqli_query($conn, $consulta);
  while($row = mysqli_fetch_assoc($result))  
  {   	 
	  $datos[$i] = $row;
	  $i++;
  }

  mysqli_close($conn);

  if($i != 0)
  {
	  echo json_encode($datos);

  }
  else
  {
	  echo 'vacio';
  }
?>