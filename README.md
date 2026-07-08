# Sistema Cafetería Concurrente

Proyecto académico del curso Taller de Programación Concurrente.

## Descripción

Este proyecto implementa una primera versión de un sistema concurrente para registrar y procesar pedidos de una cafetería.

## Funcionalidades iniciales

- Registro de pedidos.
- Cola concurrente de pedidos.
- Productor de pedidos.
- Consumidor de pedidos.
- Validación de stock.
- Control básico de concurrencia con synchronized.
- Prueba de compilación y ejecución con GitHub Actions.

## Ejecución local

Compilar:

```bash
javac -d out src/*.java
Ejecutar:
java -cp out SistemaCafeteria
