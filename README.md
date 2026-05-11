# QuickOrder - Sistema de Gestión de Pedidos y Logística

## 1. Información General
* **Asignatura:** Desarrollo FullStack 1 (DSY1103)
* **Evaluación:** Evaluación Parcial 2 - Avance de Arquitectura de Microservicios
* **Integrantes:**
  * Carlos Ramírez
  * Matías Estobar
* **Repositorio:** [https://github.com/CARLOSRAMIREZ2006/QuickOrder.git](https://github.com/CARLOSRAMIREZ2006/QuickOrder.git)

---

## 2. Contexto del Problema
**QuickOrder** es una solución de software diseñada bajo una arquitectura de microservicios para optimizar el flujo de pedidos, gestión de inventarios y logística de entrega en un entorno de comercio electrónico o retail. 

El sistema busca resolver la fragmentación de los procesos de venta, permitiendo una comunicación eficiente entre el registro de clientes, la disponibilidad de stock, el procesamiento de pagos y la gestión de reclamos o notificaciones, garantizando que cada componente sea independiente y escalable.

---

## 3. Arquitectura y Microservicios
Para cumplir con los requerimientos técnicos de la evaluación, se han implementado **10 microservicios** fundamentales que cubren el dominio del negocio:

1.  **Microservicio de Usuario:** Gestión de credenciales y perfiles de sistema.
2.  **Microservicio de Cliente:** Administración de datos de contacto y perfiles de compradores.
3.  **Microservicio de Producto:** Catálogo central de artículos disponibles.
4.  **Microservicio de Inventario:** Control de stock y asignación dinámica de productos.
5.  **Microservicio de Pedido:** Gestión del ciclo de vida de la orden de compra.
6.  **Microservicio de DetallePedido:** Desglose de productos y cantidades por cada orden.
7.  **Microservicio de Pagos:** Procesamiento de transacciones financieras y validación de pagos.
8.  **Microservicio de Despacho:** Logística de entrega y seguimiento de envíos.
9.  **Microservicio de Notificación:** Sistema de alertas y avisos para el usuario/cliente.
10. **Microservicio de Reclamos:** Gestión de soporte post-venta y resolución de incidencias.

---

## 4. Tecnologías Utilizadas
* **Backend:** Java con Spring Boot.
* **Gestión de Dependencias:** Maven.
* **Herramientas de Desarrollo:** IntelliJ IDEA.
* **Seguridad:** Spring Security (SecurityConfig).
* **Pruebas de API:** Postman (Documentación de flujos mediante peticiones GET, POST, PUT, DELETE).
* **Control de Versiones:** Git / GitHub.

---

## 5. Implementación Técnica (Pasos Realizados)
El desarrollo siguió un flujo estructurado de 24 pasos clave, que incluyen:
* Configuración inicial mediante **Spring Initializr**.
* Creación de paquetes siguiendo el patrón **Model-Repository-Service-Controller** para cada dominio.
* Implementación de una capa de **Seguridad** centralizada.
* Configuración de un **GlobalExceptionHandler** para la gestión estandarizada de excepciones.
* Pruebas de integración en Postman para simular:
    * Creación de clientes y productos.
    * Asignación de inventario y confirmación de pedidos con descuento de stock.
    * Procesamiento de pagos y generación de despachos.
    * Envío de notificaciones y registro de reclamos.

---

## 6. Aportes Individuales

### Aporte de Carlos Ramírez
* Configuración del repositorio base y estructura inicial del proyecto en Spring Boot.
* Implementación de los microservicios de Usuario, Cliente, Producto e Inventario.
* Configuración de la capa de seguridad (`SecurityConfig`) y manejo global de excepciones.
* Documentación de pruebas iniciales en Postman.

### Aporte de Matías Estobar
* Desarrollo de los microservicios core de negocio: Pedido, DetallePedido y Pagos.
* Implementación de la lógica de integración para Despacho, Notificación y Reclamos.
* Validación de flujos de negocio completos (Confirmación de pedido -> Descuento de stock -> Pago).
* Pruebas de integración final y verificación de endpoints.
