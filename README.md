# gestion-api
API para el sistema de inventarios, facturacion y cobranza de FERBO

## Recursos disponibles

### Asentamiento
* /gestion/asentamiento/{cp}

### Movil
* /movil/generar
* /movil/verificar
* /movil/deshabilitar

### Clientes
* /movil/clientes

### Ocupación Camara
* /movil/ocupacion/planta/{numUsuario} 
* /movil/ocupacion/planta/{numUsuario}?clientes=6
* /movil/ocupacion/planta/{numUsuario}?clientes=6&clientes=26

#### Nota
Para la ocupación de camaras son tres formas usando el mismo end-point: la primera es para un usuario con todos los clientes, la segunda es para un usuario y un cliente, y la ultima es para un usuario y varios clientes.

### Reporte Ocupacion Camara
* /movil/ocupacion/reporte/{numUsuario}?fecha=2026-04-22
* /movil/ocupacion/reporte/{numUsuario}?fecha=2026-04-22&clientes=6
* /movil/ocupacion/reporte/{numUsuario}?fecha=2026-04-22&clientes=6&clientes=26

#### Nota
Para el reporte de ocupación de camaras son tres formas usando el mismo end-point con un numero de usuario y fecha, ademas de lo siguiente: la primera es con todos los clientes, la segunda es con un cliente, y la última es para varios clientes.

### Kardex
* /movil/constancias/kardex/{fechaInicio}/{fechaFin}
* /movil/constancias/kardex/{fechaInicio}/{fechaFin}?cliente=6
* /movil/constancias/kardex/{fechaInicio}/{fechaFin}?cliente=6&planta=1

#### Nota
Para la consulta del kardex son tres formas usando el mismo end-point con una fecha de inicio y una fin, ademas de lo siguiente: la primera es con todos los clientes y plantas, la segunda es con un cliente y todas las plantas, y la última es con un cliente y una planta.

### Reporte de Kardex
* /movil/reporte/kardex/{folioCliente}

#### Nota
Para la consulta en pdf del kardex solo es pasarle el folio del cliente al end-point

### Reporte de Entradas
* /movil/reporte/entrada/{fechaInicio}/{fechaFin}
* /movil/reporte/entrada/{fechaInicio}/{fechaFin}?cliente=6
* /movil/reporte/entrada/{fechaInicio}/{fechaFin}?cliente=6&planta=1
* /movil/reporte/entrada/{fechaInicio}/{fechaFin}?cliente=6&planta=1&camara=1

#### Nota
Para obtener el reporte de entradas son cuatro formas usando el mismo end-point con una fecha de inicio y una fin, ademas de lo siguiente: la primera es con todos los clientes, todas las planta, y todas las camaras; la segunda es con un cliente, todas las plantas, y todas las camaras, la tercera es con un cliente, una planta y todas las camaras; y la última es con un cliente, una planta y una camara. 

### Reporte de Inventario
* /movil/reporte/inventario/{fecha}
* /movil/reporte/inventario/{fecha}?cliente=6
* /movil/reporte/inventario/{fecha}?cliente=6&planta=1

#### Nota
Para obtener el reporte de inventarios son tres formas usando el mismo end-point con una sola fecha, ademas de lo siguiente: la primera es el inventario solo con fecha; la seguna es el inventario de un solo fecha y cliente, y la tercera es el inventario con fecha, cliente y planta.

### Reporte de Salidas
* /movil/reporte/salida/{fechaInicio}/{fechaFin}
* /movil/reporte/salida/{fechaInicio}/{fechaFin}?cliente=6
* /movil/reporte/salida/{fechaInicio}/{fechaFin}?cliente=6&planta=1
* /movil/reporte/salida/{fechaInicio}/{fechaFin}?cliente=6&planta=1&camara=1

#### Nota
Para pbtener el reporte de inventarios de salida y son cuatro formas usando el mismo end-point con fecha inicio y de fin, ademas de lo siguiente: la segunda es con el periodo o intervalo de fechas, con todos los clientes, todas la plantas y camaras; la segunda es con un solo cliente, todas la camatas y plantas; la tercera es con el un cliente, una planta y todas la camaras; esta última es con un cliente, una planta y una camara.

### Consulta de Candado de Salida
* /movil/candadoSalida/{idCliente}

#### Nota
Para obtener el candado de salida solo es pasarle el identificador del cliente para poder empezar la consulta y ser mostrada

### Actualizar el Candado de Salida
* /movil/candadoSalida/{idCandado}

####Nota
Para actualizar el candado de salida solo es pasarle el identificador del candado y el objeto del candado

