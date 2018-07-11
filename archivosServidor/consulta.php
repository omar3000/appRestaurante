<?php
	include "conexion.php";
	//retorna los productos del catalago, menos los de la familia de panaderia
	$datos =array();
	$i =0;
	$consulta = "SELECT p.id, Codigo, Descripcion, f.nombre, p.subproducto FROM  producto p inner join subfamilia s on p.SubFamilia_id = s.id inner join familia f on s.Familia_id = f.id WHERE p.visible = 1 and f.nombre != 'Panadería' ";
	$result = mysqli_query($conn, $consulta);
	while($row = mysqli_fetch_assoc($result))  
	{
		   $datos[$i] = $row;
		   $i++;
	}
	mysqli_close($conn);
	echo json_encode($datos);
?>

