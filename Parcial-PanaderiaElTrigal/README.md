# Panadería El Trigal

Proyecto académico desarrollado con Java 17, JavaFX, Maven y JUnit 5.

## Requisitos
- JDK 17 instalado.
- IntelliJ IDEA.
- Conexión a Internet la primera vez que Maven descargue JavaFX y JUnit.

## Abrir en IntelliJ IDEA
1. Descomprime la carpeta.
2. En IntelliJ, selecciona **Open** y abre la carpeta `PanaderiaElTrigal`.
3. Abre `pom.xml` y selecciona **Load Maven Project** si IntelliJ lo solicita.
4. Verifica que el proyecto use JDK 17: `File > Project Structure > Project SDK`.
5. Espera a que Maven descargue las dependencias.

## Ejecutar la aplicación
Abre la pestaña Maven y ejecuta:

`Plugins > javafx > javafx:run`

También puedes abrir `src/main/java/view/Aplicacion.java` y ejecutar su método `main` si IntelliJ tiene configurado JavaFX mediante Maven.

## Ejecutar las pruebas
En la pestaña Maven ejecuta:

`Lifecycle > test`

Las pruebas están en `src/test/java`.

## Organización
- `model`: clases del negocio, enumeraciones e interfaz.
- `controllers`: comunicación entre la vista y el modelo.
- `viewcontrollers`: lógica de la pantalla JavaFX.
- `view`: punto de entrada de la aplicación.
- `src/main/resources/view`: archivo FXML.
- `src/test`: pruebas unitarias JUnit 5.

## Reglas implementadas
- Los códigos de empleados y pedidos son únicos.
- Los pedidos nuevos comienzan en estado `PENDIENTE`.
- La asignación ignora cajeros y conserva el orden de registro en caso de empate.
- Solo se asigna un panadero con tiempo suficiente.
- Cancelar un pedido asignado devuelve los minutos reservados.
- Los pedidos cancelados permanecen registrados.
