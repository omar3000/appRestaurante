<?php

  include "conexion.php";
 
  $string = $_POST['json'];
  $var = json_decode($string);
 

  date_default_timezone_set('America/Mexico_City');
  $hoy = date("Y-m-d H:i:s");

  ///TM  //T
  //2 SUCURSAL
  //1 CLIENTE MOSTRADOR
  //1 DOLAR


  //retorna el id del usuario del pin introducido
  $consulta = "SELECT id FROM usuario_punto_venta WHERE pin = '$var->pin' Limit 1";
  $result = mysqli_query($conn, $consulta);
  $value2 = mysqli_fetch_assoc($result);

  if($value2 != null)
  {

    //devuelve el id de la venta si ya hay una guardada
    $consulta = "SELECT id FROM venta WHERE Serie = 'TO'  AND noMesa = '$var->mesa' Limit 1";
    $result = mysqli_query($conn, $consulta);
    $value = mysqli_fetch_assoc($result);
    
    $idVenta = $value['id'];


    //revisa si hay pedidos para comprobar que no es una venta temporal
    $consulta = "SELECT id FROM conceptos_por_venta WHERE  Venta_id = '$idVenta'";
    $result6 = mysqli_query($conn, $consulta); 
    $value4 = mysqli_fetch_assoc($result6);

    //si es venta temporar ahora actualiza con los datos reales
    if($value4 == null)
    {

      //saca el ultimo folio de la serie 
      $consulta = "SELECT Folio FROM venta WHERE Serie = 'TM'  OR  'TO'  ORDER BY id DESC LIMIT 1";
      $result = mysqli_query($conn, $consulta);
      $ultimoFolio = mysqli_fetch_assoc($result);
      if($ultimoFolio['Folio'] == null)
        $ultimoFolio['Folio'] = 0;
                
      $ultimoFolio['Folio'] += 1;
                
      //inserta la venta real y liberar la mesa
      $sql = "UPDATE venta SET Serie='TM',Cliente_id=1,fecha='$hoy',catalogo_status_id=1,sucursal_id=2,usuario_id='$value2[id]',
      Folio='$ultimoFolio[Folio]' WHERE  id = '$idVenta'";
      mysqli_query($conn, $sql);  
      
    }
    else
    {
      $sql = "UPDATE venta SET Serie='TM' WHERE  id = '$idVenta'";
      mysqli_query($conn, $sql);  
    }
    
    $existe = false;
    $consulta2 = "SELECT id FROM conceptos_por_venta WHERE  Venta_id = '$idVenta' AND  preparado = 0";
    $result2 = mysqli_query($conn, $consulta2); 
    //recorre la consulta de los conceptos guardados de la venta
    while($row = mysqli_fetch_assoc($result2))
    {
      //recorre el peddio que se mando de la app en json
      foreach($var->mercancia as $obj)
      {
        //si coincide el id del pedido con el id de la consulta es que todavia existe
        if($obj->id == $row['id'])
        {
          $existe = true;
            
        }
      }
      if(!$existe)
      {
          //aqui eliminamos el concepto que ya no existe en el pedido
          $queryEliminar = "DELETE FROM conceptos_por_venta WHERE  id = '$row[id]'";
          mysqli_query($conn, $queryEliminar);
      }
      //restauramos la bandera
      $existe = false;
    }

    //recorre los objetos del json
    foreach($var->mercancia as $obj)
    {
      //los objetos que ya tienen id quiere decir que ya estan guardados y no es necesario insertarlo
      if($obj->id == 0)
      {

        //obtiene la informacion del producto
        $consulta =  "SELECT * FROM producto WHERE Descripcion =  '$obj->producto' Limit 1";
        $result = mysqli_query($conn, $consulta); 
        $value = mysqli_fetch_assoc($result);
        
        //saca el precio del producto 
        $consultaTipoCliente = "SELECT Precio FROM precio_por_tipocliente WHERE Sucursal_id = 2 AND Producto_id = '$value[id]'"; 
        $result = mysqli_query($conn, $consultaTipoCliente); 
        $valueP = mysqli_fetch_assoc($result);
        
        //calcula los costos del producto con los ivas
        $iva_unitario =  ($valueP['Precio'] / 100) * $value['iva'];
        $pecio_unitario_mas_iva = $valueP['Precio'] + $iva_unitario;
        $importe = ($valueP['Precio'] * $obj->cantidad);
        $iva_importe = ($importe / 100) * $value['iva'];
        $importe_mas_iva = $importe + $iva_importe;
        
        //se insertan los pedidos que vienen del json
        $sql="INSERT INTO conceptos_por_venta (Venta_id,Producto_id,TipoCliente_id,cantidad,precio_unitario,
        precio_unitario_original,iva_unitario,precio_unitario_mas_iva,importe, iva_importe, importe_mas_iva, noSilla, observaciones, 
        preparado, fecha_pedido)  VALUES ('$idVenta','$value[id]',1,'$obj->cantidad', '$valueP[Precio]', '$valueP[Precio]', 
        '$iva_unitario', '$pecio_unitario_mas_iva', '$importe', '$iva_importe', '$importe_mas_iva', '$obj->silla', 
        '$obj->observaciones', 0, '$hoy')";
        echo $sql;      
        mysqli_query($conn, $sql);
      }
    }
  }
  else
  {
    echo "pinInvalido";
  }
  
  mysqli_close($conn);

?>