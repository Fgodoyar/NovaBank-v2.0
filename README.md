# Novabank

## Descripción
NovaBank es un programa de gestión bancario. La versión actual del proyecto, permite al usuario interactuar con los métodos básicos de un sistema bancario con acceso a datos.

Sus funcionalidades actuales son las siguientes:

- Registro de clientes, cuentas y movimientos.
- Ingreso, retiro y transacciones.
- Consultas y historial de movimientos.

---

## Arquitectura
Se ha refactorizado la arquitectura siguiendo una arquitectura por capas.
El propósito de esto es mejorar la mantenibilidad y separación de responsabilidades.

La arquitectura que se ha seguido es la siguiente:

CAPA DE PRESENTACIÓN-->CAPA DE SERVICIOS-->CAPA DE DOMINIO-->CAPA DE PERSISTENCIA

---

## Requisitos
Para ejecutar el proyecto, necesitarás tener:
- **Java Development Kit (JDK) 17** o superior.
- **Apache Maven 3.6** o superior.
- **PostgreSQL** instalado y en ejecución

---

## Configuración de la base de datos
- Creación de la base de datos
- Ejecución de scripts (schema.sql)

---

## Configuración de conexión
- DatabaseConnectionManager

```bash
private static final String URL = "jdbc:postgresql://localhost:5432/NovaBank";
private static final String USER = "postgres";
private static final String PASS = "sF0I+S2K2X4c";
```

---

## Ejecución del sistema
Para compilar el programa:
```bash
mvn clean compile
```
Para ejecutar el programa:
```bash
mvn exec:java
```
---

## Ejecución de tests
Ejecuta los test con el siguiente comando:
```bash
mvn test
```

---

## Patrones de diseño aplicados
- Singleton
- Factory
- Decorator

---
