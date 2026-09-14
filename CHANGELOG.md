# Changelog

Todos los cambios importantes en este proyecto serán documentados en este archivo.

Este proyecto sigue versionado semántico (SemVer) y el formato de [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/).

---
## [1.0.2] - 2026-09-14

### Added
- Se agregan los endpoints para la consulta de ordenes de retiro.
  - Obtener la lista de ordenes de retiro
  - Obtener el detalle de una orden de retiro
  - Cancelar una orden de retiro 

### Change
- Se agrega a la consulta por folio de cliente al endpoint de Kardex, manteniendo los demás criterios de búsqueda.

## [1.0.1] - 2026-08-19

### Added
- Corrección en la comunicación entre gestion-api y sgp-api

## [1.0.0] - 2026-04-27

### Added
- Creación del proyecto
- Configuración base de Spring Boot
- Autenticacion basica y JWT (filtros)
- Estructura de capas
  -Controller
  -Service
  -Repository
  -DTO
  -Mappers
  -Interface DAO
- Excepciones
- Models
- Tools

